import { ICompletionModel } from "./completionModel";
export declare class MockCompletionModel implements ICompletionModel {
    private strictResponses;
    private completionMap;
    constructor(strictResponses: boolean);
    static fromFile(file: string, strictResponses: boolean): MockCompletionModel;
    private key;
    addCompletions(prompt: string, temperature: number, completions: string[]): void;
    completions(prompt: string, temperature: number): Promise<Set<string>>;
}
