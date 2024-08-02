module DMEM_Hardcoded(b0I,b1I,b2I,b3I,b4I,b5I,b6I,b7I,b8I,b9I,b10I,b11I,b12I,b13I,b14I,b15I);
//DMEM testing v1.0 1/19/2019
//author: Kyung-Tae Kim
output [7:0] b0I;
output [7:0] b1I;
output [7:0] b2I;
output [7:0] b3I;
output [7:0] b4I;
output [7:0] b5I;
output [7:0] b6I;
output [7:0] b7I;
output [7:0] b8I;
output [7:0] b9I;
output [7:0] b10I;
output [7:0] b11I;
output [7:0] b12I;
output [7:0] b13I;
output [7:0] b14I;
output [7:0] b15I;

//BubbleSort v2 3/10/19
assign b0I[7:0]  = 8'b00000111; //array 0
assign b1I[7:0]  = 8'b00000110; 
assign b2I[7:0]  = 8'b00000101;
assign b3I[7:0]  = 8'b00000100;
assign b4I[7:0]  = 8'b00000011; //[last]
assign b5I[7:0]  = 8'b00000000;
assign b6I[7:0]  = 8'b00000000; 
assign b7I[7:0]  = 8'b00000000; 
assign b8I[7:0]  = 8'b00000000; 
assign b9I[7:0]  = 8'b00000000; 
assign b10I[7:0] = 8'b00000000; 
assign b11I[7:0] = 8'b00000000; 
assign b12I[7:0] = 8'b00000000;
assign b13I[7:0] = 8'b00000000;
assign b14I[7:0] = 8'b00000000;
assign b15I[7:0] = 8'b00000000;

/*linear search?
assign b0I[7:0]  = 8'b00000101; //array 0
assign b1I[7:0]  = 8'b00000010; 
assign b2I[7:0]  = 8'b00000111;
assign b3I[7:0]  = 8'b00000011;
assign b4I[7:0]  = 8'b00000110; 
assign b5I[7:0]  = 8'b00000001; //array 5
assign b6I[7:0]  = 8'b00000000; //found
assign b7I[7:0]  = 8'b00000000; //index
assign b8I[7:0]  = 8'b00001000; //key
assign b9I[7:0]  = 8'b00000110; //N
assign b10I[7:0] = 8'b00000000; 
assign b11I[7:0] = 8'b00000000; 
assign b12I[7:0] = 8'b00000000;
assign b13I[7:0] = 8'b00000000;
assign b14I[7:0] = 8'b00000000;
assign b15I[7:0] = 8'b00000000;

/*binary search values
assign b0I[7:0]  = 8'b00000001; //array 0
assign b1I[7:0]  = 8'b00000010; 
assign b2I[7:0]  = 8'b00000100;
assign b3I[7:0]  = 8'b00000101;
assign b4I[7:0]  = 8'b00000111; 
assign b5I[7:0]  = 8'b00001000;
assign b6I[7:0]  = 8'b00001001; //array 6
assign b7I[7:0]  = 8'b00000000; //found
assign b8I[7:0]  = 8'b00000000; //low
assign b9I[7:0]  = 8'b00000110; //high
assign b10I[7:0] = 8'b00000000; //mid
assign b11I[7:0] = 8'b00000101; //key
assign b12I[7:0] = 8'b00000000;
assign b13I[7:0] = 8'b00000000;
assign b14I[7:0] = 8'b00000000;
assign b15I[7:0] = 8'b00000000;
*/

/* bubble sort values
assign b0I[7:0]  = 8'b00000111; 
assign b1I[7:0]  = 8'b00000110;
assign b2I[7:0]  = 8'b00000101;
assign b3I[7:0]  = 8'b00000100;
assign b4I[7:0]  = 8'b00000011;
assign b5I[7:0]  = 8'b00000010;
assign b6I[7:0]  = 8'b00000001;
assign b7I[7:0]  = 8'b00000000;
assign b8I[7:0]  = 8'b00000111;
assign b9I[7:0]  = 8'b00000000;
assign b10I[7:0] = 8'b00000000;
assign b11I[7:0] = 8'b00000000;
assign b12I[7:0] = 8'b00000000;
assign b13I[7:0] = 8'b00000000;
assign b14I[7:0] = 8'b00000000;
assign b15I[7:0] = 8'b00000000;
*/
/*
assign b15I[7:0]  = 8'b00000000; 
assign b14I[7:0]  = 8'b00000001;
assign b13I[7:0]  = 8'b00000010;
assign b12I[7:0]  = 8'b00000011;
assign b11I[7:0]  = 8'b00000100;
assign b10I[7:0]  = 8'b00000101;
assign b9I[7:0]  = 8'b00000110;
assign b8I[7:0]  = 8'b00000111;
assign b7I[7:0]  = 8'b00001000;
assign b6I[7:0]  = 8'b00001001;
assign b5I[7:0] = 8'b00001010;
assign b4I[7:0] = 8'b00001011;
assign b3I[7:0] = 8'b00001100;
assign b2I[7:0] = 8'b00001101;
assign b1I[7:0] = 8'b00001110;
assign b0I[7:0] = 8'b00001111;
*/

endmodule