"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const fs_1 = __importDefault(require("fs"));
const path_1 = __importDefault(require("path"));
function formatNum(numerator, denominator) {
    if (denominator == 0)
        return "--";
    return `${numerator} (${((numerator / denominator) * 100).toFixed(0)} %)`;
}
function parseReports(root) {
    var _a, _b, _c, _d, _e, _f, _g, _h, _j;
    const coverageStats = {};
    for (const proj of fs_1.default.readdirSync(root)) {
        const projDir = path_1.default.join(root, proj);
        if (!fs_1.default.lstatSync(projDir).isDirectory())
            continue;
        const stmtCovMap = new Map(); // map from statement to list of tests covering that statement
        const reportData = JSON.parse(fs_1.default.readFileSync(path_1.default.join(projDir, "report.json"), "utf8"));
        const packageName = reportData.metaData.packageName;
        const numCoveredStmts = (_c = (_b = (_a = reportData.coverage) === null || _a === void 0 ? void 0 : _a.total.statements) === null || _b === void 0 ? void 0 : _b.covered) !== null && _c !== void 0 ? _c : 0;
        const coverage = (_f = (_e = (_d = reportData.coverage) === null || _d === void 0 ? void 0 : _d.total.statements) === null || _e === void 0 ? void 0 : _e.pct) !== null && _f !== void 0 ? _f : 0;
        const numPassing = (_h = (_g = reportData.stats) === null || _g === void 0 ? void 0 : _g.nrPasses) !== null && _h !== void 0 ? _h : 0;
        for (const test of reportData.tests) {
            for (const coveredStmt of (_j = test.coveredStatements) !== null && _j !== void 0 ? _j : []) {
                if (!stmtCovMap.has(coveredStmt)) {
                    stmtCovMap.set(coveredStmt, []);
                }
                stmtCovMap.get(coveredStmt).push(test.testName);
            }
        }
        coverageStats[packageName] = {
            proj,
            numPassing,
            coverage,
            numCoveredStmts,
            stmtCovMap,
        };
    }
    return coverageStats;
}
function printTestDiversityReport(title, coverageStats) {
    console.log(`
# ${title}

Project| # Passing Tests| Coverage | # Covered Stmts | Avg. num tests/stmt | # Uniquely Covered Stmts | # Uniquely Covering Tests
--- | ---: | ---: | ---: | ---: | ---: | ---:`);
    for (const { proj, numPassing, coverage, numCoveredStmts, stmtCovMap, } of Object.values(coverageStats)) {
        const coveringTestPerStmt = Array.from(stmtCovMap.values());
        const averageTestsPerStmt = (coveringTestPerStmt
            .map((coveringTests) => coveringTests.length)
            .reduce((a, b) => a + b, 0) / coveringTestPerStmt.length).toFixed(2);
        let numUniquelyCoveredStmts = 0;
        const uniquelyCoveringTests = new Set();
        for (const coveringTests of stmtCovMap.values()) {
            if (coveringTests.length == 1) {
                numUniquelyCoveredStmts++;
                uniquelyCoveringTests.add(coveringTests[0]);
            }
        }
        const numUniquelyCoveringTests = formatNum(uniquelyCoveringTests.size, numPassing);
        console.log(`${proj}| ${numPassing} | ${coverage}% | ${numCoveredStmts} | ${averageTestsPerStmt} |  ${numUniquelyCoveredStmts} | ${numUniquelyCoveringTests}`);
    }
    console.log(`Interpreting table:
  - First three columns are the same as the typical table we output
  - \# Covered stmts: the number of statements covered by the passing tests, from the report.json file
  - Avg num tests/stmt: for each covered statement, we find the tests that cover this statement and then calculate the average num of tests/stmt
  - \# Uniquely Covered Stmts: these are statements covered by only one test
  - \# Uniquely Covering Tests: number of tests that uniquely cover at least one statement (and percentage w.r.t number of passing tests; the higher the percentage the better although 100% is unlikely)
   `);
}
if (require.main === module) {
    if (process.argv.length != 3) {
        console.error("Usage: node generate_diversity_report.js <artifact_dir>");
        process.exit(1);
    }
    const artifactDir = process.argv[2];
    let coverageStats = parseReports(artifactDir);
    printTestDiversityReport("Diversity of Tests w.r.t Stmt Coverage", coverageStats);
}
//# sourceMappingURL=generate_diversity_report.js.map