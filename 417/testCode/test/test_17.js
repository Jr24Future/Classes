let mocha = require('mocha');
let assert = require('assert');

// Mock implementation of testcode module
let testcode = {
    add: function(a, b) {
        return a + b;
    }
};

describe('test testcode', function() {
    describe('testcode.add', function() {
        it('should return the sum of two negative numbers', function(done) {
            let result = testcode.add(-2, -3);
            assert.strictEqual(result, -5);
            done();
        });
    });
});