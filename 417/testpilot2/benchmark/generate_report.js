"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const fs_1 = __importDefault(require("fs"));
const parse_reports_1 = require("./parse_reports");
function percentage(p) {
    if (typeof p === "number") {
        return `${p.toFixed(2)}%`;
    }
    else {
        return p;
    }
}
function printCoverageReport(title, stats) {
    console.log(`
# ${title}
Project | # Snippets Available | # Tests | # Passing Tests |  Statement coverage | Branch coverage | # Non-trivial tests | # Non-trivial passing tests | Statement coverage by non-trivial tests
--- |  --: | --: | --: | --: | --: | --: | --: | --:`);
    for (const { proj, nrUniqueSnippets, numTests, numPassing, stmtCoverage, branchCoverage, nonTrivialTests, nonTrivialPassing, nonTrivialCoverage, } of Object.values(stats)) {
        console.log(`${proj} | ${nrUniqueSnippets} | ${numTests} | ${numPassing} | ${percentage(stmtCoverage)} | ${percentage(branchCoverage)} | ${nonTrivialTests} | ${nonTrivialPassing} | ${percentage(nonTrivialCoverage)}`);
    }
}
function printFailureReport(title, stats, showPercentages = true) {
    console.log(`
# ${title}
Project | # FailedTests | # AssertionErrors | # FileSysErrors | # CorrectnessErrors |  # Timeout | # Other
--- |  --: | --: | --: | --: | --: | --:|`);
    for (const { proj, numFailing, numAssertionErrors, numFileSysErrors, numCorrectnessErrors, numTimeoutErrors, numOther, } of Object.values(stats)) {
        console.log(`${proj} | ${numFailing} | ${formatNum(numAssertionErrors, numFailing, showPercentages)} | ${formatNum(numFileSysErrors, numFailing, showPercentages)} | ${formatNum(numCorrectnessErrors, numFailing, showPercentages)} | ${formatNum(numTimeoutErrors, numFailing, showPercentages)} | ${formatNum(numOther, numFailing, showPercentages)}`);
    }
}
function printRefinerReport(title, stats) {
    const refinerNames = Array.from(stats.refinerNames).sort();
    console.log(`
# ${title}
Project | ${refinerNames.join(" | ")}
--- | ${"--: |".repeat(refinerNames.length)}`);
    for (const { proj, refinersData } of Object.values(stats.stats)) {
        if (!proj)
            continue;
        console.log(`${proj} | ${refinerNames
            .map((name) => name in refinersData ? refinersData[name].coverage + "%" : "--")
            .join(" | ")}`);
    }
}
function printSimilarityReport(title, stats) {
    console.log(`
# ${title}
Project | numGeneratedTests | numExistingTests | maxSimilarity
--- |  --: | --: | --:`);
    for (const { proj, similarityReport } of Object.values(stats)) {
        console.log(`${proj} | ${similarityReport.numGeneratedTests} | ${similarityReport.numExistingTests} | ${similarityReport.maxSimilarity}`);
    }
}
function formatNum(number, denominator, showPercentages = true) {
    if (denominator == 0)
        return "--";
    if (showPercentages)
        return `${number} (${((number / denominator) * 100).toFixed(2)}%)`;
    else
        return `${number}`;
}
function coverageDiff(cov1, cov2) {
    if (cov1 === "unknown" || cov2 === "unknown") {
        return "unknown";
    }
    else {
        return (cov1 - cov2).toFixed(2);
    }
}
function compareCovToBaseline(baselineCovStats) {
    const diffStats = {};
    for (const [packageName, projStats] of Object.entries(coverageStats)) {
        const baseline = baselineCovStats[packageName];
        // print diff if the same config is in the baseline, otherwise, skip diff for this config
        if (baseline) {
            const nonTrivialTestDiff = projStats.nonTrivialTests - baseline.nonTrivialTests;
            const nonTrivialPassingDiff = projStats.nonTrivialPassing - baseline.nonTrivialPassing;
            diffStats[packageName] = {
                proj: projStats.proj,
                nrUniqueSnippets: ppDiff(projStats.nrUniqueSnippets - baseline.nrUniqueSnippets),
                numTests: ppDiff(projStats.numTests - baseline.numTests),
                numPassing: ppDiff(projStats.numPassing - baseline.numPassing),
                coverage: ppDiff(coverageDiff(projStats.stmtCoverage, baseline.stmtCoverage)),
                nonTrivialTests: ppDiff(nonTrivialTestDiff),
                nonTrivialPassing: ppDiff(nonTrivialPassingDiff),
                nonTrivialCoverage: ppDiff(coverageDiff(projStats.nonTrivialCoverage, baseline.nonTrivialCoverage)),
            };
        }
    }
    printCoverageReport("Coverage Comparison to baseline", diffStats);
}
function compareFailuresToBaseline(baselineFailureStats) {
    const diffStats = {};
    for (const [packageName, projStats] of Object.entries(failureStats)) {
        const baseline = baselineFailureStats[packageName];
        //print diff if the same config is in the baseline, otherwise, skip diff for this config
        if (baseline) {
            diffStats[packageName] = {
                proj: projStats.proj,
                numFailing: ppDiff(projStats.numFailing - baseline.numFailing, true),
                numAssertionErrors: ppDiff(projStats.numAssertionErrors - baseline.numAssertionErrors),
                numFileSysErrors: ppDiff(projStats.numFileSysErrors - baseline.numFileSysErrors),
                numCorrectnessErrors: ppDiff(projStats.numCorrectnessErrors - baseline.numCorrectnessErrors),
                numTimeoutErrors: ppDiff(projStats.numTimeoutErrors - baseline.numTimeoutErrors),
                numOther: ppDiff(projStats.numOther - baseline.numOther),
            };
        }
    }
    printFailureReport("Failure Comparison to baseline", diffStats, false);
}
function ppDiff(d, lowerIsBetter = false) {
    let s;
    if (d > 0) {
        s = `+${d}`;
    }
    else if (d == 0) {
        s = "±0";
    }
    else {
        s = String(d);
    }
    if (lowerIsBetter ? d < 0 : d > 0) {
        return `**${s}**`;
    }
    else {
        return s;
    }
}
if (process.argv.length < 4 || process.argv.length > 6) {
    console.error("Usage: node generate_report.js model [<config.json>] <artifact_dir> [<baseline_artifact_dir>]");
    process.exit(1);
}
const model = process.argv[2];
const hasConfig = fs_1.default.lstatSync(process.argv[3]).isFile();
const config = hasConfig
    ? JSON.parse(fs_1.default.readFileSync(process.argv[3], "utf8"))
    : {};
const artifactDir = hasConfig ? process.argv[4] : process.argv[2];
const baselineArtifactDir = hasConfig ? process.argv[5] : process.argv[4];
console.log(`
# Parameters
- model: ${model}
- temperatures: ${config.temperatures}
- snippets from: ${config.snippetsFrom}
- snippet length: ${config.snippetLength}
- numSnippets: ${config.numSnippets}`);
const { coverageStats, failureStats, refinersStats, similarityStats } = (0, parse_reports_1.parseReports)(artifactDir);
printCoverageReport("Coverage report", coverageStats);
printFailureReport("Failure report", failureStats);
// printRefinerReport("Coverage when excluding refiners", refinersStats);
printSimilarityReport("Similarity of generated tests to existing tests", similarityStats);
if (baselineArtifactDir) {
    const baselineResults = (0, parse_reports_1.parseReports)(baselineArtifactDir);
    const baselineCovStats = baselineResults.coverageStats;
    const baselineFailureStats = baselineResults.failureStats;
    compareCovToBaseline(baselineCovStats);
    compareFailuresToBaseline(baselineFailureStats);
}
//# sourceMappingURL=generate_report.js.map