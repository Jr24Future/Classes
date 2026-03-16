"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.getSnippets = exports.getSnippetsInfo = exports.getSnippetData = void 0;
const cp = __importStar(require("child_process"));
const fs = __importStar(require("fs"));
const os = __importStar(require("os"));
const path = __importStar(require("path"));
const yargs_1 = __importDefault(require("yargs"));
const helpers_1 = require("yargs/helpers");
const adm_zip_1 = __importDefault(require("adm-zip"));
const snippetHelper_1 = require("./snippetHelper");
const snippetHelper = new snippetHelper_1.Snippets();
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
function* getSnippetData(database, methods) {
    // create temporary CSV file to store relevant method names in
    const csvFile = `${os.tmpdir()}/targetMethod.csv`;
    const escapedMethodNames = methods.map((method) => `"${method.replace(/"/g, '""')}"`);
    fs.writeFileSync(csvFile, escapedMethodNames.join("\n") + "\n");
    // run mining query
    const bqrsFile = `${os.tmpdir()}/results.bqrs`;
    cp.execFileSync("codeql", [
        "query",
        "run",
        "-d",
        database,
        "-o",
        bqrsFile,
        "--external",
        `targetFunction=${csvFile}`,
        path.join(__dirname, "../../ql/queries/SnippetMining.ql"),
    ], { stdio: "inherit" });
    // decode results into CSV format
    const outputFile = `${os.tmpdir()}/results.csv`;
    cp.execFileSync("codeql", [
        "bqrs",
        "decode",
        "--format",
        "csv",
        "--no-titles",
        "--entities",
        "id",
        "--output",
        outputFile,
        bqrsFile,
    ], { stdio: "inherit" });
    const results = fs.readFileSync(outputFile, "utf8");
    for (const data of results.split("\n")) {
        let [id, method, file, line] = data.split(",");
        if (!id) {
            continue;
        }
        yield {
            id: +id,
            method: method.slice(1, -1),
            file: file.slice(1, -1),
            line: +line,
        };
    }
}
exports.getSnippetData = getSnippetData;
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
function getSnippetsInfo(database, methods) {
    const snippets = [];
    for (const { id, method, file, line } of getSnippetData(database, methods)) {
        if (!snippets[id]) {
            snippets[id] = [method, new Map()];
        }
        const fileMap = snippets[id][1];
        if (!fileMap.has(file)) {
            fileMap.set(file, []);
        }
        const lineNumbers = fileMap.get(file);
        lineNumbers.push(line);
    }
    return snippets;
}
exports.getSnippetsInfo = getSnippetsInfo;
/**
 * Extract usage snippets for the given methods from the given CodeQL database.
 *
 * @param database The path to the CodeQL database.
 * @param numSnippets The number of snippets to extract.
 * @param methods The methods to extract usage snippets for.
 * @param maxLength The maximum number of lines to include in each snippet.
 * @returns A string array of usage snippets.
 */
function getSnippets(database, numSnippets, methods, maxLength) {
    let results = new Map();
    // mine snippets
    const snippets = getSnippetsInfo(database, methods);
    // now output them
    const srcArchive = new adm_zip_1.default(path.join(database, "src.zip"));
    for (const i in snippets) {
        const [methodName, files] = snippets[i];
        let currentSnippet = `for ${methodName}`;
        for (const [file, lineNumbers] of files.entries()) {
            const contents = srcArchive.readAsText(file.slice(1));
            const lines = contents.split("\n");
            // pull out relevant lines from the file and record
            // minimum indentation level
            let relevantLineNumbers = lineNumbers.sort((a, b) => a - b);
            if (maxLength !== -1) {
                relevantLineNumbers = relevantLineNumbers.slice(-maxLength);
            }
            const relevantLines = [];
            let minIndent = -1;
            for (const lineNumber of relevantLineNumbers) {
                const line = lines[lineNumber - 1] || "";
                const indent = line.search(/\S/);
                if (minIndent === -1 || indent < minIndent) {
                    minIndent = indent;
                }
                relevantLines.push(line);
            }
            if (minIndent === -1) {
                minIndent = 0;
            }
            // output relevant lines, outdenting them by the minimum indentation
            for (const line of relevantLines) {
                currentSnippet += `\n ${line}`;
            }
        }
        if (results.has(methodName)) {
            results.get(methodName).add(currentSnippet);
        }
        else {
            results.set(methodName, new Set([currentSnippet]));
        }
    }
    // select snippets that are dissimilar
    let finalSnippets = new Map();
    for (let [method, snippets] of results) {
        // if we have too many snippets, throw some away (snippet selection doesn't scale beyond ~50 snippets)
        if (snippets.size > snippetHelper.MAX_SNIPPETS) {
            snippets = new Set([...snippets].slice(0, snippetHelper.MAX_SNIPPETS));
        }
        let selectedSnippets = snippetHelper.selectSnippets(snippets, numSnippets);
        finalSnippets.set(method, Array.from(selectedSnippets));
        snippetHelper.distanceCache.clear();
    }
    return finalSnippets;
}
exports.getSnippets = getSnippets;
if (require.main === module) {
    (async () => {
        const parser = (0, yargs_1.default)((0, helpers_1.hideBin)(process.argv))
            .usage("$0 [-n <num>] [-l <max-length>] <database> <method>")
            .example("$0 ~/databases/memfs toJSON", "extract three usage snippets for method toJSON from the memfs database")
            .option("n", {
            describe: "number of snippets to generate",
            default: 3,
            type: "number",
        })
            .option("l", {
            alias: "length",
            describe: "maximum length of each snippet in lines; -1 means no limit",
            default: -1,
            type: "number",
        })
            .demand(2);
        const argv = await parser.argv;
        const database = argv._[0];
        const methods = argv._.slice(1);
        const numSnippets = argv.n;
        const maxLength = argv.l;
        const allSnippets = getSnippets(database, numSnippets, methods, maxLength);
        for (const [method, snippets] of allSnippets) {
            console.log(`${method}:`);
            console.log(snippets.join("\n"));
        }
    })().catch((err) => {
        console.error(err);
        process.exit(1);
    });
}
//# sourceMappingURL=mineSnippets.js.map