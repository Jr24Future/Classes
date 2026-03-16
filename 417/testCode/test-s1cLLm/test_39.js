let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.classifySign', function() {
        it('should return "negative" for negative numbers', function(done) {
            assert.strictEqual(testcode.classifySign(-5), 'negative');
            assert.strictEqual(testcode.classifySign(-1), 'negative');
            assert.strictEqual(testcode.classifySign(-100), 'negative');
            done();
        });

            })
})