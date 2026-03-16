let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.classifySign', function() {
        it('should return "positive" for positive numbers', function(done) {
            assert.strictEqual(testcode.classifySign(5), 'positive');
            assert.strictEqual(testcode.classifySign(1), 'positive');
            assert.strictEqual(testcode.classifySign(100), 'positive');
            done();
        });

            })
})