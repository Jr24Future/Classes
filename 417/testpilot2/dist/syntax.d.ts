/**
 * Fix the given code by adding missing closing brackets.
 *
 * @param code The incomplete code.
 * @returns Fixed code with closing brackets, or undefined if it cannot be fixed
 *          with closing brackets.
 */
export declare function closeBrackets(code: string): {
    source: string;
    ast: any;
} | undefined;
/**
 * Trim a completion to avoid incomplete lines and extra whitespace, and make
 * sure it does not break out of enclosing syntactic scopes by closing more
 * brackets than it opens.
 *
 * @param completion The completion.
 * @returns The trimmed completion.
 */
export declare function trimCompletion(completion: string): string;
/**
 * Combine a function's doc comment into a single trimmed commented string
 * @param docComment the original doc comment, as extracted by exploreAPI
 * @returns the doc comment with all non-empty lines starting with // (instead of '* ')
 */
export declare function trimAndCombineDocComment(docComment: string): string;
/**
 * Comment out the given code line by line.
 */
export declare function commentOut(code: string): string;
