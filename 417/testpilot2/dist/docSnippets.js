"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.getPackageMethods = exports.findFencedCodeBlocks = exports.callsAPIMethod = exports.trimSnippetToMaxLength = exports.getDocSnippets = exports.getDocSnippetsAsJson = void 0;
const yargs_1 = __importDefault(require("yargs"));
const helpers_1 = require("yargs/helpers");
const fs = __importStar(require("fs"));
const snippetHelper_1 = require("./snippetHelper");
const exploreAPI_1 = require("./exploreAPI");
const path = __importStar(require("path"));
var jsExtensions = require("common-js-file-extensions");
const snippetHelper = new snippetHelper_1.Snippets();
function emptyPkgSnippetInfo() {
    return {
        numMethods: 0,
        numMethodsWithEg: 0,
        totalSnippets: 0,
        snippetsPerMethod: [],
    };
}
/**
 * Get code file contents for use as snippets
 * @param files set of code files to extract snippets from
 * @returns a set of snippets in the given code files
 */
function getSnippetsFromCodeFiles(files) {
    let snippets = new Set();
    files.forEach((file) => snippets.add(fs.readFileSync(file, "utf8")));
    return snippets;
}
/**
 * Mine snippets from fenced code blocks in markdown files and example JS files and return as JSON.
 * @param dirName the directory in which to search
 * @param numSnippets the number of snippets to mine
 * @param methods array of methods to extract usage snippets for
 * @param maxLength The maximum number of lines to include in each snippet.
 * @returns a stringified JSON object containing the mined snippets and stats about the mined snippets
 */
function getDocSnippetsAsJson(dirName, numSnippets, methods, maxLength) {
    const result = getDocSnippets(dirName, numSnippets, methods, maxLength);
    let allPkgSnippets = new Set();
    let structuredResult = emptyPkgSnippetInfo();
    structuredResult.numMethods = methods.length;
    result.forEach((snippets, method) => {
        snippets.forEach(allPkgSnippets.add, allPkgSnippets);
        if (snippets.length > 0)
            structuredResult.numMethodsWithEg += 1;
        const snippResults = {
            method: method,
            snippets: snippets,
        };
        structuredResult.snippetsPerMethod.push(snippResults);
    });
    structuredResult.totalSnippets = allPkgSnippets.size;
    return JSON.stringify(structuredResult, null, 2);
}
exports.getDocSnippetsAsJson = getDocSnippetsAsJson;
/**
 * Mine snippets from fenced code blocks in markdown files.
 * @param dirName the directory in which to search
 * @param numSnippets the maximum number of snippets to mine per method, or "all" to mine all snippets
 * @param methods array of methods to extract usage snippets for
 * @param maxLength The maximum number of lines to include in each snippet.
 * @returns a map associating a method name to a set of usage snippets for that method
 */
function getDocSnippets(dirName, numSnippets, methods, maxLength) {
    // find all markdown files in the given directory
    let [mdFiles, exampleCodeFiles] = findExampleFiles(dirName);
    // initialize result; initially each method has an empty set of associated snippets
    let result = new Map();
    methods.forEach((method) => result.set(method, []));
    // extract snippets from each markdown file
    let snippets = new Set();
    mdFiles.forEach((mdFile) => {
        let codeBlocks = findFencedCodeBlocks(mdFile);
        codeBlocks.forEach((codeBlock) => {
            let extractedSnippets = extractSnippetsFromCodeBlock(stripFencing(codeBlock));
            extractedSnippets.forEach((snippet) => {
                // when code blocks contain multiple examples, only the first example may require packages
                // in such cases, we add these requires to the snippets for the remaining examples
                if (hasNoRequires(snippet)) {
                    const requires = findAllRequires(codeBlock);
                    if (requires.length > 0) {
                        snippet = requires + "\n" + snippet;
                    }
                }
                snippets.add(snippet);
            });
        });
    });
    getSnippetsFromCodeFiles(exampleCodeFiles).forEach((snippet) => snippets.add(snippet));
    // iterate through all snippets. If a snippet contains a method name,
    // associate it with the set of snippets for that method. Filter out
    // snippets that are less than 2 lines long.
    snippets.forEach((snippet) => {
        const tokens = tokenize(snippet);
        methods.forEach((method) => {
            if (tokens.includes(method) &&
                nrLinesInSnippet(snippet) > 1 &&
                callsAPIMethod(snippet, method)) {
                snippet = trimSnippetToMaxLength(snippet, maxLength);
                result.get(method).push(snippet);
            }
        });
    });
    if (numSnippets !== "all") {
        // for each method, select the required number of diverse snippets
        methods.forEach((method) => {
            const allSnippetsForMethod = result.get(method);
            const selectedSnippets = snippetHelper.selectSnippets(new Set(allSnippetsForMethod), numSnippets);
            result.set(method, Array.from(selectedSnippets));
        });
    }
    return result;
}
exports.getDocSnippets = getDocSnippets;
/**
 * Find the number of lines in a given snippet
 * @param snippet the snippet
 * @returns the number of lines in the given snippet
 */
