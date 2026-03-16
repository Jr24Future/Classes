/**
 * This function provides supports for retrying the creation of a promise
 * up to a given number of times in case the promise is rejected.
 * This is useful for, e.g., retrying a request to a server that is temporarily unavailable.
 *
 */
export declare function retry<T>(f: () => Promise<T>, howManyTimes: number): Promise<T>;
/**
 * This interface provides supports for retrying the creation of a promise
 */
export interface IRateLimiter {
    /**
     * Waits until the rate limiter allows the next request, then evaluate the function that
     * produces the promise
     */
    next<T>(p: () => Promise<T>): Promise<T>;
    /**
     * returns a description of the rate limiter
     */
    getDescription(): string;
}
/**
 * This class provides supports for asynchronous rate limiting by
 * limiting the number of requests to the server to at most one
 * in N milliseconds. This is useful for throttling requests to
 * a server that has a limit on the number of requests per second.
 */
export declare abstract class RateLimiter implements IRateLimiter {
    protected howManyMilliSeconds: number;
    constructor(howManyMilliSeconds: number);
    /**
     * the timer is a promise that is resolved after a certain number of milliseconds
     * have elapsed. The timer is reset after each request.
     */
    private timer;
    /**
     *  Waits until the timer has expired, then evaluate the function that
     * produces the promise
     * @param p a function that produces a promise
     * @returns returns the promise produced by the function p (after the timer has expired)
     */
    next<T>(p: () => Promise<T>): Promise<T>;
    abstract getDescription(): string;
    /**
     * resets the timer
     * @returns a promise that is resolved after the number of milliseconds
     *         specified in the constructor have elapsed
     */
    protected resetTimer: () => Promise<void>;
}
/**
 * A rate limiter that limits the number of requests to the server to a
 * maximum of one per N milliseconds.
 *
 */
export declare class FixedRateLimiter extends RateLimiter implements IRateLimiter {
    constructor(N: number);
    /**
     * returns a description of the rate limiter
     */
    getDescription(): string;
}
/**
 * A custom rate limiter for use during benchmark runs. It increases
 * the pace of requests after two designated thresholds have been reached.
 */
export declare class BenchmarkRateLimiter extends RateLimiter {
    private requestCount;
    private static INITIAL_PACE;
    private static PACE_AFTER_150_REQUESTS;
    private static PACE_AFTER_300_REQUESTS;
    constructor();
    next<T>(p: () => Promise<T>): Promise<T>;
    /**
     * returns a description of the rate limiter
     */
    getDescription(): string;
}
/**
 * A rate limiter that does not limit the rate of requests to the server.
 */
export declare class NoRateLimiter implements IRateLimiter {
    next<T>(p: () => Promise<T>): Promise<T>;
    /**
     * returns a description of the rate limiter
     */
    getDescription(): string;
}
