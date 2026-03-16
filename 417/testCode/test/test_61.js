let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('should throw an error if input is not an array', function(done) {
        assert.throws(() => {
            testcode.average('not an array');
        }, /nums must be an array/);
        done();
    });

    })