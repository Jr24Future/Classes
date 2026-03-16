let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.absDiff', function() {
        it('should return the absolute difference of two positive numbers', function(done) {
            let result = testcode.absDiff(5, 3);
            assert.strictEqual(result, 2);
            done();
        });

            })
})