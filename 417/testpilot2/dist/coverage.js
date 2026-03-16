"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.emptyCoverageSummary = void 0;
/**
 * An empty coverage summary object
 */
function emptyCoverageSummary() {
    return {
        total: {
            lines: { total: 0, covered: 0, skipped: 0, pct: 0 },
            statements: { total: 0, covered: 0, skipped: 0, pct: 0 },
            functions: { total: 0, covered: 0, skipped: 0, pct: 0 },
            branches: { total: 0, covered: 0, skipped: 0, pct: 0 },
            branchesTrue: { total: 0, covered: 0, skipped: 0, pct: 0 },
        },
    };
}
exports.emptyCoverageSummary = emptyCoverageSummary;
//# sourceMappingURL=coverage.js.map