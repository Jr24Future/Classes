"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.Codex = void 0;
const axios_1 = __importDefault(require("axios"));
const fs_1 = __importDefault(require("fs"));
const perf_hooks_1 = require("perf_hooks");
const syntax_1 = require("./syntax");
const defaultPostOptions = {
    max_tokens: 100,
    temperature: 0,
    n: 5,
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
class Codex {
    constructor(isStarCoder, instanceOptions = {}) {
        this.isStarCoder = isStarCoder;
        this.instanceOptions = instanceOptions;
        this.apiEndpoint = this.isStarCoder
            ? getEnv("STARCODER_API_ENDPOINT")
            : getEnv("TESTPILOT_LLM_API_ENDPOINT");
        this.authHeaders = this.isStarCoder
            ? "{}"
            : getEnv("TESTPILOT_LLM_AUTH_HEADERS");
        console.log(`Using ${this.isStarCoder ? "StarCoder" : "GPT"} API at ${this.apiEndpoint}`);
    }
    /**
     * Query Codex for completions with a given prompt.
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
        perf_hooks_1.performance.mark("codex-query-start");
        const postOptions = this.isStarCoder
            ? {
                inputs: prompt,
                parameters: {
                    max_new_tokens: options.max_tokens,
                    temperature: options.temperature || 0.01,
                    n: options.n,
                },
            }
            : {
                prompt,
                ...options,
            };
        const res = await axios_1.default.post(this.apiEndpoint, postOptions, { headers });
        perf_hooks_1.performance.measure(`codex-query:${JSON.stringify({
            ...options,
            promptLength: prompt.length,
        })}`, "codex-query-start");
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
        let numContentFiltered = 0;
        const completions = new Set();
        if (this.isStarCoder) {
            completions.add(json.generated_text);
        }
        else {
            for (const choice of json.choices || [{ text: "" }]) {
                if (choice.finish_reason === "content_filter") {
                    numContentFiltered++;
                }
                completions.add(choice.text);
            }
        }
        if (numContentFiltered > 0) {
            console.warn(`${numContentFiltered} completions were truncated due to content filtering.`);
        }
        return completions;
    }
    /**
     * Get completions from Codex and postprocess them as needed; print a warning if it did not produce any
     *
     * @param prompt the prompt to use
     */
    async completions(prompt, temperature) {
        try {
            let result = new Set();
            for (const completion of await this.query(prompt, { temperature })) {
                result.add((0, syntax_1.trimCompletion)(completion));
            }
            return result;
        }
        catch (err) {
            console.warn(`Failed to get completions: ${err.message}`);
            return new Set();
        }
    }
}
exports.Codex = Codex;
if (require.main === module) {
    (async () => {
        const codex = new Codex(false);
        const prompt = fs_1.default.readFileSync(0, "utf8");
        const responses = await codex.query(prompt, { n: 1 });
        console.log([...responses][0]);
    })().catch((err) => {
        console.error(err);
        process.exit(1);
    });
}
//# sourceMappingURL=codex.js.map