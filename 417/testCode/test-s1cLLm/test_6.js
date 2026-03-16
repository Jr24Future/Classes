let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test testcode', function() {
    it('should return 0 when adding -1 and 1', function(done) {
        let result = testcode.add(-1, 1);
        assert.strictEqual(result, 0);
        done();
    });

    })