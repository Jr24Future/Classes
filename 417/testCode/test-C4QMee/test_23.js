let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.absDiff', function() {
        it('should return the absolute difference when both numbers are negative', function(done) {
            let result = testcode.absDiff(-7, -2);
            assert.strictEqual(result, 5);
            done();
        });
    });
});