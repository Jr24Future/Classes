"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.parseReports = void 0;
const fs_1 = __importDefault(require("fs"));
const path_1 = __importDefault(require("path"));
// https://nodejs.org/api/errors.html#nodejs-error-codes
const FILE_SYS_ERRORS = ["EEXIST", "EISDIR", "ENOENT", "ENOTEMPTY", "EACCES"];
/**
 * Categorize types of failures in given tests
 * @param data report data, as found in report.json
 * @returns an object with the number of occurrences of each type of failure
 */
function getFailedStats(data) {
    const failures = data.tests
        .filter((test) => test.status === "FAILED")
        .map((test) => test.err);
    const numFailing = failures.length;
    let numAssertionErrors = 0;
    let numFileSysErrors = 0;
    //correctness errors include Type errors, Reference errors, done errors, and infinite recursion/call stack errors
    let numCorrectnessErrors = 0;
    let numTimeoutErrors = 0;
    let numOther = 0;
    for (const failure of failures) {
        if (isAssertionError(failure)) {
            numAssertionErrors++;
        }
        else if (isFileSysError(failure)) {
            numFileSysErrors++;
        }
        else if (isCorrectnessError(failure) || isSyntaxError(failure)) {
            numCorrectnessErrors++;
        }
        else if (isTimedOutTest(failure)) {
            numTimeoutErrors++;
        }
        else {
            numOther++;
        }
    }
    return {
        numFailing,
        numAssertionErrors,
        numFileSysErrors,
        numCorrectnessErrors,
        numTimeoutErrors,
        numOther,
    };
}
function isSyntaxError(err) {
    if (!err.message)
        return false;
    return err.message.includes("Invalid syntax");
}
/**
 * Checks if tests fails because of a correctness error (right now: type error, reference error, done error, infinite recursion/call stack error)
 * @param err test failure info to check
 * @returns true/false
 */
function isCorrectnessError(err) {
    if (!err.stack)
        return false;
    return (err.stack.includes("ReferenceError") ||
        err.stack.includes("TypeError") ||
        err.stack.includes("done() invoked with non-Error") ||
        err.stack.includes("Maximum call stack size exceeded"));
}
/**
 * Checks if tests fails because of an assertion error
 * @param err test failure info to check
 * @returns true/false
 */
function isAssertionError(err) {
    if (!err.stack)
        return false;
    return err.stack.includes("AssertionError");
}
/**
 * Checks if tests fails because of file system errors, as defined in FILE_SYS_ERRORS
 * @param err test failure info to check
 * @returns true/false
 */
function isFileSysError(err) {
    if (!err.code)
        return false;
    return FILE_SYS_ERRORS.includes(err.code);
}
/**
 * Checks if tests fails because of time outs
 * @param err test failure info to check
 * @returns true/false
 */
function isTimedOutTest(err) {
    if (!err.code)
        return false;
    return err.code === "ERR_MOCHA_TIMEOUT";
}
/**
 * Parse the `report.json`, `stats.json`,  and `api.json` files for all projects under the
 * given root directory and return five objects summarizing the results:
 *
 * - `coverageStats`: a mapping from project configuration (i.e., project name
 *   plus number of snippets) to an object with statistics about the project and
 *   the statement coverage our tests achieve
 * - `failureStats`: a mapping from project configuration to an object with
 *   statistics on the kinds of test failures we observe
 * - `packageStats`: a mapping from project configuration to an object with
 *   descriptive statistics of the packages
 * - `refinerStats`: a mapping from project configuration to an object with
 *   the coverage data of each refiner
 * - `performanceStats`: a mapping from project configuration to an object with
 *  performance data
 * - `similarityStats`: a mapping from project configuration to an object with
 *  similarity data (Based on edit distance)
 */
