let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.absDiff', function() {
        it('should return the absolute difference when one number is negative', function(done) {
            let result = testcode.absDiff(-3, 2);
            assert.strictEqual(result, 5);
            done();
        });

            })
})