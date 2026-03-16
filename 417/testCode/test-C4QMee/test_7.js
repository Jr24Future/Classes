let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('should return the sum of two negative numbers', function(done) {
        let result = testcode.add(-2, -3);
        assert.strictEqual(result, -5);
        done();
    });

    })