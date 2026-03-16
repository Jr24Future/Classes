let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('test testcode.absDiff with negative numbers', function(done) {
        assert.strictEqual(testcode.absDiff(-5, -3), 2);
        assert.strictEqual(testcode.absDiff(-10, -4), 6);
        done();
    });

    })