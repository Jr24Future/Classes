let mocha = require('mocha');
let assert = require('assert');
let testcode = require('./testcode'); // Ensure the path to testcode is correct

describe('test testcode', function() {
    it('should throw an error if input is not an array', function(done) {
        assert.throws(() => {
            testcode.average('not an array');
        }, {
            name: 'Error',
            message: 'nums must be an array'
        });
        done();
    });
});