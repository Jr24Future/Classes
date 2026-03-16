import { ICoverageSummary } from "./coverage";
import { Prompt } from "./promptCrafting";
import { ITestInfo, TestOutcome } from "./report";
export interface IPromptInfo {
    /** The prompt. */
    prompt: Prompt;
    /** A unique ID for this prompt. */
    id: number;
    /** The file to store the prompt in. */
    file: string;
    /** The sampling temperature for this prompt. */
    temperature: number;
    /** The set of completions obtained for this prompt. */
    completions: Set<string>;
}
export interface ITestResultCollector {
    /**
     * Record information about a test for the given API function from the given
     * prompt. If the test was already recorded, the existing test info is returned,
     * with the new prompt added to the list of prompts.
     */
    recordTestInfo(testSource: string, prompt: Prompt, api: string): ITestInfo;
    /**
     * Record a test result.
     *
     * @param test the test that was run
     * @param temperature the sampling temperature used to generate the test
     * @param outcome the outcome of the test
     */
    recordTestResult(test: ITestInfo, temperature: number, outcome: TestOutcome): void;
    /**
     * Record information about a prompt.
     *
     * @param prompt the prompt
     * @param temperature the sampling temperature
     * @param completions the set of completions for the prompt
     */
    recordPromptInfo(prompt: Prompt, temperature: number, completions: Set<string>): void;
    /**
     * Record coverage information.
     *
     * @param coverageSummary the coverage information
     */
    recordCoverageInfo(coverageSummary: ICoverageSummary): void;
}
export declare class BaseTestResultCollector implements ITestResultCollector {
    protected readonly tests: Map<string, ITestInfo>;
    protected readonly prompts: Map<Prompt, IPromptInfo>;
    protected coverageSummary: ICoverageSummary;
    recordTestInfo(testSource: string, prompt: Prompt, api: string): ITestInfo;
    recordTestResult(test: ITestInfo, temperature: number, outcome: TestOutcome): void;
    recordPromptInfo(prompt: Prompt, temperature: number, completions: Set<string>): void;
    recordCoverageInfo(coverageSummary: ICoverageSummary): void;
    getPromptInfos(): IPromptInfo[];
    getTestInfos(): ITestInfo[];
}
