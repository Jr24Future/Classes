let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('test testcode.absDiff with zero', function(done) {
        assert.strictEqual(testcode.absDiff(0, 0), 0);
        assert.strictEqual(testcode.absDiff(0, 5), 5);
        assert.strictEqual(testcode.absDiff(5, 0), 5);
        done();
    });

    })