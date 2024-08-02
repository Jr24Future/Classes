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
// CREATED		"Thu Oct 26 12:20:53 2023"

module lab08(
	W0,
	W1,
	W2,
	W3,
	Xc3,
	Xc2,
	Xc1,
	S,
	F
);


input wire	W0;
input wire	W1;
input wire	W2;
input wire	W3;
input wire	Xc3;
input wire	Xc2;
input wire	Xc1;
input wire	[1:0] S;
output wire	[3:0] F;

wire	[3:0] F_ALTERA_SYNTHESIZED;





mux_4x1	b2v_inst(
	.W3(Xc1),
	.W2(W0),
	.W1(W1),
	.W0(W2),
	.S(S),
	.F(F_ALTERA_SYNTHESIZED[2]));


mux_4x1	b2v_inst1(
	.W3(Xc2),
	.W2(Xc1),
	.W1(W0),
	.W0(W1),
	.S(S),
	.F(F_ALTERA_SYNTHESIZED[1]));


mux_4x1	b2v_inst2(
	.W3(W0),
	.W2(W1),
	.W1(W2),
	.W0(W3),
	.S(S),
	.F(F_ALTERA_SYNTHESIZED[3]));


mux_4x1	b2v_inst3(
	.W3(Xc3),
	.W2(Xc2),
	.W1(Xc1),
	.W0(W0),
	.S(S),
	.F(F_ALTERA_SYNTHESIZED[0]));

assign	F = F_ALTERA_SYNTHESIZED;

endmodule
