"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.TestGenerator = void 0;
const promptCrafting_1 = require("./promptCrafting");
const report_1 = require("./report");
/**
 * Context class collecting various bits of information needed for test
 * generation.
 */
class TestGenerator {
    constructor(temperatures, snippetMap, model, templateFileName, retryTemplateFileName, validator, collector) {
        this.temperatures = temperatures;
        this.snippetMap = snippetMap;
        this.model = model;
        this.templateFileName = templateFileName;
        this.retryTemplateFileName = retryTemplateFileName;
        this.validator = validator;
        this.collector = collector;
        this.refiners = [
            new promptCrafting_1.SnippetIncluder(),
            new promptCrafting_1.RetryWithError(),
            new promptCrafting_1.DocCommentIncluder(),
            new promptCrafting_1.FunctionBodyIncluder(),
        ];
    }
    /**
     * Generate tests for a given function and validate them.
     */
    async generateAndValidateTests(fun) {
        var _a;
        for (const temperature of this.temperatures) {
            let generatedPassingTests = false;
            const generatedPrompts = new Map();
            const snippets = (_a = this.snippetMap(fun.functionName)) !== null && _a !== void 0 ? _a : [];
            const promptOptions = {
                ...(0, promptCrafting_1.defaultPromptOptions)(),
                templateFileName: this.templateFileName,
                retryTemplateFileName: this.retryTemplateFileName,
            };
            const worklist = [new promptCrafting_1.Prompt(fun, snippets, promptOptions)];
            while (worklist.length > 0) {
                const prompt = worklist.pop();
                // check whether we've generated this prompt before; if so, record that
                // fact by updating provenance info and skip it
                const assembledPrompt = prompt.assemble();
                const previousPrompt = generatedPrompts.get(assembledPrompt);
                if (previousPrompt) {
                    previousPrompt.withProvenance(...prompt.provenance);
                    continue;
                }
                generatedPrompts.set(assembledPrompt, prompt);
                const completions = await this.model.completions(assembledPrompt, temperature);
                for (const completion of completions) {
                    const tests = extractTestFromCompletion(completion);
                    if (tests.size > 0) {
                        for (const test of tests) {
                            const testInfo = this.validateCompletion(prompt, test, temperature);
                            if (testInfo.outcome.status === report_1.TestStatus.PASSED) {
                                generatedPassingTests = true;
                            }
                            this.refinePrompts(prompt, test, testInfo, worklist);
                            if (generatedPassingTests)
                                break;
                        }
                    }
                }
                this.collector.recordPromptInfo(prompt, temperature, completions);
            }
        }
    }
    /**
     * Build a test for the given prompt and completion, validate it, and return
     * a test info object.
     */
    validateCompletion(prompt, completion, temperature) {
        let testSource = prompt.completeTest(completion);
        const testInfo = this.collector.recordTestInfo(testSource !== null && testSource !== void 0 ? testSource : completion, prompt, prompt.fun.accessPath);
        if (testInfo.prompts.length > 1) {
            // we have already validated this test
            return testInfo;
        }
        let outcome;
        if (completion === "") {
            outcome = report_1.TestOutcome.FAILED({ message: "Empty test" });
        }
        else if (testSource) {
            outcome = this.validator.validateTest(testInfo.testName, testInfo.testSource);
        }
        else {
            outcome = report_1.TestOutcome.FAILED({ message: "Invalid syntax" });
        }
        this.collector.recordTestResult(testInfo, temperature, outcome);
        return testInfo;
    }
    /**
     * Refine the prompt based on the test outcome, and add the refined prompts
     * to the worklist.
     */
    refinePrompts(prompt, completion, testInfo, worklist) {
        for (const refiner of this.refiners) {
            for (const refinedPrompt of refiner.refine(prompt, completion, testInfo.outcome)) {
                const provenance = {
                    originalPrompt: prompt,
                    testId: testInfo.id,
                    refiner: refiner.name,
                };
                worklist.push(refinedPrompt.withProvenance(provenance));
            }
        }
    }
}
exports.TestGenerator = TestGenerator;
function extractTestFromCompletion(rawCompletion) {
    const regExp = /```.*?\n(.*?)\n```/gs;
    let match;
    while ((match = regExp.exec(rawCompletion)) !== null) {
        const code = match[1];
        const set = new Set();
        if (code.split("it(").length === 2) {
            set.add(code);
            return set;
        }
        else {
            // we received a suite with more than one test, turn this into multiple suites each containing one test
            const indexOfSuite = code.indexOf("describe(");
            const indexOfFirstTest = code.indexOf("it(");
            let testIndex = indexOfFirstTest;
            while (code.indexOf("it(", testIndex + 1) !== -1) {
                // while there is another test
                const nextTestIndex = code.indexOf("it(", testIndex + 1);
                const test = code.substring(testIndex, nextTestIndex);
                set.add(test);
                testIndex = nextTestIndex;
            }
            // add the last test
            const lastTest = code.substring(testIndex);
            set.add(lastTest);
            const preSuite = code.substring(0, indexOfSuite);
            const suiteHeader = code.substring(indexOfSuite, indexOfFirstTest);
            const result = new Set([...set].map((test) => {
                return preSuite + suiteHeader + test;
            }));
            return result;
        }
    }
    // if we're unable to extract something, return a set containing the raw completion
    // even though it's unlikely to validate
    const set = new Set();
    set.add(rawCompletion);
    return set;
}
//# sourceMappingURL=generateTests.js.map