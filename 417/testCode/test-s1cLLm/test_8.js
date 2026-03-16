let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('should return 10 when adding 7 and 3', function(done) {
        let result = testcode.add(7, 3);
        assert.strictEqual(result, 10);
        done();
    });

    })