let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('should handle an array with decimal numbers', function(done) {
        const result = testcode.average([1.5, 2.5, 3.5]);
        assert.strictEqual(result, 2.5);
        done();
    });
});