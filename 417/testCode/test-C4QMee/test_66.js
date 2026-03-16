let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('should handle an array with mixed positive and negative numbers', function(done) {
        const result = testcode.average([-1, 0, 1]);
        assert.strictEqual(result, 0);
        done();
    });
});