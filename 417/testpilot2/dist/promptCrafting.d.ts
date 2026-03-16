import { APIFunction } from "./exploreAPI";
import { TestOutcome } from "./report";
/**
 * A strategy object for refining a prompt based on the outcome of a test
 * generated from it.
 */
export interface IPromptRefiner {
    /** A human-readable name for identifying this refiner. */
    get name(): string;
    /**
     * Refine the `original` prompt based on the `outcome` of a test generated
     * from it and the given `body`.
     */
    refine(original: Prompt, body: string, outcome: TestOutcome): Prompt[];
}
/**
 * Options for controlling prompt generation.
 */
type PromptOptions = {
    /** Whether to include usage snippets in the prompt. */
    includeSnippets: boolean;
    /** Whether to include the function's doc comment in the prompt. */
    includeDocComment: boolean;
    /** Whether to include the function's body in the prompt. */
    includeFunctionBody: boolean;
    /** Template file used to generate prompts for chat model */
    templateFileName?: string;
    /** Template file used to generate prompts when errors occur */
    retryTemplateFileName?: string;
};
export declare function defaultPromptOptions(): PromptOptions;
/**
 * Structured representation of a prompt we send to the model.
 *
 * In general, our prompts look like this:
 *
 * ```js
 * let mocha = require('mocha');            // -+
 * let assert = require('assert');          //  | Imports
 * let pkg = require('pkg');                // -+
 *
 * // usage #1                              // -+
 * ...                                      //  |
 * // usage #2                              //  | Usage snippets
 * ...                                      // -+
 *
 * // this does...                          // -+
 * // @param foo                            //  |
 * // @returns bar                          //  | Doc comment
 * ...                                      // -+
 *
 * // fn(args)                              //    Signature of the function we're testing
 * // function fn(args) {                   // -+
 * //     ...                               //  | Function body (optional)
 * // }                                     // -+
 *
 * describe('test pkg', function() {        //    Test suite header
 *   it('test fn', function(done) {         //    Test case header
 * ```
 *
 * The structured representation keeps track of these parts and provides methods
 * to assemble them into a textual prompt and complete them into a test case.
 */
export declare class Prompt {
    readonly fun: APIFunction;
    readonly usageSnippets: string[];
    readonly options: PromptOptions;
    private readonly imports;
    private readonly signature;
    private readonly docComment;
    private readonly functionBody;
    private readonly suiteHeader;
    protected readonly testHeader: string;
    readonly provenance: PromptProvenance[];
    constructor(fun: APIFunction, usageSnippets: string[], options: PromptOptions);
    /**
     * Assemble the usage snippets into a single string.
     */
    private assembleUsageSnippets;
    /**
     * Assemble a prompt to send to the model from the structured
     * representation.
     */
    assemble(): string;
    /**
     * Given a test body suggested by the model, assemble a complete,
     * syntactically correct test.
     */
    completeTest(body: string, stubOutHeaders?: boolean): string | undefined;
    withProvenance(...provenanceInfos: PromptProvenance[]): Prompt;
    functionHasDocComment(): boolean;
}
/**
 * A record of how a prompt was generated, including information about which
 * `originalPrompt` it was generated from, information about the test that gave
 * rise to the prompt refinement, and the name of the refiner.
 */
export type PromptProvenance = {
    originalPrompt: Prompt;
    testId: number;
    refiner: string;
};
/**
 * A prompt refiner that adds usage snippets to the prompt.
 */
export declare class SnippetIncluder implements IPromptRefiner {
    get name(): string;
    refine(original: Prompt, completion: string, outcome: TestOutcome): Prompt[];
}
/**
 * A prompt refiner that adds a function's doc comments to the prompt.
 */
export declare class DocCommentIncluder implements IPromptRefiner {
    get name(): string;
    refine(original: Prompt, completion: string, outcome: TestOutcome): Prompt[];
}
export declare class RetryPrompt extends Prompt {
    private prev;
    private body;
    private readonly err;
    constructor(prev: Prompt, body: string, err: string);
    assemble(): string;
}
/**
 * A prompt refiner that, for a failed test, adds the error message to the
 * prompt and tries again.
 */
export declare class RetryWithError implements IPromptRefiner {
    get name(): string;
    refine(original: Prompt, completion: string, outcome: TestOutcome): Prompt[];
}
/**
 * A prompt refiner that includes the body of the function in the prompt.
 */
export declare class FunctionBodyIncluder implements IPromptRefiner {
    get name(): string;
    refine(original: Prompt, completion: string, outcome: TestOutcome): Prompt[];
}
export {};
