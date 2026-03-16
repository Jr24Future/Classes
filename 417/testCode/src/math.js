// src/math.js

// add two numbers
function add(a, b) {
  return a + b;
}

// absolute difference
function absDiff(a, b) {
  return Math.abs(a - b);
}

// classify a number as 'negative', 'zero', or 'positive'
function classifySign(x) {
  if (x < 0) return 'negative';
  if (x === 0) return 'zero';
  return 'positive';
}

// simple average with a bug for empty arrays?
function average(nums) {
  if (!Array.isArray(nums)) throw new Error('nums must be an array');
  let sum = 0;
  for (const n of nums) sum += n;
  return sum / nums.length; // note: division by 0 if nums = []
}

module.exports = { add, absDiff, classifySign, average };
