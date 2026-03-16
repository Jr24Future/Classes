let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.absDiff', function() {
        it('should return the absolute difference when the first number is smaller', function(done) {
            let result = testcode.absDiff(3, 5);
            assert.strictEqual(result, 2);
            done();
        });

            })
})