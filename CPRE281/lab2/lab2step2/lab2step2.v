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
// CREATED		"Thu Sep  7 12:15:09 2023"

module lab2step2(
	A,
	B,
	C,
	F
);


input wire	A;
input wire	B;
input wire	C;
output wire	F;

wire	SYNTHESIZED_WIRE_15;
wire	SYNTHESIZED_WIRE_16;
wire	SYNTHESIZED_WIRE_17;
wire	SYNTHESIZED_WIRE_9;
wire	SYNTHESIZED_WIRE_10;
wire	SYNTHESIZED_WIRE_11;
wire	SYNTHESIZED_WIRE_12;
wire	SYNTHESIZED_WIRE_13;
wire	SYNTHESIZED_WIRE_14;




assign	SYNTHESIZED_WIRE_15 =  ~A;

assign	SYNTHESIZED_WIRE_16 =  ~B;

assign	SYNTHESIZED_WIRE_17 =  ~C;

assign	SYNTHESIZED_WIRE_9 = SYNTHESIZED_WIRE_15 & SYNTHESIZED_WIRE_16 & SYNTHESIZED_WIRE_17;

assign	SYNTHESIZED_WIRE_10 = SYNTHESIZED_WIRE_15 & B & C;

assign	SYNTHESIZED_WIRE_12 = A & SYNTHESIZED_WIRE_16 & SYNTHESIZED_WIRE_17;

assign	SYNTHESIZED_WIRE_11 = SYNTHESIZED_WIRE_15 & SYNTHESIZED_WIRE_16 & C;

assign	SYNTHESIZED_WIRE_13 = A & B & SYNTHESIZED_WIRE_17;

assign	SYNTHESIZED_WIRE_14 = A & B & C;

assign	F = SYNTHESIZED_WIRE_9 | SYNTHESIZED_WIRE_10 | SYNTHESIZED_WIRE_11 | SYNTHESIZED_WIRE_12 | SYNTHESIZED_WIRE_13 | SYNTHESIZED_WIRE_14;


endmodule
