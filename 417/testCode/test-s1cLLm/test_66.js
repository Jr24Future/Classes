let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('should return NaN for an empty array', function(done) {
        const result = testcode.average([]);
        assert.strictEqual(result, NaN); // Use strictEqual to check for NaN
        done();
    });
});