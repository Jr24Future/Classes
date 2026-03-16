"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.TestOutcome = exports.TestStatus = void 0;
var TestStatus;
(function (TestStatus) {
    TestStatus["PASSED"] = "PASSED";
    TestStatus["FAILED"] = "FAILED";
    TestStatus["PENDING"] = "PENDING";
    TestStatus["OTHER"] = "OTHER";
})(TestStatus = exports.TestStatus || (exports.TestStatus = {}));
var TestOutcome;
(function (TestOutcome) {
    function PASSED(coverageReport, coverageData) {
        return { status: "PASSED", coverageReport, coverageData };
    }
    TestOutcome.PASSED = PASSED;
    TestOutcome.PENDING = { status: "PENDING" };
    TestOutcome.OTHER = { status: "OTHER" };
    function FAILED(err) {
        return { status: "FAILED", err };
    }
    TestOutcome.FAILED = FAILED;
})(TestOutcome = exports.TestOutcome || (exports.TestOutcome = {}));
//# sourceMappingURL=report.js.map