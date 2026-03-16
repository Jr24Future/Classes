"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.FunctionBodyIncluder = exports.RetryWithError = exports.RetryPrompt = exports.DocCommentIncluder = exports.SnippetIncluder = exports.Prompt = exports.defaultPromptOptions = void 0;
const dedent_1 = __importDefault(require("dedent"));
const exploreAPI_1 = require("./exploreAPI");
const report_1 = require("./report");
const syntax_1 = require("./syntax");
const handlebars_1 = __importDefault(require("handlebars"));
const fs_1 = __importDefault(require("fs"));
function defaultPromptOptions() {
    return {
        includeSnippets: false,
        includeDocComment: false,
        includeFunctionBody: false,
    };
}
exports.defaultPromptOptions = defaultPromptOptions;
/**
 * Structured representation of a prompt we send to the model.
 *
 * In general, our prompts look like this:
 *
 * ```js
 * let mocha = require('mocha');            // -+
 * let assert = require('assert');          //  | Imports
 * let pkg = require('pkg');                // -+
 *
 * // usage #1                              // -+
 * ...                                      //  |
 * // usage #2                              //  | Usage snippets
 * ...                                      // -+
 *
 * // this does...                          // -+
 * // @param foo                            //  |
 * // @returns bar                          //  | Doc comment
 * ...                                      // -+
 *
 * // fn(args)                              //    Signature of the function we're testing
 * // function fn(args) {                   // -+
 * //     ...                               //  | Function body (optional)
 * // }                                     // -+
 *
 * describe('test pkg', function() {        //    Test suite header
 *   it('test fn', function(done) {         //    Test case header
 * ```
 *
 * The structured representation keeps track of these parts and provides methods
 * to assemble them into a textual prompt and complete them into a test case.
 */
class Prompt {
    constructor(fun, usageSnippets, options) {
        var _a;
        this.fun = fun;
        this.usageSnippets = usageSnippets;
        this.options = options;
        this.provenance = [];
        const sanitizedPackageName = (0, exploreAPI_1.sanitizePackageName)(fun.packageName);
        this.imports = (0, dedent_1.default) `
            let mocha = require('mocha');
            let assert = require('assert');
            let ${sanitizedPackageName} = require('${fun.packageName}');\n`;
        this.signature = fun.signature;
        if (options.includeFunctionBody) {
            this.functionBody = fun.descriptor.implementation;
        }
        else {
            this.functionBody = "";
        }
        this.suiteHeader = `describe('test ${sanitizedPackageName}', function() {\n`;
        this.testHeader = `    it('test ${fun.accessPath}', function(done) {\n`;
        if (options.includeDocComment) {
            this.docComment = (0, syntax_1.trimAndCombineDocComment)((_a = fun.descriptor.docComment) !== null && _a !== void 0 ? _a : "");
        }
        else {
            this.docComment = "";
        }
    }
    /**
     * Assemble the usage snippets into a single string.
     */
    assembleUsageSnippets() {
        if (!this.options.includeSnippets) {
            return "";
        }
        else {
            return this.usageSnippets
                .map((snippet, index) => {
                const lines = snippet.split("\n");
                return `// usage #${index + 1}\n` + lines.join("") + "\n";
            })
                .join("");
        }
    }
    /**
     * Assemble a prompt to send to the model from the structured
     * representation.
     */
    assemble() {
        // return this.embedInTemplate(this.signature, this.functionBody, this.docComment, this.assembleUsageSnippets(),
        const signature = this.signature;
        const functionBody = this.functionBody;
        const docComments = this.docComment;
        const snippets = this.assembleUsageSnippets();
        const headers = this.imports + this.suiteHeader + this.testHeader;
        const templateFileName = this.options.templateFileName;
        const template = fs_1.default.readFileSync(templateFileName, "utf8");
        const compiledTemplate = handlebars_1.default.compile(template);
        let expandedTemplate = compiledTemplate({
            signature: signature.trim(),
            docComments: docComments ? docComments : "",
            functionBody: functionBody
                ? `This function is defined as follows:\n\`\`\`\n${functionBody.trim()}\n\`\`\``
                : "",
            snippets: snippets
                ? `You may use the following examples to guide your implementation:\n\`\`\`\n${snippets}\n\`\`\``
                : "",
            code: headers,
        });
        while (expandedTemplate.includes("\n\n\n")) {
            // avoid unnecessary blank lines
            expandedTemplate = expandedTemplate.replace("\n\n\n", "\n\n");
        }
        while (expandedTemplate.includes("```\n\n")) {
            // avoid empty lines at the beginning of fenced code blocks
            expandedTemplate = expandedTemplate.replace("```\n\n", "```\n");
        }
        while (expandedTemplate.includes("\n\n```")) {
            // avoid empty lines at the end of fenced code blocks
            expandedTemplate = expandedTemplate.replace("\n\n```", "\n```");
        }
        if (expandedTemplate.includes("Please")) {
            expandedTemplate = expandedTemplate.replace("Please", "\nPlease"); // start new paragraph for the instructions
        }
        if (expandedTemplate.includes("This function")) {
            expandedTemplate = expandedTemplate.replace("This function", "\nThis function"); // start new paragraph for the function body
        }
        if (expandedTemplate.includes("You may use")) {
            expandedTemplate = expandedTemplate.replace("You may use", "\nYou may use"); // start new paragraph for the examples
        }
        return expandedTemplate;
    }
    /**
     * Given a test body suggested by the model, assemble a complete,
     * syntactically correct test.
     */
    completeTest(body, stubOutHeaders = true) {
        let code = "";
        // add imports if first line of body does not contain "require"
        const line = body.split("\n")[0];
        if (line.indexOf("require") === -1) {
            code += this.imports + "\n";
        }
        // add headers if they are not already in the body
        if (!body.includes("describe(")) {
            code =
                code +
                    (stubOutHeaders
                        ? // stub out suite header and test header so we don't double-count identical tests
                            "describe('test suite', function() {\n" +
                                "    it('test case', function(done) {\n"
                        : this.suiteHeader + this.testHeader) +
                    // add the body, making sure the first line is indented correctly
                    body.trim().replace(/^(?=\S)/, " ".repeat(8)) +
                    "\n";
        }
        else {
            // only add the body if it already includes test/suite headers
            code += body;
        }
        // close brackets
        const fixed = (0, syntax_1.closeBrackets)(code);
        // beautify closing brackets
        const beautified = fixed === null || fixed === void 0 ? void 0 : fixed.source.replace(/\}\)\}\)$/, "    })\n})");
        return beautified;
    }
    // public embedInTemplate(signature: string, functionBody: string, docComments: string, snippets: string, body: string): string {
    //   const templateFileName = this.options.templateFileName;
    //   const template = fs.readFileSync(templateFileName!, "utf8");
    //   const compiledTemplate = handlebars.compile(template);
    //   let expandedTemplate = compiledTemplate({
    //     signature: signature.trim(),
    //     docComments: docComments ? docComments : "",
    //     functionBody: functionBody ? `This function is defined as follows:\n\`\`\`\n${functionBody.trim()}\n\`\`\`` : "",
    //     snippets: snippets ? `You may use the following examples to guide your implementation:\n\`\`\`\n${snippets}\n\`\`\`` : "",
    //     code: body });
    //   while (expandedTemplate.includes('\n\n\n')){ // avoid unnecessary blank lines
    //     expandedTemplate = expandedTemplate.replace('\n\n\n','\n\n');
    //   }
    //   while (expandedTemplate.includes('\`\`\`\n\n')){ // avoid empty lines at the beginning of fenced code blocks
    //     expandedTemplate = expandedTemplate.replace('\`\`\`\n\n','\`\`\`\n');
    //   }
    //   while (expandedTemplate.includes('\n\n\`\`\`')){ // avoid empty lines at the end of fenced code blocks
    //     expandedTemplate = expandedTemplate.replace('\n\n\`\`\`','\n\`\`\`');
    //   }
    //   if (expandedTemplate.includes('Please')){
    //     expandedTemplate = expandedTemplate.replace('Please', '\nPlease'); // start new paragraph for the instructions
    //   }
    //   return expandedTemplate;
    // }
    withProvenance(...provenanceInfos) {
        this.provenance.push(...provenanceInfos);
        return this;
    }
    functionHasDocComment() {
        return this.fun.descriptor.docComment !== undefined;
    }
}
exports.Prompt = Prompt;
/**
 * A prompt refiner that adds usage snippets to the prompt.
 */
