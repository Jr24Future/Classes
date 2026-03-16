let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.classifySign', function() {
        it('should return "zero" for zero', function(done) {
            let result = testcode.classifySign(0);
            assert.strictEqual(result, 'zero');
            done();
        });

            })
})