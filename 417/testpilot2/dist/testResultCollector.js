"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.BaseTestResultCollector = void 0;
const coverage_1 = require("./coverage");
const report_1 = require("./report");
class BaseTestResultCollector {
    constructor() {
        this.tests = new Map();
        this.prompts = new Map();
        this.coverageSummary = (0, coverage_1.emptyCoverageSummary)();
    }
    recordTestInfo(testSource, prompt, api) {
        let testInfo = this.tests.get(testSource);
        if (testInfo) {
            testInfo.prompts.push(prompt);
        }
        else {
            const id = this.tests.size;
            testInfo = {
                id,
                testName: `test_${id}.js`,
                outcome: report_1.TestOutcome.OTHER,
                testSource: testSource,
                prompts: [prompt],
                api,
            };
            this.tests.set(testSource, testInfo);
        }
        return testInfo;
    }
    recordTestResult(test, temperature, outcome) {
        test.outcome = outcome;
    }
    recordPromptInfo(prompt, temperature, completions) {
        const id = this.prompts.size;
        const file = `prompt_${id}.js`;
        this.prompts.set(prompt, { prompt, id, file, temperature, completions });
    }
    recordCoverageInfo(coverageSummary) {
        this.coverageSummary = coverageSummary;
    }
    getPromptInfos() {
        return Array.from(this.prompts.values());
    }
    getTestInfos() {
        return Array.from(this.tests.values());
    }
}
exports.BaseTestResultCollector = BaseTestResultCollector;
//# sourceMappingURL=testResultCollector.js.map