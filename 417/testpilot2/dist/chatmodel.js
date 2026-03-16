"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.ChatModel = void 0;
const axios_1 = __importDefault(require("axios"));
const perf_hooks_1 = require("perf_hooks");
const promise_utils_1 = require("./promise-utils");
const defaultPostOptions = {
    max_tokens: 1000,
    temperature: 0,
    top_p: 1, // no need to change this
};
function getEnv(name) {
    const value = process.env[name];
    if (!value) {
        console.error(`Please set the ${name} environment variable.`);
        process.exit(1);
    }
    return value;
}
/**
 * A model that uses the ChatModel API to provide completions.
 */
class ChatModel {
    constructor(model, nrAttempts, rateLimiter, instanceOptions = {}) {
        this.model = model;
        this.nrAttempts = nrAttempts;
        this.rateLimiter = rateLimiter;
        this.instanceOptions = instanceOptions;
        this.apiEndpoint = getEnv("TESTPILOT_LLM_API_ENDPOINT");
        this.authHeaders = getEnv("TESTPILOT_LLM_AUTH_HEADERS");
        console.log(`Using ${this.model} at ${this.apiEndpoint} with ${this.nrAttempts} attempts and ${this.rateLimiter.getDescription()}`);
    }
    /**
     * Query the ChatModel for completions with a given prompt.
     *
     * @param prompt The prompt to use for the completion.
     * @param requestPostOptions The options to use for the request.
     * @returns A promise that resolves to a set of completions.
     */
    async query(prompt, requestPostOptions = {}) {
        const headers = {
            "Content-Type": "application/json",
            ...JSON.parse(this.authHeaders),
        };
        const options = {
            ...defaultPostOptions,
            // options provided to constructor override default options
            ...this.instanceOptions,
            // options provided to this function override default and instance options
            ...requestPostOptions,
        };
        perf_hooks_1.performance.mark("llm-query-start");
        const postOptions = {
            model: this.model,
            messages: [
                {
                    role: "system",
                    content: "You are a programming assistant.",
                },
                {
                    role: "user",
                    content: prompt,
                },
            ],
            ...options,
        };
        const res = await (0, promise_utils_1.retry)(() => this.rateLimiter.next(() => axios_1.default.post(this.apiEndpoint, postOptions, { headers })), this.nrAttempts);
        perf_hooks_1.performance.measure(`llm-query:${JSON.stringify({
            ...options,
            promptLength: prompt.length,
        })}`, "llm-query-start");
        if (res.status !== 200) {
            throw new Error(`Request failed with status ${res.status} and message ${res.statusText}`);
        }
        if (!res.data) {
            throw new Error("Response data is empty");
        }
        const json = res.data;
        if (json.error) {
            throw new Error(json.error);
        }
        const completions = new Set();
        for (const choice of json.choices) {
            const content = choice.message.content;
            completions.add(content);
        }
        return completions;
    }
    /**
     * Get completions from the LLM; issue a warning if it did not produce any
     *
     * @param prompt the prompt to use
     */
    async completions(prompt, temperature) {
        try {
            let result = new Set();
            for (const completion of await this.query(prompt, { temperature })) {
                result.add(completion);
            }
            return result;
        }
        catch (err) {
            console.warn(`Failed to get completions: ${err.message}`);
            return new Set();
        }
    }
}
exports.ChatModel = ChatModel;
//# sourceMappingURL=chatmodel.js.map