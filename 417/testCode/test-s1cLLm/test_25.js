let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('test testcode.absDiff with mixed numbers', function(done) {
        assert.strictEqual(testcode.absDiff(5, -3), 8);
        assert.strictEqual(testcode.absDiff(-5, 3), 8);
        done();
    });

    })