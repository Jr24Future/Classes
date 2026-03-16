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
    it('should return the average of an array of numbers', function(done) {
        const result = testcode.average([1, 2, 3, 4, 5]);
        assert.strictEqual(result, 3);
        done();
    });
});