class SnippetIncluder {
    get name() {
        return "SnippetIncluder";
    }
    refine(original, completion, outcome) {
        if (!original.options.includeSnippets &&
            original.usageSnippets.length > 0) {
            return [
                new Prompt(original.fun, original.usageSnippets, {
                    ...original.options,
                    includeSnippets: true,
                }),
            ];
        }
        return [];
    }
}
exports.SnippetIncluder = SnippetIncluder;
/**
 * A prompt refiner that adds a function's doc comments to the prompt.
 */
class DocCommentIncluder {
    get name() {
        return "DocCommentIncluder";
    }
    refine(original, completion, outcome) {
        if (!original.options.includeDocComment &&
            original.functionHasDocComment()) {
            return [
                new Prompt(original.fun, original.usageSnippets, {
                    ...original.options,
                    includeDocComment: true,
                }),
            ];
        }
        return [];
    }
}
exports.DocCommentIncluder = DocCommentIncluder;
class RetryPrompt extends Prompt {
    constructor(prev, body, err) {
        super(prev.fun, prev.usageSnippets, prev.options);
        this.prev = prev;
        this.body = body;
        this.err = err;
    }
    assemble() {
        const rawFailingTest = this.prev.completeTest(this.body);
        const templateFileName = this.options.retryTemplateFileName;
        const template = fs_1.default.readFileSync(templateFileName, "utf8");
        const compiledTemplate = handlebars_1.default.compile(template);
        const expandedTemplate = compiledTemplate({
            test: rawFailingTest,
            error: this.err,
        });
        return expandedTemplate;
    }
}
exports.RetryPrompt = RetryPrompt;
/**
 * A prompt refiner that, for a failed test, adds the error message to the
 * prompt and tries again.
 */
class RetryWithError {
    get name() {
        return "RetryWithError";
    }
    refine(original, completion, outcome) {
        if (!(original instanceof RetryPrompt) &&
            outcome.status === report_1.TestStatus.FAILED) {
            return [new RetryPrompt(original, completion, outcome.err.message)];
        }
        return [];
    }
}
exports.RetryWithError = RetryWithError;
/**
 * A prompt refiner that includes the body of the function in the prompt.
 */
class FunctionBodyIncluder {
    get name() {
        return "FunctionBodyIncluder";
    }
    refine(original, completion, outcome) {
        if (!original.options.includeFunctionBody &&
            original.fun.descriptor.implementation !== "") {
            return [
                new Prompt(original.fun, original.usageSnippets, {
                    ...original.options,
                    includeFunctionBody: true,
                }),
            ];
        }
        return [];
    }
}
exports.FunctionBodyIncluder = FunctionBodyIncluder;
//# sourceMappingURL=promptCrafting.js.map