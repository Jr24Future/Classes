let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.average', function() {
        it('should return the average of an array of numbers', function(done) {
            let nums = [1, 2, 3, 4, 5];
            let result = testcode.average(nums);
            assert.strictEqual(result, 3);
            done();
        });

            })
})