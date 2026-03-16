let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('should return the sum of a number and zero', function(done) {
        let result = testcode.add(7, 0);
        assert.strictEqual(result, 7);
        done();
    });

    })