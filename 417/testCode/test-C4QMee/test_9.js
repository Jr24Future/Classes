let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('should return the sum of two zeros', function(done) {
        let result = testcode.add(0, 0);
        assert.strictEqual(result, 0);
        done();
    });

    })