function parseReports(root, calculateUniquelyCoveringTests = false) {
    var _a, _b, _c, _d, _e, _f, _g, _h, _j, _k, _l, _m, _o, _p, _q, _r, _s, _t, _u, _v, _w, _x, _y, _z, _0, _1, _2, _3, _4, _5, _6, _7;
    const coverageStats = {};
    const failureStats = {};
    const packageStats = {};
    const refinersStats = { refinerNames: new Set(), stats: {} };
    const performanceStats = {};
    const similarityStats = {};
    for (const proj of fs_1.default.readdirSync(root)) {
        if (proj === ".DS_Store") {
            continue;
        }
        const projDir = path_1.default.join(root, proj);
        const reportFile = path_1.default.join(projDir, "report.json");
        const data = JSON.parse(fs_1.default.readFileSync(reportFile, "utf8"));
        var packageName = data.metaData.packageName;
        //special handling of gitlab-js
        if (packageName !== undefined && packageName.includes("/")) {
            const parts = packageName.split("/");
            packageName = parts[1];
        }
        const numTests = (_b = (_a = data.stats) === null || _a === void 0 ? void 0 : _a.nrTests) !== null && _b !== void 0 ? _b : 0;
        const numPassing = (_d = (_c = data.stats) === null || _c === void 0 ? void 0 : _c.nrPasses) !== null && _d !== void 0 ? _d : 0;
        const nrUniqueSnippets = (_e = data.nrUniqueSnippets) !== null && _e !== void 0 ? _e : 0;
        const stmtCoverage = (_h = (_g = (_f = data.coverage) === null || _f === void 0 ? void 0 : _f.total.statements) === null || _g === void 0 ? void 0 : _g.pct) !== null && _h !== void 0 ? _h : 0;
        const branchCoverage = (_l = (_k = (_j = data.coverage) === null || _j === void 0 ? void 0 : _j.total.branches) === null || _k === void 0 ? void 0 : _k.pct) !== null && _l !== void 0 ? _l : 0;
        const nonTrivialTests = (_o = (_m = data.stats) === null || _m === void 0 ? void 0 : _m.nrNonTrivialTests) !== null && _o !== void 0 ? _o : 0;
        const nonTrivialPassing = (_q = (_p = data.stats) === null || _p === void 0 ? void 0 : _p.nrNonTrivialPasses) !== null && _q !== void 0 ? _q : 0;
        const nonTrivialCoverage = (_t = (_s = (_r = data.coverage) === null || _r === void 0 ? void 0 : _r.total.statements) === null || _s === void 0 ? void 0 : _s.nonTrivialPct) !== null && _t !== void 0 ? _t : 0;
        const apiExplorationTime = (_v = (_u = data.stats) === null || _u === void 0 ? void 0 : _u.apiExplorationTime) !== null && _v !== void 0 ? _v : -1;
        const docCommentExtractionTime = (_x = (_w = data.stats) === null || _w === void 0 ? void 0 : _w.docCommentExtractionTime) !== null && _x !== void 0 ? _x : -1;
        const snippetExtractionTime = (_z = (_y = data.stats) === null || _y === void 0 ? void 0 : _y.snippetExtractionTime) !== null && _z !== void 0 ? _z : -1;
        const codexQueryTime = (_1 = (_0 = data.stats) === null || _0 === void 0 ? void 0 : _0.codexQueryTime) !== null && _1 !== void 0 ? _1 : -1;
        const totalTime = (_3 = (_2 = data.stats) === null || _2 === void 0 ? void 0 : _2.totalTime) !== null && _3 !== void 0 ? _3 : -1;
        var numExistingTests = -1;
        let numUniquelyCoveringTests = null;
        if (calculateUniquelyCoveringTests) {
            numUniquelyCoveringTests = getNumUniquelyCoveringTests(data.tests);
        }
        coverageStats[packageName] = {
            proj,
            nrUniqueSnippets,
            numTests,
            numPassing,
            stmtCoverage: stmtCoverage,
            branchCoverage: branchCoverage,
            nonTrivialTests,
            nonTrivialPassing,
            nonTrivialCoverage,
            numUniquelyCoveringTests,
        };
        failureStats[packageName] = { proj, ...getFailedStats(data) };
        const refinersReport = path_1.default.join(projDir, "refiners.json");
        if (fs_1.default.existsSync(refinersReport)) {
            const refinersData = JSON.parse(fs_1.default.readFileSync(refinersReport, "utf8"));
            refinersStats.stats[packageName] = { proj, refinersData };
            for (const refinerName of Object.keys(refinersData)) {
                refinersStats.refinerNames.add(refinerName);
            }
        }
        const packageStatsReport = path_1.default.join(projDir, "stats.json");
        const snippetsReport = path_1.default.join(projDir, "snippetMap.json");
        const apiReport = path_1.default.join(projDir, "api.json");
        const apiStats = getAPIStats(snippetsReport, apiReport);
        if (fs_1.default.existsSync(packageStatsReport)) {
            const packageStatsData = JSON.parse(fs_1.default.readFileSync(packageStatsReport, "utf8"));
            const weeklyDownloads = packageStatsData.weeklyDownloads;
            const stmtCoverageFromLoading = (_5 = (_4 = packageStatsData.coverageFromLoading.statements) === null || _4 === void 0 ? void 0 : _4.pct) !== null && _5 !== void 0 ? _5 : 0;
            const branchCoverageFromLoading = (_7 = (_6 = packageStatsData.coverageFromLoading.branches) === null || _6 === void 0 ? void 0 : _6.pct) !== null && _7 !== void 0 ? _7 : 0;
            const repo = packageStatsData.repository;
            const sha = packageStatsData.sha;
            const loc = packageStatsData.loc;
            packageStats[packageName] = {
                proj,
                repo,
                sha,
                loc,
                numExistingTests,
                weeklyDownloads,
                stmtCoverageFromLoading,
                branchCoverageFromLoading,
                nrUniqueSnippets,
                ...apiStats,
            };
        }
        performanceStats[packageName] = {
            proj,
            apiExplorationTime,
            docCommentExtractionTime,
            snippetExtractionTime,
            codexQueryTime,
            totalTime,
            ...apiStats,
        };
        const similarityStatsReport = path_1.default.join(projDir, "similarityReport.json");
        if (fs_1.default.existsSync(similarityStatsReport)) {
            const similarityReport = JSON.parse(fs_1.default.readFileSync(similarityStatsReport, "utf8"));
            similarityStats[packageName] = {
                proj,
                similarityReport,
            };
            packageStats[packageName].numExistingTests =
                similarityReport.numExistingTests;
        }
    }
    return {
        coverageStats,
        failureStats,
        refinersStats,
        packageStats,
        performanceStats,
        similarityStats,
    };
}
exports.parseReports = parseReports;
/***
 * Parse `api.json` and `snippetMap.json` files of a project and return an object containing the following statistics:
 * - `numFunctions`: number of functions in the project
 * - `numFunctionsWithExamples`: number of functions with at least one example
 * - `numFunctionsWithDocComments`: number of functions with doc comments
 */
