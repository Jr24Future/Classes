// test/testcode.js
// Small helper so all the generated tests that do require('./testcode')
// get the real implementation from src/math.js

module.exports = require('..');  // this loads src/math.js via package.json "main"
