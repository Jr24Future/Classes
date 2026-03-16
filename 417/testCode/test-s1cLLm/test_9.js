let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('should return 1 when adding 0 and 1', function(done) {
        let result = testcode.add(0, 1);
        assert.strictEqual(result, 1);
        done();
    });
});