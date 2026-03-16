let mocha = require('mocha');
let assert = require('assert');
let testcode = require('./testcode'); // Ensure the correct path to the testcode module

describe('test testcode', function() {
    it('should return 5 when adding 2 and 3', function(done) {
        let result = testcode.add(2, 3);
        assert.strictEqual(result, 5);
        done();
    });
});