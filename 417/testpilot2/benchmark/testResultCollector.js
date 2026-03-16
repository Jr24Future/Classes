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
Object.defineProperty(exports, "__esModule", { value: true });
exports.TestResultCollector = void 0;
const fs = __importStar(require("fs"));
const path = __importStar(require("path"));
const __1 = require("..");
const testCollectorHelper_1 = require("./testCollectorHelper");
/**
 * A full-featured test-result collector that can be used to persist information
 * to disk.
 */
class TestResultCollector extends __1.BaseTestResultCollector {
    /**
     * constructor registers meta-data associated with a test run
     *
     * @param outputDir: the directory in which to write the report and other files
     * @param snippetsTypeAsString: the type of snippets used to generate the tests (code, doc, both, or none)
     * @param numSnippets: number of snippets to include in a prompt (default 3)
     * @param snippetLength: length of each snippet (maximum length of each snippet in lines (default 20 lines))
     * @param temperature: sampling temperature for obtaining completions (default 0)
     * @param numCompletions: number of completions to obtain for each prompt (default 5)
     */
    constructor(packageName, packagePath, outputDir, api, snippetMap, perf, snippetsTypeAsString, numSnippets, snippetLength, numCompletions) {
        super();
        this.packagePath = packagePath;
        this.outputDir = outputDir;
        this.api = api;
        this.snippetMap = snippetMap;
        this.perf = perf;
        this.metaData = {
            packageName,
            useDocSnippets: snippetsTypeAsString === "doc" || snippetsTypeAsString === "both",
            useCodeSnippets: snippetsTypeAsString === "code" || snippetsTypeAsString === "both",
            numSnippets,
            snippetLength,
            numCompletions,
        };
        this.createOutputDir();
    }
    getTestsWithStatus(status) {
        return [...this.tests.values()].filter((test) => test.outcome.status === status);
    }
    getNrPasses() {
        return this.getTestsWithStatus(__1.TestStatus.PASSED).length;
    }
    getNrFailures() {
        return this.getTestsWithStatus(__1.TestStatus.FAILED).length;
    }
    getNrPending() {
        return this.getTestsWithStatus(__1.TestStatus.PENDING).length;
    }
    getNrOther() {
        return this.getTestsWithStatus(__1.TestStatus.OTHER).length;
    }
    getReport() {
        return {
            metaData: this.metaData,
            nrUniqueSnippets: this.computeNrUniqueSnippets(),
            stats: {
                nrTests: this.tests.size,
                nrPasses: this.getNrPasses(),
                nrFailures: this.getNrFailures(),
                nrPending: this.getNrPending(),
                nrOther: this.getNrOther(),
                apiExplorationTime: this.perf.getApiExplorationTime(),
                docCommentExtractionTime: this.perf.getDocCommentExtractionTime(),
                snippetExtractionTime: this.perf.getSnippetExtractionTime(),
                codexQueryTime: this.perf.getTotalCodexQueryTime(),
                totalTime: this.perf.getTotalTime(),
            },
            tests: [...this.tests.values()].map(this.getReportForTest, this),
            coverage: this.coverageSummary,
        };
    }
    getReportForTest(test) {
        const promptIds = test.prompts.map((prompt) => this.prompts.get(prompt).id);
        const err = test.outcome.status === __1.TestStatus.FAILED ? test.outcome.err : {};
        const coveredStatements = this.getCoveredStatements(test.outcome);
        return {
            testName: test.testName,
            api: test.api,
            testFile: test.testName,
            promptIds: promptIds,
            status: test.outcome.status,
            err: err,
            coveredStatements: coveredStatements,
            duration: this.perf.getTestDuration(test.testName),
        };
    }
    /**
     * Get the list of statements covered by the test with the given outcome.
     *
     * Tests that do not pass or that do not have a coverage summary are not
     * considered to cover any statements. For passing tests, covered statements are
     * represented in the form
     * '<file>@<startLine>:<startColumn>-<endLine>:<endColumn>'.
     */
    getCoveredStatements(outcome) {
        if (outcome.status !== __1.TestStatus.PASSED ||
            outcome.coverageReport === undefined) {
            return [];
        }
        const coveredStatements = [];
        const coverage = JSON.parse(fs.readFileSync(outcome.coverageReport, "utf8"));
        for (const file of Object.keys(coverage)) {
            const relpath = path.relative(this.packagePath, coverage[file].path);
            coveredStatements.push(...(0, testCollectorHelper_1.getCoveredStmtsForFile)(coverage[file], relpath));
        }
        return coveredStatements;
    }
    /**
     * compute the number of unique snippets that are available in the snippet map
     * @returns the number of unique snippets
     */
    computeNrUniqueSnippets() {
        const uniqueSnippets = new Set();
        for (const snippetGroup of this.snippetMap.values()) {
            for (const snippet of snippetGroup) {
                uniqueSnippets.add(snippet);
            }
        }
        return uniqueSnippets.size;
    }
    /**
     * For passing tests, preprend a checkmark and make the text green.
     * For failing tests, prepend an 'x' and make the text red.
     * For other tests, prepend a '?' and make the text purple.
     */
    getTestLabel(test) {
        const testName = test.testName;
        if (test.outcome.status === __1.TestStatus.PASSED) {
            return "\u001b[32m" + "\u2713" + testName + "\u001b[0m";
        }
        else if (test.outcome.status === __1.TestStatus.FAILED) {
            return "\u001b[31m" + "\u2717" + testName + "\u001b[0m";
        }
        else {
            return "\u001b[35m" + "\u2753" + testName + "\u001b[0m";
        }
    }
    /**
     * print summary of test results for each API method
     */
    reportAPICoverage() {
        console.log("API coverage:");
        const testsPerAPI = new Map();
        for (const test of this.tests.values()) {
            const api = test.api;
            if (!testsPerAPI.has(api)) {
                testsPerAPI.set(api, new Set());
            }
            testsPerAPI.get(api).add(test);
        }
        for (const [api, tests] of testsPerAPI.entries()) {
            const testLabels = [...tests].map((test) => this.getTestLabel(test));
            console.log(`  ${api}: ${[...testLabels.values()].join(", ")}`);
        }
    }
    report() {
        // write report to 'report.json' in the specified output directory
        const report = this.getReport();
        fs.writeFileSync(path.join(this.outputDir, "report.json"), JSON.stringify(report, null, 2));
        // write out tests to 'tests' directory
        const testOutputDir = path.join(this.outputDir, "tests");
        const coverageDataDir = path.join(this.outputDir, "coverageData");
        for (const { testName, testSource, outcome } of this.tests.values()) {
            fs.writeFileSync(path.join(testOutputDir, testName), testSource);
            // copy coverage data if available
            if (outcome.status === "PASSED" && outcome.coverageData) {
                const destDir = path.join(coverageDataDir, path.basename(testName, ".js"));
                fs.mkdirSync(destDir, { recursive: true });
                __1.MochaValidator.copyCoverageData(outcome.coverageData, destDir);
            }
        }
        // write out prompts to 'prompts' directory, and summary of prompts to 'prompts.json'
        const promptOutputDir = path.join(this.outputDir, "prompts");
        for (const promptInfo of this.prompts.values()) {
            fs.writeFileSync(path.join(promptOutputDir, promptInfo.file), promptInfo.prompt.assemble());
        }
        let prompts = {
            metaData: this.metaData,
            prompts: [...this.prompts.values()].map(({ prompt, id, file, temperature, completions }) => {
                const tests = [...this.tests.values()]
                    .filter((test) => test.prompts.includes(prompt))
                    .map((test) => test.testName);
                const provenance = prompt.provenance.map((p) => ({
                    originalPrompt: this.prompts.get(p.originalPrompt).id,
                    test: p.testId,
                    refiner: p.refiner,
                }));
                return {
                    id,
                    file,
                    temperature,
                    completions: [...completions.values()],
                    tests,
                    provenance,
                };
            }),
        };
        fs.writeFileSync(path.join(this.outputDir, "prompts.json"), JSON.stringify(prompts, null, 2));
        // write API info to 'api.json'
        fs.writeFileSync(path.join(this.outputDir, "api.json"), JSON.stringify(this.api, null, 2));
        // write snippetMap to 'snippetMap.json'
        fs.writeFileSync(path.join(this.outputDir, "snippetMap.json"), JSON.stringify([...this.snippetMap], null, 2));
        // write Codex query times to 'codexQueryTimes.json'
        fs.writeFileSync(path.join(this.outputDir, "codexQueryTimes.json"), JSON.stringify(this.perf.getCodexQueryTimes(), null, 2));
        // print summary statistics
        console.log(`${this.getNrPasses()} passed, ${this.getNrFailures()} failed, ${this.getNrPending()} pending, ${this.getNrOther()} other`);
        // print API coverage
        this.reportAPICoverage();
    }
    /**
     * Create directory for output files if it does not exist. If it does exist, delete it and its contents and create a new one.
     */
    createOutputDir() {
        if (fs.existsSync(this.outputDir)) {
            fs.rmdirSync(this.outputDir, { recursive: true });
        }
        fs.mkdirSync(this.outputDir, { recursive: true });
        fs.mkdirSync(path.join(this.outputDir, "tests"));
        fs.mkdirSync(path.join(this.outputDir, "prompts"));
        fs.mkdirSync(path.join(this.outputDir, "coverageData"));
    }
    recordTestResult(test, temperature, outcome) {
        super.recordTestResult(test, temperature, outcome);
        console.log(`${test.testName} (for ${test.api} at temperature ${temperature}, ${test.prompts[0].usageSnippets.length} snippets available): ${outcome.status}`);
    }
}
exports.TestResultCollector = TestResultCollector;
//# sourceMappingURL=testResultCollector.js.map