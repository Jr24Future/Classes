let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.average', function() {
        it('should handle mixed positive and negative numbers', function(done) {
            let nums = [-1, 0, 1, 2, 3];
            let result = testcode.average(nums);
            assert.strictEqual(result, 1);
            done();
        });
    });
});