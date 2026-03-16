let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.absDiff', function() {
        it('should return 0 when both numbers are the same', function(done) {
            let result = testcode.absDiff(5, 5);
            assert.strictEqual(result, 0);
            done();
        });
    });
});