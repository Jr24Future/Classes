"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.getCoveredStmtsForFile = exports.getFileStmts = exports.createUniqueStmtId = void 0;
/***
 * Create a unique statement id from path and start/end location for a given statement
 */
function createUniqueStmtId(relpath, startLine, startColumn, endLine, endColumn) {
    return `${relpath}@${startLine}:${startColumn}-${endLine}:${endColumn}`;
}
exports.createUniqueStmtId = createUniqueStmtId;
/**
 * Get a map from statement index to unique statement id for a given file in the coverage report
 * @param recordedStmtMap: the statement map recorded in the coverage report
 * @param fileRelPath: the relative path of the file in the coverage report
 * @returns a map from statement index to unique statement id (in same format as createUniqueStmtId)
 */
function getFileStmts(recordedStmtMap, fileRelPath) {
    const statementMap = new Map();
    for (const key of Object.keys(recordedStmtMap)) {
        const { start: { line: startLine, column: startColumn }, end: { line: endLine, column: endColumn }, } = recordedStmtMap[key];
        const statementId = createUniqueStmtId(fileRelPath, startLine, startColumn, endLine, endColumn);
        statementMap.set(key, statementId);
    }
    return statementMap;
}
exports.getFileStmts = getFileStmts;
/**
 * Get the list of statements covered from a given file in the coverage report
 * @param fileCoverage: the coverage report for a given file
 * @param relpath: the relative path of the file in the coverage report
 * @returns a list of covered statements (in same format as createUniqueStmtId)
 */
function getCoveredStmtsForFile(fileCoverage, relpath) {
    const statementMap = getFileStmts(fileCoverage.statementMap, relpath);
    const coveredStmtIds = [];
    for (const stmtIndx of Object.keys(fileCoverage.s)) {
        const isCovered = fileCoverage.s[stmtIndx];
        if (isCovered) {
            coveredStmtIds.push(statementMap.get(stmtIndx));
        }
    }
    return coveredStmtIds;
}
exports.getCoveredStmtsForFile = getCoveredStmtsForFile;
//# sourceMappingURL=testCollectorHelper.js.map