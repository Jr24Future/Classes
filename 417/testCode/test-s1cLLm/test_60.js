let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('should handle an array with a single number', function(done) {
        const result = testcode.average([10]);
        assert.strictEqual(result, 10);
        done();
    });

    })