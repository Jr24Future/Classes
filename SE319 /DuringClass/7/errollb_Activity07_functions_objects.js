// Author: Erroll Barker
// ISU Netid : errollb@iastate.edu
//Date : February 14th, 2024


console.log(~~1);
let n1 = 11;
let n2 = 10;
const maxOfTwo = (n1, n2) => Math.max(n1, n2);
console.log(`The max between ${n1} and ${n2} is :`, maxOfTwo(n1,n2));


console.log(~~2);
let arr = [10,11,1024,125,9,201];
const maxOfArray = (arr) => arr.reduce((a, b) => (a > b ? a : b));
console.log(maxOfArray(arr));


console.log(~~3); 
const movie = {
    title : 'Some movie',
    releaseYear: 2018,
    rating: 4.5,
    director: 'Steven Spielberg'
    };
    function showProperties(movie){
        for(let key in movie) {
            console.log(`${key}: ${movie[key]}`);
        }
    }
    showProperties(movie);


    console.log(~~4);
    const circle = {
        r: 2,
        area: function() {
            return Math.PI * Math.pow(this.r, 2);
        },
    };
    console.log(circle.area());

    console.log(~~5);
    const circle2 = {
        R: 2,
        area : function() {
            return Math.PI * Math.pow(this.R, 2);
        },
        get R_Value(){
            return this.R
        },
    };
    console.log(`Area with ${circle2.R_Value}:`, circle2.area());
    circle2.R_Value=3;
    console.log(`Area with ${circle2.R_Value}:`, circle2.area());





    console.log(~~6); //---------------------
    let circle3 = {
        radius: 2,
        getRadiusValue: function() {
          return this.radius;
        },
        setRadiusValue: function(value) {
          this.radius = value;
        },
        area: function() {
          return Math.PI * this.radius * this.radius;
        }
      };
      
      console.log(`Area with ${circle3.getRadiusValue()} :`, circle3.area());
      circle3.setRadiusValue(3);
      console.log(`Area with ${circle3.getRadiusValue()} :`, circle3.area());