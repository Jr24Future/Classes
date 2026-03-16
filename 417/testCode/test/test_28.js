let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('test testcode.absDiff with equal numbers', function(done) {
        assert.strictEqual(testcode.absDiff(7, 7), 0);
        assert.strictEqual(testcode.absDiff(-2, -2), 0);
        done();
    });
});