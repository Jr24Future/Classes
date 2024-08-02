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
// CREATED		"Thu Nov  9 12:38:00 2023"

module lab10step3a(
	CLk,
	CLRN,
	Q
);


input wire	CLk;
input wire	CLRN;
output wire	[3:0] Q;

reg	[3:0] Q_ALTERA_SYNTHESIZED;
wire	SYNTHESIZED_WIRE_12;
wire	SYNTHESIZED_WIRE_4;
wire	SYNTHESIZED_WIRE_7;
wire	SYNTHESIZED_WIRE_11;

assign	SYNTHESIZED_WIRE_12 = 1;




always@(posedge CLk or negedge CLRN)
begin
if (!CLRN)
	begin
	Q_ALTERA_SYNTHESIZED[0] <= 0;
	end
else
	begin
	Q_ALTERA_SYNTHESIZED[0] <= ~Q_ALTERA_SYNTHESIZED[0] & SYNTHESIZED_WIRE_12 | Q_ALTERA_SYNTHESIZED[0] & ~SYNTHESIZED_WIRE_12;
	end
end


always@(posedge SYNTHESIZED_WIRE_4 or negedge CLRN)
begin
if (!CLRN)
	begin
	Q_ALTERA_SYNTHESIZED[1] <= 0;
	end
else
	begin
	Q_ALTERA_SYNTHESIZED[1] <= ~Q_ALTERA_SYNTHESIZED[1] & SYNTHESIZED_WIRE_12 | Q_ALTERA_SYNTHESIZED[1] & ~SYNTHESIZED_WIRE_12;
	end
end


always@(posedge SYNTHESIZED_WIRE_7 or negedge CLRN)
begin
if (!CLRN)
	begin
	Q_ALTERA_SYNTHESIZED[2] <= 0;
	end
else
	begin
	Q_ALTERA_SYNTHESIZED[2] <= ~Q_ALTERA_SYNTHESIZED[2] & SYNTHESIZED_WIRE_12 | Q_ALTERA_SYNTHESIZED[2] & ~SYNTHESIZED_WIRE_12;
	end
end


always@(posedge SYNTHESIZED_WIRE_11 or negedge CLRN or negedge SYNTHESIZED_WIRE_12)
begin
if (!CLRN)
	begin
	Q_ALTERA_SYNTHESIZED[3] <= 0;
	end
else
	begin
if (!SYNTHESIZED_WIRE_12)
	begin
	Q_ALTERA_SYNTHESIZED[3] <= 1;
	end
else
	begin
	Q_ALTERA_SYNTHESIZED[3] <= ~Q_ALTERA_SYNTHESIZED[3] & SYNTHESIZED_WIRE_12 | Q_ALTERA_SYNTHESIZED[3] & ~SYNTHESIZED_WIRE_12;
	end
	end
end


assign	SYNTHESIZED_WIRE_4 =  ~Q_ALTERA_SYNTHESIZED[0];

assign	SYNTHESIZED_WIRE_7 =  ~Q_ALTERA_SYNTHESIZED[1];

assign	SYNTHESIZED_WIRE_11 =  ~Q_ALTERA_SYNTHESIZED[2];

assign	Q = Q_ALTERA_SYNTHESIZED;

endmodule
