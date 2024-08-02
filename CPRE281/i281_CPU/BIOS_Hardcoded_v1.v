module BIOS_Hardcoded_v1(b0,b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15);
//BIOS v1.0 1/3/2019
//author: Alexander Stoytchev
output [15:0] b0;
output [15:0] b1;
output [15:0] b2;
output [15:0] b3;
output [15:0] b4;
output [15:0] b5;
output [15:0] b6;
output [15:0] b7;
output [15:0] b8;
output [15:0] b9;
output [15:0] b10;
output [15:0] b11;
output [15:0] b12;
output [15:0] b13;
output [15:0] b14;
output [15:0] b15;

assign b0[15:0] = 16'b0101110000010000;
assign b1[15:0] = 16'b0111001100000000;
assign b2[15:0] = 16'b1110000000000001;
assign b3[15:0] = 16'b1100000000000011;
assign b4[15:0] = 16'b0001000100000000;
assign b5[15:0] = 16'b0101000000000001;
assign b6[15:0] = 16'b1100000011111010;
assign b7[15:0] = 16'b0101010000100000;
assign b8[15:0] = 16'b0101100001000000;
assign b9[15:0] = 16'b0111011000000000;
assign b10[15:0] = 16'b1110000000000001;
assign b11[15:0] = 16'b1100000000010011;
assign b12[15:0] = 16'b0001011100000000;
assign b13[15:0] = 16'b0101010000000001;
assign b14[15:0] = 16'b1100000011111010;
assign b15[15:0] = 16'b0000000000000000;

endmodule