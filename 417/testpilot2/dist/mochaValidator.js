"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.MochaValidator = void 0;
const path_1 = __importDefault(require("path"));
const fs_1 = __importDefault(require("fs"));
const os_1 = __importDefault(require("os"));
const child_process_1 = __importDefault(require("child_process"));
const child_process_2 = require("child_process");
const testValidator_1 = require("./testValidator");
const report_1 = require("./report");
const coverage_1 = require("./coverage");
const perf_hooks_1 = require("perf_hooks");
class MochaValidator extends testValidator_1.TestValidator {
    constructor(packageName, packagePath) {
        super();
        this.packageName = packageName;
        this.packagePath = packagePath;
        this.coverageDirs = [];
        this.testDir = fs_1.default.mkdtempSync(path_1.default.join(packagePath, "test-"));
    }
    scrubTestDirFromError(error) {
        if (!error || typeof error !== "object") {
            console.warn(`Unexpected error type: ${typeof error}`);
            return error;
        }
        else if (typeof error.message !== "string") {
            console.warn(`Unexpected error.message type: ${typeof error.message}`);
            return error;
        }
        error.message = error.message.replace(new RegExp(this.testDir, "g"), "/path/to/test");
        return error;
    }
    validateTest(testName, testSource) {
        const requirePattern = new RegExp(`require\\('${this.packageName}'\\)`, "g");
        let testFile = path_1.default.join(this.testDir, testName);
        if (fs_1.default.existsSync(testFile)) {
            throw new Error(`Test file ${testFile} already exists`);
        }
        fs_1.default.writeFileSync(testFile, testSource.replace(requirePattern, `require('..')`));
        const packagePath = path_1.default.resolve(this.testDir, "..");
        // temporary directory to store output from mocha and nyc
        const tmpDir = fs_1.default.mkdtempSync(path_1.default.join(os_1.default.tmpdir(), "mocha-validator"));
        // directory to store nyc profile and coverage data
        const coverageDir = path_1.default.join(tmpDir, "coverage");
        // coverage report, produced by nyc
        const coverageReport = path_1.default.join(coverageDir, "coverage-final.json");
        // test report, produced by mocha
        const reportFile = path_1.default.join(tmpDir, "report.json");
        perf_hooks_1.performance.mark(`start:${testName}`);
let command = path_1.default.join(__dirname, "..", "node_modules", ".bin", "nyc");
if (process.platform === "win32") {
    command = path_1.default.join(__dirname, "..", "node_modules", ".bin", "nyc.cmd");
}
const args = [
            `--cwd=${packagePath}`,
            `--exclude=${path_1.default.basename(this.testDir)}`,
            "--reporter=json",
            `--report-dir=${coverageDir}`,
            `--temp-dir=${coverageDir}`,
            path_1.default.join(__dirname, "..", "node_modules", ".bin", "mocha"),
            "--full-trace",
            "--exit",
            "--allow-uncaught=false",
            "--reporter=json",
            "--reporter-option",
            `output=${reportFile}`,
            "--",
            testFile,
        ];
        const options = {
            timeout: 5000,
            killSignal: "SIGKILL",
        };
        //console.log(`Running test: ${testName}, command: ${command} ${args.join(" ")} --options=${JSON.stringify(options)}`);
        const res = (0, child_process_2.spawnSync)(command, args, options);
        perf_hooks_1.performance.measure(`duration:${testName}`, `start:${testName}`);
        let stderr = "";
        if (res && res.stderr && typeof res.stderr.toString === "function") {
        stderr = res.stderr.toString();
        } else {
        console.warn("MochaValidator: stderr is missing or not a Buffer:", res && res.error);
        }
        const report = MochaValidator.tryParseReport(reportFile);
        // parse test results; this is a bit complicated since Mocha sometimes reports asynchroneous tests
        // as both passed and failed; we want to make sure to count them as failed
        let outcome = report_1.TestOutcome.OTHER;
        if (res.status != 0 ||
            stderr.includes("AssertionError") ||
            !report ||
            report.failures.length > 0) {
            // we need to construct a ITestFailureInfo object
            // first, try to get it from the report
            if (report &&
                report.failures.length > 0 &&
                report.failures[0].err.message) {
                outcome = report_1.TestOutcome.FAILED(this.scrubTestDirFromError(report.failures[0].err));
            }
            else {
                // if that fails, try to get it from stderr
                const match = stderr.match(/(AssertionError: .*)/);
                if (match) {
                    outcome = report_1.TestOutcome.FAILED(this.scrubTestDirFromError({ message: match[1] }));
                }
                else {
                    // if that fails, just use the whole stderr or (if that's empty) the exit code
                    outcome = report_1.TestOutcome.FAILED(this.scrubTestDirFromError({
                        message: stderr !== null && stderr !== void 0 ? stderr : `Mocha exited with code ${res.status}`,
                    }));
                }
            }
        }
        else {
            // further sanity check: there should be exactly one result (either passed or pending)
            const numResults = report.passes.length + report.pending.length;
            if (numResults != 1) {
                console.log(`WARNING: Expected 1 test result, got ${numResults}`);
            }
            if (report.passes.length > 0) {
                outcome = report_1.TestOutcome.PASSED(coverageReport, coverageDir);
                this.coverageDirs.push(coverageDir);
            }
            else {
                outcome = report_1.TestOutcome.PENDING;
            }
        }
        // no need to keep coverage data for invalid tests
        if (outcome.status != "PASSED") {
            if (fs_1.default.existsSync(coverageDir)) {
                fs_1.default.rmdirSync(coverageDir, { recursive: true });
            }
        }
        return outcome;
    }
    static tryParseReport(reportFile) {
        try {
            return JSON.parse(fs_1.default.readFileSync(reportFile, "utf8"));
        }
        catch (e) {
            console.warn(`Error parsing coverage report: ${e}`);
            return undefined;
        }
    }
    computeCoverageSummary() {
        if (this.coverageDirs.length == 0) {
            return (0, coverage_1.emptyCoverageSummary)();
        }
        const testDir = fs_1.default.mkdtempSync(path_1.default.join(this.packagePath, "test-"));
        try {
            // create/clean .nyc_output directory
            const nycOutput = path_1.default.join(this.packagePath, ".nyc_output");
            if (fs_1.default.existsSync(nycOutput)) {
                fs_1.default.rmdirSync(nycOutput, { recursive: true });
            }
            fs_1.default.mkdirSync(nycOutput);
            // copy all .json files from coverageDirs to nycOutput
            for (const coverageDir of this.coverageDirs) {
                MochaValidator.copyCoverageData(coverageDir, nycOutput);
            }
            // create nyc report
            child_process_1.default.spawnSync(path_1.default.join(__dirname, "..", "node_modules", ".bin", "nyc"), [
                `--report-dir=${path_1.default.join(testDir, "coverage")}`,
                "--reporter=json-summary",
                "report",
            ], {
                cwd: this.packagePath,
                stdio: "inherit",
            });
            const coverageSummaryFileName = path_1.default.join(testDir, "coverage", "coverage-summary.json");
            if (fs_1.default.existsSync(coverageSummaryFileName)) {
                return JSON.parse(fs_1.default.readFileSync(coverageSummaryFileName, "utf8"));
            }
            else {
                throw new Error(`Failed to generate coverage summary: ${coverageSummaryFileName} does not exist.`);
            }
        }
        finally {
            fs_1.default.rmdirSync(testDir, { recursive: true });
        }
    }
    /**
     * Copy all .json files from `src` to `dest` (which must exist).
     */
    static copyCoverageData(src, dest) {
        for (const file of fs_1.default.readdirSync(src)) {
            if (file.endsWith(".json") && file !== "coverage-final.json") {
                fs_1.default.copyFileSync(path_1.default.join(src, file), path_1.default.join(dest, file));
            }
        }
    }
    cleanup() {
        for (const coverageDir of this.coverageDirs) {
            fs_1.default.rmdirSync(coverageDir, { recursive: true });
        }
    }
}
exports.MochaValidator = MochaValidator;
//# sourceMappingURL=mochaValidator.js.map