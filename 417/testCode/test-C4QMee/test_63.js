let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('should return 0 for an empty array', function(done) {
        const result = testcode.average([]);
        assert.strictEqual(result, NaN); // Note: Division by 0 returns NaN
        done();
    });

    })