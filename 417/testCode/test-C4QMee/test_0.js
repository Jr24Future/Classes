let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.add', function() {
        it('should return the sum of two positive numbers', function(done) {
            let result = testcode.add(2, 3);
            assert.strictEqual(result, 5);
            done();
        });

            })
})