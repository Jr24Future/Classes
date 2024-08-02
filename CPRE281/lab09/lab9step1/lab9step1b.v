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
// CREATED		"Thu Nov  2 11:45:14 2023"

module lab9step1b(
	S,
	R,
	Q,
	QN
);


input wire	S;
input wire	R;
output wire	Q;
output wire	QN;

wire	SYNTHESIZED_WIRE_0;
wire	SYNTHESIZED_WIRE_1;
wire	SYNTHESIZED_WIRE_2;
wire	SYNTHESIZED_WIRE_3;

assign	Q = SYNTHESIZED_WIRE_3;
assign	QN = SYNTHESIZED_WIRE_0;



assign	SYNTHESIZED_WIRE_3 = ~(SYNTHESIZED_WIRE_0 & SYNTHESIZED_WIRE_1);

assign	SYNTHESIZED_WIRE_0 = ~(SYNTHESIZED_WIRE_2 & SYNTHESIZED_WIRE_3);

assign	SYNTHESIZED_WIRE_1 =  ~S;

assign	SYNTHESIZED_WIRE_2 =  ~R;


endmodule
