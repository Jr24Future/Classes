import { ICompletionModel } from "./completionModel";
declare const defaultPostOptions: {
    max_tokens: number;
    temperature: number;
    n: number;
    top_p: number;
};
export type PostOptions = Partial<typeof defaultPostOptions>;
export declare class Codex implements ICompletionModel {
    private readonly isStarCoder;
    private readonly instanceOptions;
    private readonly apiEndpoint;
    private readonly authHeaders;
    constructor(isStarCoder: boolean, instanceOptions?: PostOptions);
    /**
     * Query Codex for completions with a given prompt.
     *
     * @param prompt The prompt to use for the completion.
     * @param requestPostOptions The options to use for the request.
     * @returns A promise that resolves to a set of completions.
     */
    query(prompt: string, requestPostOptions?: PostOptions): Promise<Set<string>>;
    /**
     * Get completions from Codex and postprocess them as needed; print a warning if it did not produce any
     *
     * @param prompt the prompt to use
     */
    completions(prompt: string, temperature: number): Promise<Set<string>>;
}
export {};
