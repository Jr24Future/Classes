/**
 * Mine snippets from fenced code blocks in markdown files and example JS files and return as JSON.
 * @param dirName the directory in which to search
 * @param numSnippets the number of snippets to mine
 * @param methods array of methods to extract usage snippets for
 * @param maxLength The maximum number of lines to include in each snippet.
 * @returns a stringified JSON object containing the mined snippets and stats about the mined snippets
 */
export declare function getDocSnippetsAsJson(dirName: string, numSnippets: number, methods: string[], maxLength: number): string;
/**
 * Mine snippets from fenced code blocks in markdown files.
 * @param dirName the directory in which to search
 * @param numSnippets the maximum number of snippets to mine per method, or "all" to mine all snippets
 * @param methods array of methods to extract usage snippets for
 * @param maxLength The maximum number of lines to include in each snippet.
 * @returns a map associating a method name to a set of usage snippets for that method
 */
export declare function getDocSnippets(dirName: string, numSnippets: number | "all", methods: string[], maxLength: number): Map<string, string[]>;
/**
 * Trim a given code snippet to a max number of lines
 * @param snippet the snippet
 * @param maxLength maximum number of lines to inlcude in the snippet
 * @returns the trimmed snippet with only maxLength lines kept
 */
export declare function trimSnippetToMaxLength(snippet: string, maxLength: number): string;
/**
 * Make sure the method named is involved in a call
 * @param snippet the snippet
 * @param method the method name
 * @returns true if the method name is involved in a call, false otherwise
 */
export declare function callsAPIMethod(snippet: string, methodName: string): boolean;
/**
 * Find fenced code blocks in a given markdown file
 * @param file the markdown file to search
 * @returns a set of fenced code blocks in the given markdown file
 */
export declare function findFencedCodeBlocks(fileName: string): Set<string>;
export declare function getPackageMethods(pkgName: string, methodsFile?: string): string[];
