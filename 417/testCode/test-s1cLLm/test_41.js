let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.classifySign', function() {
        it('should handle non-numeric inputs gracefully', function(done) {
            assert.strictEqual(testcode.classifySign('string'), 'invalid');
            assert.strictEqual(testcode.classifySign(null), 'invalid');
            assert.strictEqual(testcode.classifySign(undefined), 'invalid');
            done();
        });
    });
});