let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.classifySign', function() {
        it('should return "positive" for positive numbers', function(done) {
            let result = testcode.classifySign(5);
            assert.strictEqual(result, 'positive');
            done();
        });

            })
})