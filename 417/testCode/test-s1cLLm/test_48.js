let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test suite', function() {
    it('test case', function(done) {
        // testcode.js
module.exports = {
    classifySign: function(num) {
        if (num > 0) {
            return 'positive';
        } else if (num < 0) {
            return 'negative';
        } else {
            return 'zero';
        }
    }
};
    })
})