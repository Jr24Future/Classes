let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test suite', function() {
    it('test case', function(done) {
        // testcode.js
module.exports = {
    absDiff: function(a, b) {
        return Math.abs(a - b);
    }
};
    })
})