function getAPIStats(snippetsReport, apiReport) {
    let numFunctions = -1;
    let numFunctionsWithExamples = -1;
    let numFunctionsWithDocComments = -1;
    if (fs_1.default.existsSync(apiReport)) {
        const apiData = JSON.parse(fs_1.default.readFileSync(apiReport, "utf8"));
        //note that it is inaccurate to base the number of functions on snippetsMap as functions with the same name get mapped to the same key,
        //leading to an underestimate of the number of functions
        numFunctions = apiData.length;
        const functionsWithDocComments = apiData.filter((f) => f.descriptor.docComment !== undefined);
        numFunctionsWithDocComments = functionsWithDocComments.length;
    }
    if (fs_1.default.existsSync(snippetsReport)) {
        const snippetsData = JSON.parse(fs_1.default.readFileSync(snippetsReport, "utf8"));
        numFunctionsWithExamples = snippetsData
            .map((entry) => entry[1])
            .filter((entry) => entry.length > 0).length;
    }
    return {
        numFunctions,
        numFunctionsWithExamples,
        numFunctionsWithDocComments,
    };
}
/**
 * Finds number of tests that cover at least one statement no other test covers
 * @param tests object containing all tests
 * @returns number of tests that cover at least one statement no other test covers
 */
function getNumUniquelyCoveringTests(tests) {
    var _a;
    const stmtCovMap = new Map(); // map from statement to list of tests covering that statement
    for (const test of tests) {
        for (const coveredStmt of (_a = test.coveredStatements) !== null && _a !== void 0 ? _a : []) {
            if (!stmtCovMap.has(coveredStmt)) {
                stmtCovMap.set(coveredStmt, []);
            }
            stmtCovMap.get(coveredStmt).push(test.testName);
        }
    }
    let numUniquelyCoveredStmts = 0;
    const uniquelyCoveringTests = new Set();
    for (const coveringTests of stmtCovMap.values()) {
        if (coveringTests.length == 1) {
            numUniquelyCoveredStmts++;
            uniquelyCoveringTests.add(coveringTests[0]);
        }
    }
    return uniquelyCoveringTests.size;
}
//# sourceMappingURL=parse_reports.js.map