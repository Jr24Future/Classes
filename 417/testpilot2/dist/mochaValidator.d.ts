import { TestValidator } from "./testValidator";
import { TestOutcome } from "./report";
import { ICoverageSummary } from "./coverage";
export declare class MochaValidator extends TestValidator {
    private packageName;
    private packagePath;
    private readonly testDir;
    private readonly coverageDirs;
    constructor(packageName: string, packagePath: string);
    private scrubTestDirFromError;
    validateTest(testName: string, testSource: string): TestOutcome;
    private static tryParseReport;
    computeCoverageSummary(): ICoverageSummary;
    /**
     * Copy all .json files from `src` to `dest` (which must exist).
     */
    static copyCoverageData(src: string, dest: string): void;
    cleanup(): void;
}