function nrLinesInSnippet(snippet) {
    return snippet.split("\n").length;
}
/**
 * Trim a given code snippet to a max number of lines
 * @param snippet the snippet
 * @param maxLength maximum number of lines to inlcude in the snippet
 * @returns the trimmed snippet with only maxLength lines kept
 */
function trimSnippetToMaxLength(snippet, maxLength) {
    const lines = snippet.split("\n").slice(0, maxLength);
    return lines.join("\n");
}
exports.trimSnippetToMaxLength = trimSnippetToMaxLength;
/**
 * Make sure the method named is involved in a call
 * @param snippet the snippet
 * @param method the method name
 * @returns true if the method name is involved in a call, false otherwise
 */
function callsAPIMethod(snippet, methodName) {
    const lines = snippet.split("\n");
    for (let i = 0; i < lines.length; i++) {
        const regex = new RegExp("\\b" + methodName + "\\(");
        if (lines[i].search(regex) !== -1) {
            return true;
        }
    }
    return false;
}
exports.callsAPIMethod = callsAPIMethod;
/**
 * Check if given directory path ends with one the predefined example directories
 * @param dirPath directory path to check
 * @returns true if dirPath ends with one of the predefined example directories, false otherwise
 */
function isExampleDir(dirPath) {
    const exampleDirs = ["examples", "example", "demo"];
    return exampleDirs.includes(path.basename(dirPath));
}
/***
 * Check if given file has a JS code file extension
 * @param fileName file name to check
 * @returns true if fileName has JS code file extension, false otherwise
 */
function isJSFile(fileName) {
    return jsExtensions.code.includes(path.extname(fileName).slice(1));
}
/**
 * Recursively search for markdown files and example code files in the given directory
 * @param dir the directory to search
 * @returns a set of markdown files and a set of example code files, in the given directory
 **/
function findExampleFiles(directoryName) {
    let markDownFiles = new Set();
    let exampleCodeFiles = new Set();
    try {
        let files = fs.readdirSync(directoryName);
        for (let file of files) {
            let filePath = directoryName + "/" + file;
            if (isExampleDir(directoryName) &&
                fs.statSync(filePath).isFile() &&
                isJSFile(filePath)) {
                exampleCodeFiles.add(filePath);
            }
            else if (fs.statSync(filePath).isFile() && file.endsWith(".md")) {
                markDownFiles.add(filePath);
            }
            else if (fs.statSync(filePath).isDirectory()) {
                if (directoryName.indexOf("node_modules") === -1) {
                    const [newMarkDownFiles, newExampleCodeFiles] = findExampleFiles(filePath);
                    newMarkDownFiles.forEach((file) => markDownFiles.add(file));
                    newExampleCodeFiles.forEach((file) => exampleCodeFiles.add(file));
                }
            }
        }
    }
    catch (err) {
        console.log(err);
    }
    return [markDownFiles, exampleCodeFiles];
}
/**
 * Find fenced code blocks in a given markdown file
 * @param file the markdown file to search
 * @returns a set of fenced code blocks in the given markdown file
 */
function findFencedCodeBlocks(fileName) {
    let codeBlocks = new Set();
    let regExp = /^```[\s\S]*?^```$/gm;
    let fileContents = fs.readFileSync(fileName, "utf8");
    let matches = fileContents.match(regExp);
    if (matches) {
        for (let match of matches) {
            if ((match.startsWith("```js") && !match.startsWith("```json")) ||
                match.startsWith("```javascript") ||
                match.startsWith("```ts") ||
                match.startsWith("```typescript") ||
                match.startsWith("```tsx") ||
                match.startsWith("```\n")) {
                codeBlocks.add(match);
            }
        }
    }
    return codeBlocks;
}
exports.findFencedCodeBlocks = findFencedCodeBlocks;
/**
 * Tokenize a code block into a list of alphanumeric words, ignoring all other characters.
 * @param code the code block to tokenize
 * @returns a list of tokens in the given code block
 */
function tokenize(code) {
    const word = /[a-zA-Z_$][\w$]*/g;
    return code.match(word) || [];
}
/**
 * Remove the first and last line from a fenced code block
 * @param codeBlock the code block to remove the first and last line from
 * @returns the code block with the first and last line removed
 */
