let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test suite', function() {
    it('test case', function(done) {
        // testcode.js
function classifySign(number) {
    if (number > 0) {
        return 'positive';
    } else if (number < 0) {
        return 'negative';
    } else {
        return 'zero';
    }
}

module.exports = { classifySign };
    })
})