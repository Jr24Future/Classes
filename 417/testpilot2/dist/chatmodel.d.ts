import { ICompletionModel } from "./completionModel";
import { IRateLimiter } from "./promise-utils";
declare const defaultPostOptions: {
    max_tokens: number;
    temperature: number;
    top_p: number;
};
export type PostOptions = Partial<typeof defaultPostOptions>;
/**
 * A model that uses the ChatModel API to provide completions.
 */
export declare class ChatModel implements ICompletionModel {
    private readonly model;
    private readonly nrAttempts;
    private readonly rateLimiter;
    private readonly instanceOptions;
    private readonly apiEndpoint;
    private readonly authHeaders;
    constructor(model: string, nrAttempts: number, rateLimiter: IRateLimiter, instanceOptions?: PostOptions);
    /**
     * Query the ChatModel for completions with a given prompt.
     *
     * @param prompt The prompt to use for the completion.
     * @param requestPostOptions The options to use for the request.
     * @returns A promise that resolves to a set of completions.
     */
    query(prompt: string, requestPostOptions?: PostOptions): Promise<Set<string>>;
    /**
     * Get completions from the LLM; issue a warning if it did not produce any
     *
     * @param prompt the prompt to use
     */
    completions(prompt: string, temperature: number): Promise<Set<string>>;
}
export {};