function stripFencing(snippet) {
    let lines = snippet.split("\n");
    lines.shift();
    lines.pop();
    return lines.join("\n");
}
/**
 * Remove trailing comments at the end of a snippet (these may arise if
 * code blocks are split when they contain multiple examples.
 * @param snippet the snippet to remove trailing comments from
 * @returns the snippet with trailing comments removed
 */
function removeTrailingComments(snippet) {
    let lines = snippet.split("\n");
    while (true) {
        let line = lines.pop();
        if (line.startsWith("//") || (line === null || line === void 0 ? void 0 : line.length) === 0) {
            break;
        }
    }
    return lines.join("\n");
}
/**
 * Sometimes, a fenced code block contains multiple examples. In such cases, we assume that
 * a new snippet is started whenever a previously declared variable is redeclared.
 * @param codeBlock the code block to extract snippets from
 * @returns a set of snippets in the given code block
 */
function extractSnippetsFromCodeBlock(codeBlock) {
    let snippets = new Set();
    let lines = codeBlock.split("\n");
    let startOfCurrentSnippet = 0;
    let declaredVars = new Set();
    for (let i = 0; i < lines.length; i++) {
        let line = lines[i];
        if (line.startsWith("let") ||
            line.startsWith("const") ||
            line.startsWith("var")) {
            let tokens = tokenize(line);
            let varName = tokens[1];
            if (declaredVars.has(varName)) {
                // we reached the end of a snippet if we see a redeclaration of a previously declared variable
                snippets.add(removeTrailingComments(lines.slice(startOfCurrentSnippet, i - 1).join("\n")));
                startOfCurrentSnippet = i;
                declaredVars.clear();
            }
            declaredVars.add(varName);
        }
    }
    if (startOfCurrentSnippet < lines.length) {
        snippets.add(lines.slice(startOfCurrentSnippet, lines.length).join("\n"));
    }
    return snippets;
}
function findAllRequires(codeBlock) {
    let requires = [];
    let lines = codeBlock.split("\n");
    for (let i = 0; i < lines.length; i++) {
        let line = lines[i];
        if (line.indexOf("require") !== -1) {
            requires.push(line);
        }
    }
    return requires.join("\n");
}
function hasNoRequires(snippet) {
    return findAllRequires(snippet) === "";
}
function getPackageMethods(pkgName, methodsFile) {
    var api;
    if (methodsFile != undefined) {
        //use json file if provided, otherwise, re-explore API
        api = JSON.parse(fs.readFileSync(methodsFile, "utf8"));
    }
    else {
        api = (0, exploreAPI_1.exploreAPI)(pkgName);
    }
    return Array.from(api.getFunctions(pkgName)).map((f) => f.functionName);
}
exports.getPackageMethods = getPackageMethods;
if (require.main === module) {
    (async () => {
        const parser = (0, yargs_1.default)((0, helpers_1.hideBin)(process.argv))
            .usage("Usage: $0 --package <package path> --mode <mode> [--method <method name> --methodsFile <methods json file>]")
            .options({
            package: {
                type: "string",
                description: "path of package to analyze",
                demandOption: true,
            },
            mode: {
                type: "string",
                description: "specify singlemethod to analyze only one specified method in the package or batchmode to analyze all methods in the package. If batchmode is specified, and no methods json file is provided, exploreAPIs will be called to extract method info.",
                choices: ["singlemethod", "batchmode"],
                default: "singlemethod",
            },
            method: {
                type: "string",
                description: "method name to analyze, if singlemethod mode is used",
                demandOption: false,
            },
            methodsFile: {
                type: "string",
                description: "path to json file containing all the package methods, as extracted by exploreAPIs",
                demandOption: false,
            },
            outputFile: {
                type: "string",
                description: "optional output file for results",
                demandOption: false,
            },
        });
        const argv = await parser.argv;
        const pkgDir = argv.package;
        const methName = argv.method;
        const methodsFile = argv.methodsFile;
        const mode = argv.mode;
        let methods;
        if (mode == "batchmode") {
            methods = getPackageMethods(pkgDir, methodsFile);
        }
        else {
            //mode is singlemethod
            methods = [];
            if (methName != undefined)
                methods.push(methName);
            else
                throw new Error("singlemethod mode specified, but no method name provided");
        }
        const result = getDocSnippetsAsJson(pkgDir, 3, methods, 10);
        if (argv.outputFile) {
            fs.writeFileSync(argv.outputFile, result);
        }
        else {
            console.log(result);
        }
    })();
}
//# sourceMappingURL=docSnippets.js.map