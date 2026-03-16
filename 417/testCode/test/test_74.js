let mocha = require('mocha');
let assert = require('assert');
let testcode = require('..');

describe('test suite', function() {
    it('test case', function(done) {
        // testcode.js
module.exports.average = function(nums) {
    if (nums.length === 0) {
        return 0; // Return 0 for an empty array
    }
    let sum = nums.reduce((acc, num) => acc + num, 0);
    return sum / nums.length;
};
    })
})