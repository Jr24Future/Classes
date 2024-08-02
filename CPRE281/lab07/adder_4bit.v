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
// CREATED		"Thu Oct 19 11:05:05 2023"

module adder_4bit(
	Y3,
	X0,
	X3,
	Y2,
	X2,
	Y1,
	X1,
	Y0,
	cin,
	S3,
	S2,
	S1,
	S0,
	overFlow,
	cout
);


input wire	Y3;
input wire	X0;
input wire	X3;
input wire	Y2;
input wire	X2;
input wire	Y1;
input wire	X1;
input wire	Y0;
input wire	cin;
output wire	S3;
output wire	S2;
output wire	S1;
output wire	S0;
output wire	overFlow;
output wire	cout;

wire	N1;
wire	N2;
wire	N3;
wire	SYNTHESIZED_WIRE_0;

assign	cout = SYNTHESIZED_WIRE_0;




lab07	b2v_inst(
	.Cin(N2),
	.X(Y2),
	.Y(X2),
	.S(S2),
	.Cout(N3));


lab07	b2v_inst1(
	.Cin(N1),
	.X(Y1),
	.Y(X1),
	.S(S1),
	.Cout(N2));


lab07	b2v_inst2(
	.Cin(cin),
	.X(Y0),
	.Y(X0),
	.S(S0),
	.Cout(N1));


lab07	b2v_inst3(
	.Cin(N3),
	.X(Y3),
	.Y(X3),
	.S(S3),
	.Cout(SYNTHESIZED_WIRE_0));

assign	overFlow = SYNTHESIZED_WIRE_0 ^ cin;


endmodule
