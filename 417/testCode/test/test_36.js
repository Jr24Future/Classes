let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.classifySign', function() {
        it('should return "negative" for negative numbers', function(done) {
            let result = testcode.classifySign(-3);
            assert.strictEqual(result, 'negative');
            done();
        });

            })
})