let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    describe('testcode.add', function() {
        it('should return zero when adding two zeros', function(done) {
            let result = testcode.add(0, 0);
            assert.strictEqual(result, 0);
            done();
        });
    });
});