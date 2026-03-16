type Partition = Set<string>;
export type SnippetMap = (functionName: string) => string[] | undefined;
export declare class Snippets {
    /** The maximum number of snippets we can comfortably handle. */
    MAX_SNIPPETS: number;
    /** A cache recording Levenshtein distance between pairs of strings. */
    distanceCache: Map<string, number>;
    constructor();
    /**
     * Create the partitions. Initially each snippet is in its own partition.
     * @param snippets The snippets to partition.
     * @returns The partitions.
     */
    createPartitions(snippets: Set<string>): Partition[];
    /**
     * Compute the Levenshtein distance between two strings, utilizing a cache.
     */
    computeDistance(a: string, b: string): number;
    /**
     * Determine the lowest Levenshtein distance between elements of two partitions.
     * @param partition1 The first partition to compare.
     * @param partition2 The second partition to compare.
     * @returns The lowest Levenshtein distance between elements of the two partitions.
     */
    comparePartitions(partition1: Partition, partition2: Partition): number;
    /**
     * Merge the two partitions with the lowest Levenshtein distance between them.
     * @param partitions The partitions.
     * @returns The partitions after merging.
     */
    mergeMostSimilarPartitions(partitions: Partition[]): Partition[];
    /**
     * Select a set of representative snippets. This is done by grouping
     * the snippets into partitions so that the elements of each partition
     * are as similar as possible, and then selecting the smallest snippet
     * from each partition.
     * @param snippets The snippets to select representatives for.
     * @returns The selected snippets.
     */
    selectSnippets(snippets: Set<string>, n: number): Set<string>;
}
export {};
