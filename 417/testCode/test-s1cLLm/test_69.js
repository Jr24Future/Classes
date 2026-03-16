let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test suite', function() {
    it('test case', function(done) {
        // testcode.js
function average(numbers) {
    if (numbers.length === 0) return 0; // Handle empty array case
    const sum = numbers.reduce((acc, num) => acc + num, 0);
    return sum / numbers.length;
}

module.exports = { average };
    })
})