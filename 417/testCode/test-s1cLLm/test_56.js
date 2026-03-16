let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.average', function() {
        it('should handle negative numbers', function(done) {
            let nums = [-1, -2, -3, -4, -5];
            let result = testcode.average(nums);
            assert.strictEqual(result, -3);
            done();
        });

            })
})