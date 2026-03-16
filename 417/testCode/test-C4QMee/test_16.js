let mocha = require('mocha');
let assert = require('assert');
let testcode = require('./testcode'); // Ensure the correct path to the testcode module

describe('test testcode', function() {
    it('should return the sum of two positive numbers', function(done) {
        let result = testcode.add(2, 3);
        assert.strictEqual(result, 5);
        done();
    });
});