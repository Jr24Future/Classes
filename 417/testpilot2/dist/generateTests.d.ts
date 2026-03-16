import { ICompletionModel } from "./completionModel";
import { APIFunction } from "./exploreAPI";
import { Prompt } from "./promptCrafting";
import { ITestInfo } from "./report";
import { SnippetMap } from "./snippetHelper";
import { ITestResultCollector } from "./testResultCollector";
import { TestValidator } from "./testValidator";
/**
 * Context class collecting various bits of information needed for test
 * generation.
 */
export declare class TestGenerator {
    private temperatures;
    private snippetMap;
    private model;
    private templateFileName;
    private retryTemplateFileName;
    private validator;
    private collector;
    private refiners;
    constructor(temperatures: number[], snippetMap: SnippetMap, model: ICompletionModel, templateFileName: string, retryTemplateFileName: string, validator: TestValidator, collector: ITestResultCollector);
    /**
     * Generate tests for a given function and validate them.
     */
    generateAndValidateTests(fun: APIFunction): Promise<void>;
    /**
     * Build a test for the given prompt and completion, validate it, and return
     * a test info object.
     */
    validateCompletion(prompt: Prompt, completion: string, temperature: number): ITestInfo;
    /**
     * Refine the prompt based on the test outcome, and add the refined prompts
     * to the worklist.
     */
    private refinePrompts;
}
