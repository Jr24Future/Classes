let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('should handle an array with negative numbers', function(done) {
        const result = testcode.average([-1, -2, -3, -4, -5]);
        assert.strictEqual(result, -3);
        done();
    });

    })