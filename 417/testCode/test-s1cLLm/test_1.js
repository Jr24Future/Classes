let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.add', function() {
        it('should return the sum of a positive and a negative number', function(done) {
            let result = testcode.add(5, -3);
            assert.strictEqual(result, 2);
            done();
        });

            })
})