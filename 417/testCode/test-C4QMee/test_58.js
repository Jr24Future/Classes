let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.average', function() {
        it('should handle an array with a single number', function(done) {
            let nums = [42];
            let result = testcode.average(nums);
            assert.strictEqual(result, 42);
            done();
        });

            })
})