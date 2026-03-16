let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test suite', function() {
    it('test case', function(done) {
        // testcode.js
function average(arr) {
    if (arr.length === 0) return 0; // Handle empty array case
    const sum = arr.reduce((acc, num) => acc + num, 0);
    return sum / arr.length;
}

module.exports = { average };
    })
})