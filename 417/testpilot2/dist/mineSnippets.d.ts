/**
 * Extract raw information about usage snippets for the given methods from the
 * given CodeQL database.
 *
 * @param database The path to the CodeQL database.
 * @param methods The methods to extract usage snippets for.
 * @returns A stream of result tuples `{id, method, file, line}`, where `id` is
 *          the CodeQL ID of a call to `method`, and `file`:`line` belongs to
 *          the intraprocedural slice of this call.
 */
export declare function getSnippetData(database: string, methods: string[]): Generator<{
    id: number;
    method: string;
    file: string;
    line: number;
}, void, unknown>;
type SnippetMap = [string, Map<string, number[]>][];
/**
 * Extract structured information about usage snippets for the given methods
 * from the given CodeQL database
 *
 * @param database The path to the CodeQL database.
 * @param methods The methods to extract usage snippets for.
 * @returns A sparse array indexed by CodeQL IDs. For each ID it records the
 *          name of the called method as well as a map from file names to
 *          relevant line numbers in that file.
 */
export declare function getSnippetsInfo(database: string, methods: string[]): SnippetMap;
/**
 * Extract usage snippets for the given methods from the given CodeQL database.
 *
 * @param database The path to the CodeQL database.
 * @param numSnippets The number of snippets to extract.
 * @param methods The methods to extract usage snippets for.
 * @param maxLength The maximum number of lines to include in each snippet.
 * @returns A string array of usage snippets.
 */
export declare function getSnippets(database: string, numSnippets: number, methods: string[], maxLength: number): Map<string, string[]>;
export {};
