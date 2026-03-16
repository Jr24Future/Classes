let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('should return "zero" for zero', function(done) {
        assert.strictEqual(testcode.classifySign(0), 'zero');
        done();
    });

    })