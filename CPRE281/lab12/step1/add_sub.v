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
// CREATED		"Thu Oct 19 11:22:21 2023"

module add_sub(
	X3,
	Y3,
	X2,
	Y2,
	X1,
	Y1,
	X0,
	Y0,
	control,
	Ov,
	cout,
	S3,
	S2,
	S0,
	S1
);


input wire	X3;
input wire	Y3;
input wire	X2;
input wire	Y2;
input wire	X1;
input wire	Y1;
input wire	X0;
input wire	Y0;
input wire	control;
output wire	Ov;
output wire	cout;
output wire	S3;
output wire	S2;
output wire	S0;
output wire	S1;

wire	SYNTHESIZED_WIRE_0;
wire	SYNTHESIZED_WIRE_1;
wire	SYNTHESIZED_WIRE_2;
wire	SYNTHESIZED_WIRE_3;




assign	SYNTHESIZED_WIRE_0 = control ^ Y3;

assign	SYNTHESIZED_WIRE_1 = control ^ Y2;

assign	SYNTHESIZED_WIRE_2 = control ^ Y1;

assign	SYNTHESIZED_WIRE_3 = control ^ Y0;


adder_4bit	b2v_inst7(
	.Y3(SYNTHESIZED_WIRE_0),
	.X3(X3),
	.Y2(SYNTHESIZED_WIRE_1),
	.X2(X2),
	.Y1(SYNTHESIZED_WIRE_2),
	.X1(X1),
	.cin(control),
	.Y0(SYNTHESIZED_WIRE_3),
	.X0(X0),
	.S3(S3),
	.cout(cout),
	.overFlow(Ov),
	.S2(S2),
	.S1(S1),
	.S0(S0));


endmodule
