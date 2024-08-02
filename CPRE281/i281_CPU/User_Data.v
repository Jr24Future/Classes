module User_Data(b0I,b1I,b2I,b3I,b4I,b5I,b6I,b7I,b8I,b9I,b10I,b11I,b12I,b13I,b14I,b15I);
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

//FileName: ../../Downloads/pong64_high.txt
assign b0I[7:0] = 8'b00000000; //empty[0]
assign b1I[7:0] = 8'b00000000; //empty[1]
assign b2I[7:0] = 8'b00000000; //empty[2]
assign b3I[7:0] = 8'b00000000; //empty[3]
assign b4I[7:0] = 8'b01000000; //display[0]
assign b5I[7:0] = 8'b00000000; //display[1]
assign b6I[7:0] = 8'b00000000; //display[2]
assign b7I[7:0] = 8'b00000100; //display[3]
assign b8I[7:0] = 8'b01000000; //shape[0]
assign b9I[7:0] = 8'b00001000; //shape[1]
assign b10I[7:0] = 8'b01000000; //shape[2]
assign b11I[7:0] = 8'b00000001; //shape[3]
assign b12I[7:0] = 8'b00000001; //incDec
assign b13I[7:0] = 8'b00000000; //switch[0]
assign b14I[7:0] = 8'b00000000; //switch[1]
assign b15I[7:0] = 8'b00000011; //switch[2]
endmodule
