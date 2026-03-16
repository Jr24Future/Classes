let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('should return the sum of two floating point numbers', function(done) {
        let result = testcode.add(1.5, 2.5);
        assert.strictEqual(result, 4.0);
        done();
    });
});