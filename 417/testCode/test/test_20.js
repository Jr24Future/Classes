let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.absDiff', function() {
        it('should return 0 when both numbers are equal', function(done) {
            let result = testcode.absDiff(4, 4);
            assert.strictEqual(result, 0);
            done();
        });

            })
})