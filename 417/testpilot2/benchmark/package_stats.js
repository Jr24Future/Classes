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
const axios_1 = __importDefault(require("axios"));
const child_process = __importStar(require("child_process"));
const fs = __importStar(require("fs"));
const os = __importStar(require("os"));
const path = __importStar(require("path"));
const simple_git_1 = __importDefault(require("simple-git"));
if (process.argv.length !== 3) {
    console.error("Usage: package_stats.js <package_dir>");
    console.error("  package_dir: Directory containing package.json");
    console.error();
    console.error("This script computes statistics for a package.");
    process.exit(1);
}
const pkgDir = process.argv[2];
const packageName = JSON.parse(fs.readFileSync(path.join(pkgDir, "package.json"), "utf8")).name;
(async () => {
    const git = (0, simple_git_1.default)(pkgDir);
    const weeklyDownloadsUrl = `https://api.npmjs.org/downloads/point/last-week/${packageName}`;
    let weeklyDownloads = 0;
    try {
        weeklyDownloads = (await axios_1.default.get(weeklyDownloadsUrl)).data.downloads;
    }
    catch (e) {
        console.warn(`Failed to get weekly downloads for ${packageName}: ${e}`);
        console.warn("Weekly downloads will be set to 0.");
    }
    const nyc = path.join(__dirname, "..", "node_modules", ".bin", "nyc");
    const tmpdir = fs.mkdtempSync(path.join(os.tmpdir(), "package_stats"));
    child_process.execFileSync(nyc, [
        "--reporter=json-summary",
        `--report-dir=${tmpdir}`,
        `--temp-dir=${tmpdir}`,
        "node",
        "-e",
        'require(".")',
    ], { cwd: pkgDir });
    const coverageFromLoading = JSON.parse(fs.readFileSync(path.join(tmpdir, "coverage-summary.json"), "utf8")).total;
    const loc = coverageFromLoading.lines.total;
    const repository = (await git.listRemote(["--get-url"])).trim();
    const sha = (await git.revparse(["HEAD"])).trim();
    console.log(JSON.stringify({
        packageName,
        repository,
        sha,
        loc,
        weeklyDownloads,
        coverageFromLoading,
    }, null, 2));
})().catch((e) => {
    console.error(e);
    process.exit(1);
});
//# sourceMappingURL=package_stats.js.map