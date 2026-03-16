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
Object.defineProperty(exports, "__esModule", { value: true });
exports.commentOut = exports.trimAndCombineDocComment = exports.trimCompletion = exports.closeBrackets = void 0;
const espree = __importStar(require("espree"));
/** A map from opening brackets to their corresponding closing brackets. */
const closing = new Map([
    ["(", ")"],
    ["{", "}"],
    ["[", "]"],
]);
/** All closing brackets. */
const closers = new Set(closing.values());
/**
 * Fix the given code by adding missing closing brackets.
 *
 * @param code The incomplete code.
 * @returns Fixed code with closing brackets, or undefined if it cannot be fixed
 *          with closing brackets.
 */
function closeBrackets(code) {
    let brackets = ""; // all outstanding closing brackets, in order
    for (let i = 0; i < code.length; ++i) {
        if (code[i] === "/" && code[i + 1] === "/") {
            // skip line comment
            const nl = code.indexOf("\n", i);
            if (nl === -1) {
                break;
            }
            i = nl;
        }
        else if (closing.has(code[i])) {
            // when we see an opening bracket, add the corresponding closing bracket
            brackets = closing.get(code[i]) + brackets;
        }
        else if (closers.has(code[i])) {
            if (brackets[0] === code[i]) {
                // closing brackets matches, so remove it
                brackets = brackets.slice(1);
            }
            else {
                // closing brackets does not match, so we cannot fix this code
                return undefined;
            }
        }
    }
    try {
        const ast = espree.parse(code + brackets, { ecmaVersion: "latest" });
        return { source: (code + brackets).trim(), ast };
    }
    catch (err) { }
    return undefined;
}
exports.closeBrackets = closeBrackets;
/**
 * Trim a completion to avoid incomplete lines and extra whitespace, and make
 * sure it does not break out of enclosing syntactic scopes by closing more
 * brackets than it opens.
 *
 * @param completion The completion.
 * @returns The trimmed completion.
 */
function trimCompletion(completion) {
    let endOfLastLine = completion.includes("\n")
        ? completion.lastIndexOf("\n")
        : 0;
    // Avoid incomplete lines
    if (!completion.match(/[;})]\s*$/)) {
        completion = completion.slice(0, endOfLastLine);
    }
    // check if more brackets are closed than opened
    let stack = [];
    for (let i = 0; i < completion.length; ++i) {
        if (completion[i] === "{" || completion[i] === "(") {
            stack.push(completion[i]);
        }
        else if (completion[i] === "}" || completion[i] === ")") {
            if (stack.length === 0) {
                completion = completion.slice(0, i);
                break;
            }
            stack.pop();
        }
    }
    return completion.trim();
}
exports.trimCompletion = trimCompletion;
/**
 * Combine a function's doc comment into a single trimmed commented string
 * @param docComment the original doc comment, as extracted by exploreAPI
 * @returns the doc comment with all non-empty lines starting with // (instead of '* ')
 */
function trimAndCombineDocComment(docComment) {
    return commentOut(docComment
        .split("\n")
        .map((line) => line.replace("*", "").trim())
        .filter((line) => line !== "")
        .join("\n"));
}
exports.trimAndCombineDocComment = trimAndCombineDocComment;
/**
 * Comment out the given code line by line.
 */
function commentOut(code) {
    let lines = code.split("\n");
    // remove trailing empty line
    if (lines[lines.length - 1] === "") {
        lines.pop();
    }
    return lines.map((line) => `// ${line}\n`).join("");
}
exports.commentOut = commentOut;
//# sourceMappingURL=syntax.js.map