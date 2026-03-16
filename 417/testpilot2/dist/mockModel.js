"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.MockCompletionModel = void 0;
const path_1 = __importDefault(require("path"));
const fs_1 = require("fs");
class MockCompletionModel {
    constructor(strictResponses) {
        this.strictResponses = strictResponses;
        this.completionMap = new Map();
    }
    static fromFile(file, strictResponses) {
        const data = JSON.parse((0, fs_1.readFileSync)(file, "utf8"));
        console.log("Loading completions from file");
        const model = new MockCompletionModel(strictResponses);
        for (const { file: promptFile, temperature, completions } of data.prompts) {
            const prompt = (0, fs_1.readFileSync)(path_1.default.join(path_1.default.dirname(file), "prompts", promptFile), "utf8");
            model.addCompletions(prompt, temperature, completions);
        }
        return model;
    }
    key(prompt, temperature) {
        return JSON.stringify([prompt, temperature]);
    }
    addCompletions(prompt, temperature, completions) {
        this.completionMap.set(this.key(prompt, temperature), completions);
    }
    async completions(prompt, temperature) {
        const completions = this.completionMap.get(this.key(prompt, temperature));
        if (!completions) {
            const err = `Prompt not found at temperature ${temperature}: ${prompt}`;
            if (this.strictResponses) {
                throw new Error(err);
            }
            else {
                console.warn(err);
            }
        }
        return new Set(completions);
    }
}
exports.MockCompletionModel = MockCompletionModel;
//# sourceMappingURL=mockModel.js.map