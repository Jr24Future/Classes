let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.add', function() {
        it('should return the same number when adding zero', function(done) {
            let result1 = testcode.add(0, 5);
            let result2 = testcode.add(5, 0);
            assert.strictEqual(result1, 5);
            assert.strictEqual(result2, 5);
            done();
        });

            })
})