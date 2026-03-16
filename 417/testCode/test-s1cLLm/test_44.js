let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('should return "positive" for positive numbers', function(done) {
        assert.strictEqual(testcode.classifySign(1), 'positive');
        assert.strictEqual(testcode.classifySign(5), 'positive');
        done();
    });
});