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
exports.exploreAPI = exports.findDocComments = exports.sanitizePackageName = exports.normalizeFunctionSource = exports.APIFunction = exports.API = void 0;
const fs_1 = __importDefault(require("fs"));
const path_1 = __importDefault(require("path"));
const pirates_1 = require("pirates");
const espree = __importStar(require("espree"));
const estraverse = __importStar(require("estraverse"));
const perf_hooks_1 = require("perf_hooks");
function extend(accessPath, component) {
    if (component === "default" && !accessPath.includes(".")) {
        return accessPath;
    }
    else if (component.match(/^[a-zA-Z_$][\w$]*$/)) {
        return accessPath + "." + component;
    }
    else if (component.match(/^\d+$/)) {
        return accessPath + "[" + component + "]";
    }
    else {
        return accessPath + "['" + component.replace(/['\\]/g, "\\$&") + "']";
    }
}
class API {
    constructor(elements = new Map()) {
        this.elements = elements;
    }
    set(accessPath, value) {
        this.elements.set(accessPath, value);
    }
    get(accessPath) {
        return this.elements.get(accessPath);
    }
    *getFunctions(packageName) {
        for (const [accessPath, descriptor] of this.elements) {
            if (descriptor.type === "function") {
                yield new APIFunction(accessPath, descriptor, packageName);
            }
        }
    }
    toJSON() {
        return [...this.elements];
    }
    static fromJSON(json) {
        return new API(new Map(json));
    }
}
exports.API = API;
/**
 * A representation of an API function, including both its access path and a
 * function descriptor.
 */
class APIFunction {
    constructor(accessPath, descriptor, packageName) {
        this.accessPath = accessPath;
        this.descriptor = descriptor;
        this.packageName = packageName;
    }
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
    static fromSignature(signature, implementation = "") {
        const match = signature.match(/^(class )?([-\w$]+(?:\.[\w$]+)*)(\(.*\))( async)?$/);
        if (!match)
            throw new Error(`Invalid signature: ${signature}`);
        const [, isConstructor, accessPath, parameters, isAsync] = match;
        const descriptor = {
            type: "function",
            signature: parameters,
            isAsync: !!isAsync,
            isConstructor: !!isConstructor,
            implementation,
        };
        return new APIFunction(accessPath, descriptor, accessPath.split(".")[0]); // infer package name from accesspath. This will not work for package names that contain a "."
    }
    /** Serialize the API function to a JSON object. */
    toJSON() {
        return {
            accessPath: this.accessPath,
            descriptor: this.descriptor,
            packageName: this.packageName,
        };
    }
    /** Deserialize an API function from a JSON object. */
    static fromJSON(json) {
        const { accessPath, descriptor, packageName } = json;
        return new APIFunction(accessPath, descriptor, packageName);
    }
    /** The name of the function itself. */
    get functionName() {
        return this.accessPath.split(".").pop();
    }
    /** The full signature of the function. */
    get signature() {
        const { signature, isAsync, isConstructor } = this.descriptor;
        return ((isConstructor ? "class " : "") +
            this.accessPath +
            signature +
            (isAsync ? " async" : ""));
    }
}
exports.APIFunction = APIFunction;
const funcToString = Function.prototype.toString;
/**
 * Determine if a function is a constructor
 */
function isConstructor(fn) {
    return funcToString.call(fn).startsWith("class ");
}
function getSignature(fn) {
    let funcStr = funcToString.call(fn);
    if (isConstructor(fn)) {
        // if funcStr does not contain the word 'constructor', then there it is a default constructor with no arguments
        if (!funcStr.match(/constructor\s*\(/)) {
            return "()";
        }
        else {
            // otherwise, find the signature of the constructor
            let match = funcStr.match(/constructor\s*\(([^)]*)\)/);
            if (match) {
                return "(" + match[1] + ")";
            }
            else {
                throw new Error(`Could not find constructor signature in ${funcStr}`);
            }
        }
    }
    let openingParen = funcStr.indexOf("("), closingParen = funcStr.indexOf(")");
    if (openingParen === -1 || closingParen === -1) {
        return "()";
    }
    let funcSig = funcStr.slice(openingParen + 1, closingParen);
    let nrArgs = funcSig.split(",").length;
    if (fn.length <= nrArgs) {
        return `(${funcSig})`;
    }
    else {
        let pseudoArgs = [];
        for (let i = 1; i <= fn.length; i++) {
            pseudoArgs.push(`arg${i}`);
        }
        return `(${pseudoArgs.join(", ")})`;
    }
}
/**
 * Normalizes a function implementation to unify whitespace. This allows matching functions identified through parsing the
 * source code to those identified dynamically from the object graph.
 * @param source implementation source code to normalize
 * @returns normalized source code
 */
function normalizeFunctionSource(source) {
    return source.replace(/\s+/g, " ").replace(/(?<!\w)\s+|\s+(?!\w)/g, "");
}
exports.normalizeFunctionSource = normalizeFunctionSource;
function describe(value, docComments) {
    const type = typeof value;
    switch (type) {
        case "bigint":
        case "boolean":
        case "number":
        case "string":
        case "symbol":
        case "undefined":
            return { type };
        case "object":
            if (value === null) {
                return { type: "null" };
            }
            else if (Array.isArray(value)) {
                return { type: "array" };
            }
            return { type: "object" };
        case "function":
            const isConstr = isConstructor(value);
            const signature = getSignature(value);
            const implementation = funcToString.call(value);
            const isAsync = implementation.startsWith("async ");
            const docComment = docComments.get(normalizeFunctionSource(implementation));
            return {
                type: "function",
                signature,
                implementation,
                isAsync,
                isConstructor: isConstr,
                docComment,
            };
    }
}
function getProperties(obj) {
    let props = new Set();
    // add enumerable properties
    for (let prop in obj) {
        props.add(prop);
    }
    // also add non-enumerable properties (such as static methods)
    const propDescs = Object.getOwnPropertyDescriptors(obj);
    for (let prop in propDescs) {
        const propDesc = propDescs[prop];
        if ("value" in propDesc)
            props.add(prop);
    }
    return props;
}
/**
 * Determines the set of (`path`, `type`) pairs that constitute an API.
 *
 * @param pkgName the name of the package to explore
 * @param pkgExports the object returned by `require(pkgName)`
 */
function exploreExports(pkgName, pkgExports, docComments) {
    const api = new API();
    const seen = new Set();
    function explore(accessPath, value) {
        if (seen.has(value)) {
            return;
        }
        else {
            seen.add(value);
        }
        const descriptor = describe(value, docComments);
        if (descriptor.type !== "object" && descriptor.type !== "null") {
            api.set(accessPath, descriptor);
        }
        exploreProperties(accessPath, descriptor, value);
    }
    function exploreProperties(accessPath, descriptor, value) {
        if (["array", "function", "object"].includes(descriptor.type)) {
            for (const prop of getProperties(value)) {
                // skip private properties as well as special properties of classes, functions, and arrays
                if (prop.startsWith("_") ||
                    ["super", "super_", "constructor"].includes(prop) ||
                    (descriptor.type === "function" &&
                        ["arguments", "caller", "length", "name"].includes(prop)) ||
                    (descriptor.type === "array" && prop === "length")) {
                    continue;
                }
                explore(extend(accessPath, prop), value[prop]);
            }
        }
    }
    explore(pkgName, pkgExports);
    return api;
}
/**
 * Sanitize package name by replacing non-alphanumeric characters with underscores.
 * @param pkgName the package name to sanitize
 */
function sanitizePackageName(pkgName) {
    return pkgName.replace(/[^a-zA-Z0-9_$]/g, "_");
}
exports.sanitizePackageName = sanitizePackageName;
/**
 * Populates the `docComments` map with the doc comments found in the given code.
 * @param code the code to search for functions and their corresponding docComments in
 * @param docComments the map to populate with doc comments, where the map key is the normalized function source code
 * @returns the passed code as is
 */
function findDocComments(code, docComments) {
    perf_hooks_1.performance.mark("doc-comment-extraction-start");
    try {
        const ast = espree.parse(code, {
            ecmaVersion: "latest",
            loc: true,
            comment: true,
        });
        const comments = ast.comments.filter((comment) => comment.type === "Block");
        estraverse.traverse(ast, {
            enter(node) {
                if (node.type === "FunctionDeclaration" ||
                    node.type === "FunctionExpression") {
                    const { start, end } = node;
                    const functionSource = normalizeFunctionSource(code.slice(start, end));
                    //doc comment ends on immediately preceding line
                    const fnDocComment = comments.find((comment) => comment.loc.end.line == node.loc.start.line - 1);
                    if (fnDocComment)
                        docComments.set(functionSource, fnDocComment.value);
                }
            },
        });
    }
    catch (e) {
        console.warn(`Error parsing code with espree: ${e}`); //failed parsing throws a SyntaxError exception
    }
    perf_hooks_1.performance.measure("doc-comment-extraction", "doc-comment-extraction-start");
    return code;
}
exports.findDocComments = findDocComments;
function exploreAPI(pkgPath) {
    perf_hooks_1.performance.mark("api-exploration-start");
    const pkgName = JSON.parse(fs_1.default.readFileSync(path_1.default.join(pkgPath, "package.json"), "utf8")).name;
    const docComments = new Map();
    const revert = (0, pirates_1.addHook)((code, filename) => findDocComments(code, docComments));
    const pkgExports = require(pkgPath);
    revert();
    const api = exploreExports(pkgName, pkgExports, docComments);
    perf_hooks_1.performance.measure("api-exploration", "api-exploration-start");
    return api;
}
exports.exploreAPI = exploreAPI;
if (require.main === module) {
    // Usage: node exploreAPI.js <pkgPath>
    console.log(JSON.stringify(exploreAPI(process.argv[2]), null, 2));
}
//# sourceMappingURL=exploreAPI.js.map