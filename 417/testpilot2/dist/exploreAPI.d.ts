export type FunctionDescriptor = {
    type: "function";
    signature: string;
    isAsync: boolean;
    implementation: string;
    isConstructor: boolean;
    docComment?: string;
};
export type ApiElementDescriptor = {
    type: "bigint" | "boolean" | "number" | "string" | "symbol" | "object" | "array" | "undefined" | "null";
} | FunctionDescriptor;
export declare class API {
    private readonly elements;
    constructor(elements?: Map<string, ApiElementDescriptor>);
    set(accessPath: string, value: ApiElementDescriptor): void;
    get(accessPath: string): ApiElementDescriptor | undefined;
    getFunctions(packageName: string): Generator<APIFunction, void, unknown>;
    toJSON(): [string, ApiElementDescriptor][];
    static fromJSON(json: [string, ApiElementDescriptor][]): API;
}
/**
 * A representation of an API function, including both its access path and a
 * function descriptor.
 */
export declare class APIFunction {
    readonly accessPath: string;
    readonly descriptor: FunctionDescriptor;
    readonly packageName: string;
    constructor(accessPath: string, descriptor: FunctionDescriptor, packageName: string);
    /**
     * Parse a given signature into an API function.
     *
     * The signature is expected to consist of an optional initial `class `, then
     * a dot-separated access path (starting with the package name, ending with
     * the function name), followed by a parenthesised list of parameters,
     * optionally followed by ` async`.
     *
     * Example:
     *
     * ```
     * zip-a-folder.ZipAFolder.tar(srcFolder, tarFilePath, zipAFolderOptions) async
     * ```
     */
    static fromSignature(signature: string, implementation?: string): APIFunction;
    /** Serialize the API function to a JSON object. */
    toJSON(): object;
    /** Deserialize an API function from a JSON object. */
    static fromJSON(json: object): APIFunction;
    /** The name of the function itself. */
    get functionName(): string;
    /** The full signature of the function. */
    get signature(): string;
}
/**
 * Normalizes a function implementation to unify whitespace. This allows matching functions identified through parsing the
 * source code to those identified dynamically from the object graph.
 * @param source implementation source code to normalize
 * @returns normalized source code
 */
export declare function normalizeFunctionSource(source: string): string;
/**
 * Sanitize package name by replacing non-alphanumeric characters with underscores.
 * @param pkgName the package name to sanitize
 */
export declare function sanitizePackageName(pkgName: string): string;
/**
 * Populates the `docComments` map with the doc comments found in the given code.
 * @param code the code to search for functions and their corresponding docComments in
 * @param docComments the map to populate with doc comments, where the map key is the normalized function source code
 * @returns the passed code as is
 */
export declare function findDocComments(code: string, docComments: Map<string, string>): string;
export declare function exploreAPI(pkgPath: string): API;
