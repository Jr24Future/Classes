"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.runExperiment = void 0;
const fs_1 = __importDefault(require("fs"));
const path_1 = __importDefault(require("path"));
const perf_hooks_1 = require("perf_hooks");
const __1 = require("..");
const chatmodel_1 = require("../src/chatmodel");
const yargs_1 = __importDefault(require("yargs"));
const helpers_1 = require("yargs/helpers");
const performanceMeasurer_1 = require("./performanceMeasurer");
const testResultCollector_1 = require("./testResultCollector");
const promise_utils_1 = require("../src/promise-utils");
/**
 * Run an end-to-end experiment.
 * Given a package generate tests for its methods, run them, and generate a report.
 * @param model The completion model to use.
 * @param packageName The name of the package to use.
 * @param packagePath The path to the package to use.
 * @param functions The list of functions in the API.
 * @param snippetMap The snippets for package methods.
 * @param timeLimit The maximum time (in milliseconds) to run the experiment.
 */
async function runExperiment(functions, temperatures, snippetMap, model, templateFileName, retryTemplateFileName, validator, collector, timeLimit) {
    const deadline = perf_hooks_1.performance.now() + timeLimit;
    const generator = new __1.TestGenerator(temperatures, (fn) => snippetMap.get(fn), model, templateFileName, retryTemplateFileName, validator, collector);
    // initialize the workList with all functions
    let workList = functions.map((f) => ({ fun: f, nrTimesExtended: 0 }));
    while (workList.length > 0) {
        if (perf_hooks_1.performance.now() > deadline) {
            console.log(`Time limit reached, ${workList.length} worklist items ignored.`);
            break;
        }
        const { fun } = workList.shift();
        await generator.generateAndValidateTests(fun);
    }
    collector.recordCoverageInfo(validator.computeCoverageSummary());
}
exports.runExperiment = runExperiment;
if (require.main === module) {
    (async () => {
        var _a, _b;
        const parser = (0, yargs_1.default)((0, helpers_1.hideBin)(process.argv))
            .strict()
            .options({
            outputDir: {
                type: "string",
                demandOption: true,
                description: "directory where output files will be placed",
            },
            package: {
                type: "string",
                demandOption: true,
                description: "package source",
            },
            api: {
                type: "string",
                description: "JSON file with API to generate tests for (usually api.json from a previous run)",
            },
            snippets: {
                type: "string",
                choices: ["code", "doc", "both", "none"],
                default: "doc",
                description: "where to collect usage snippets from",
            },
            database: {
                type: "string",
                description: "CodeQL database; only required if collecting snippets from code",
            },
            responses: {
                type: "string",
                description: "file with simulated model responses (usually prompts.json from a previous run)",
            },
            timeLimit: {
                type: "number",
                default: 5 * 60 * 60,
                description: "time limit in seconds (default is five hours)",
            },
            numSnippets: {
                default: "all",
                description: 'number of snippets to include in the prompt, or "all" to include all snippets',
            },
            snippetLength: {
                type: "number",
                default: 20,
                description: "maximum length of each snippet in lines",
            },
            maxTokens: {
                type: "number",
                default: 1000,
                demandOption: false,
                description: "maximum number of tokens in a completion",
            },
            temperatures: {
                type: "string",
                default: "0.0",
                description: "whitespace-separated list of sampling temperatures to try when obtaining completions",
            },
            numCompletions: {
                type: "number",
                default: 5,
                description: "number of completions to generate for each prompt",
            },
            strictResponses: {
                type: "boolean",
                default: true,
                description: "whether to require that all prompts are found when running with --responses; does not have any effect otherwise",
            },
            model: {
                type: "string",
                demandOption: true,
                default: "gpt-4o-mini",
                description: "LLM to use",
            },
            template: {
                type: "string",
                demandOption: true,
                default: "./templates/template.hb",
                description: "Handlebars template file to use",
            },
            retryTemplate: {
                type: "string",
                demandOption: true,
                default: "./templates/retry-template.hb",
                description: "Handlebars template file to use",
            },
            nrAttempts: {
                type: "number",
                default: 3,
                description: "number of attempts to make for each request",
            },
            rateLimit: {
                type: "string",
                default: "",
                demandOption: false,
                description: 'number of milliseconds between prompts or "benchmark"',
            },
        });
        const argv = await parser.argv;
        var model;
        if (!argv.responses) {
            if (argv.strictResponses) {
                console.warn("Warning: --strictResponses has no effect when not using --responses");
            }
            let rateLimiter;
            if (argv.rateLimit === "benchmark") {
                rateLimiter = new promise_utils_1.BenchmarkRateLimiter();
            }
            else if (argv.rateLimit) {
                const rateLimit = parseInt(argv.rateLimit, 10);
                if (!Number.isNaN(rateLimit)) {
                    rateLimiter = new promise_utils_1.FixedRateLimiter(+argv.rateLimit);
                }
                else {
                    throw new Error(`Invalid rate limit: ${argv.rateLimit}`);
                }
            }
            else {
                rateLimiter = new promise_utils_1.NoRateLimiter();
            }
            model = new chatmodel_1.ChatModel(argv.model, argv.nrAttempts, rateLimiter, {
                max_tokens: argv.maxTokens,
            });
        }
        else {
            model = __1.MockCompletionModel.fromFile(argv.responses, argv.strictResponses);
        }
        const packagePath = path_1.default.resolve(argv.package);
        const packageName = JSON.parse(fs_1.default.readFileSync(path_1.default.join(packagePath, "package.json"), "utf8")).name;
        const perf = new performanceMeasurer_1.PerformanceMeasurer();
        console.log(`Running experiment for ${packageName}`);
        let api;
        if (argv.api) {
            console.log(`Loading API from ${argv.api}`);
            const rawApi = JSON.parse(fs_1.default.readFileSync(argv.api, "utf8"));
            api = rawApi.map(({ accessPath, descriptor }) => new __1.APIFunction(accessPath, descriptor, packageName));
        }
        else {
            console.log("Exploring API");
            api = Array.from((0, __1.exploreAPI)(packagePath).getFunctions(packageName));
        }
        let numSnippets = argv.numSnippets === "all" ? argv.numSnippets : +argv.numSnippets;
        if (numSnippets !== "all" && !(numSnippets >= 0)) {
            throw new Error(`Invalid value for --numSnippets: ${argv.numSnippets}`);
        }
        perf_hooks_1.performance.mark("snippet-extraction-start");
        let allSnippets = new Map();
        if (numSnippets !== 0) {
            console.log("Extracting snippets");
            const functionNames = api.map((f) => f.functionName);
            if (argv.snippets == "code") {
                if (!argv.database) {
                    throw new Error("--database is required if --snippets is code");
                }
                if (numSnippets === "all") {
                    throw new Error("--numSnippets=all is not supported when collecting snippets from code");
                }
                allSnippets = (0, __1.getSnippets)(argv.database, numSnippets, functionNames, argv.snippetLength);
            }
            else if (argv.snippets == "doc") {
                if (argv.database) {
                    console.warn("--database is ignored if --snippets is doc");
                }
                allSnippets = (0, __1.getDocSnippets)(packagePath, numSnippets, functionNames, argv.snippetLength);
            }
            else if (argv.snippets == "both") {
                if (!argv.database) {
                    throw new Error("--database is required if --snippets is code");
                }
                if (numSnippets === "all") {
                    throw new Error("--numSnippets=all is not supported when collecting snippets from code");
                }
                const snippets = (0, __1.getSnippets)(argv.database, numSnippets, functionNames, argv.snippetLength);
                const docSnippets = (0, __1.getDocSnippets)(packagePath, numSnippets, functionNames, argv.snippetLength);
                for (const [key, value] of snippets.entries()) {
                    allSnippets.set(key, [...value, ...(docSnippets.get(key) || [])]);
                }
            }
            else {
                if (argv.database) {
                    console.warn("--database is ignored if --snippets is none");
                }
            }
        }
        perf_hooks_1.performance.measure("snippet-extraction", "snippet-extraction-start");
        console.log("Generating tests");
        const collector = new testResultCollector_1.TestResultCollector(packageName, packagePath, argv.outputDir, api, allSnippets, perf, argv.snippets, numSnippets, argv.snippetLength, argv.numCompletions);
        const validator = new __1.MochaValidator(packageName, packagePath);
        try {
            await runExperiment(api, argv.temperatures.split(/\s+/).map(parseFloat), allSnippets, model, argv.template, argv.retryTemplate, validator, collector, argv.timeLimit * 1000);
            collector.report();
            const report = collector.getReport();
            const coverage = (_b = (_a = report.coverage) === null || _a === void 0 ? void 0 : _a.total.statements.pct) !== null && _b !== void 0 ? _b : 0;
            console.log(`${coverage}% statement coverage`);
        }
        finally {
            validator.cleanup();
        }
    })().catch((e) => {
        console.error(e);
        process.exit(1);
    });
}
//# sourceMappingURL=run.js.map