let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.average', function() {
        it('should return 0 for an empty array', function(done) {
            let nums = [];
            let result = testcode.average(nums);
            assert.strictEqual(result, 0);
            done();
        });

            })
})