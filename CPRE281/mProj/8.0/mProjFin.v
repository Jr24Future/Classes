// Copyright (C) 2023  Intel Corporation. All rights reserved.
// Your use of Intel Corporation's design tools, logic functions 
// and other software and tools, and any partner logic 
// functions, and any output files from any of the foregoing 
// (including device programming or simulation files), and any 
// associated documentation or information are expressly subject 
// to the terms and conditions of the Intel Program License 
// Subscription Agreement, the Intel Quartus Prime License Agreement,
// the Intel FPGA IP License Agreement, or other applicable license
// agreement, including, without limitation, that your use is for
// the sole purpose of programming logic devices manufactured by
// Intel and sold by Intel or its authorized distributors.  Please
// refer to the applicable agreement for further details, at
// https://fpgasoftware.intel.com/eula.

// PROGRAM		"Quartus Prime"
// VERSION		"Version 22.1std.1 Build 917 02/14/2023 SC Standard Edition"
// CREATED		"Thu Oct  5 13:44:02 2023"

module mProjFin(
	W,
	X,
	Y,
	Z,
	P
);


input wire	W;
input wire	X;
input wire	Y;
input wire	Z;
output wire	P;

wire	SYNTHESIZED_WIRE_0;
wire	SYNTHESIZED_WIRE_1;
wire	SYNTHESIZED_WIRE_2;
wire	SYNTHESIZED_WIRE_3;
wire	SYNTHESIZED_WIRE_4;




assign	P = SYNTHESIZED_WIRE_0 | SYNTHESIZED_WIRE_1;


mProj	b2v_inst1(
	.W(W),
	.X(X),
	.Y(Y),
	.Z(Z),
	.B(SYNTHESIZED_WIRE_2));

assign	SYNTHESIZED_WIRE_3 = ~(W & Z & X & Y);

assign	SYNTHESIZED_WIRE_1 = SYNTHESIZED_WIRE_2 & SYNTHESIZED_WIRE_3;

assign	SYNTHESIZED_WIRE_0 = SYNTHESIZED_WIRE_4 & Y & Z;

assign	SYNTHESIZED_WIRE_4 =  ~W;


endmodule
