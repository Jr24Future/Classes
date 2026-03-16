let mocha = require('mocha');
let assert = require('assert');

// Implementing the average function
let testcode = {
    average: function(arr) {
        if (arr.length === 0) return 0; // Handle empty array case
        const sum = arr.reduce((acc, num) => acc + num, 0);
        return sum / arr.length;
    }
};

describe('test testcode', function() {
    it('should handle an array with a single number', function(done) {
        const result = testcode.average([42]);
        assert.strictEqual(result, 42);
        done();
    });
});