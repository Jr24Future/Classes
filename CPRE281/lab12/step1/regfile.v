module regfile(DATAP3, DATAP2, DATAP1, DATAP0, DATAQ3, DATAQ2, DATAQ1, DATAQ0,
 RP2, RP1, RP0, RQ2, RQ1, RQ0, WA2, WA1, WA0, LD_DATA, WR, CLRN, CLK);

 // address and control ports
 input RP2, RP1, RP0, RQ2, RQ1, RQ0, WA2, WA1, WA0, WR, CLRN, CLK;
 // input data port
 input [3:0] LD_DATA;
 // output data ports
 output DATAP3, DATAP2, DATAP1, DATAP0, DATAQ3, DATAQ2, DATAQ1, DATAQ0;
 
 wire [3:0] VALUE0, VALUE1, VALUE2, VALUE3, VALUE4, VALUE5, VALUE6, VALUE7;
 wire [3:0] DATAP, DATAQ;
 wire [7:0] Y;
 wire [2:0] W;
 assign W = {WA2, WA1, WA0};

 
 Decoder3to8 my_decoder(.EN(WR), .Y(Y), .W(W));

reg4b my_reg0(.IN(LD_DATA), .LOAD(Y[0]), .CLK(CLK), .OUT(VALUE0), .CLRN(CLRN));
reg4b my_reg1(.IN(LD_DATA), .LOAD(Y[1]), .CLK(CLK), .OUT(VALUE1), .CLRN(CLRN));
reg4b my_reg2(.IN(LD_DATA), .LOAD(Y[2]), .CLK(CLK), .OUT(VALUE2), .CLRN(CLRN));
reg4b my_reg3(.IN(LD_DATA), .LOAD(Y[3]), .CLK(CLK), .OUT(VALUE3), .CLRN(CLRN));
reg4b my_reg4(.IN(LD_DATA), .LOAD(Y[4]), .CLK(CLK), .OUT(VALUE4), .CLRN(CLRN));
reg4b my_reg5(.IN(LD_DATA), .LOAD(Y[5]), .CLK(CLK), .OUT(VALUE5), .CLRN(CLRN));
reg4b my_reg6(.IN(LD_DATA), .LOAD(Y[6]), .CLK(CLK), .OUT(VALUE6), .CLRN(CLRN));
reg4b my_reg7(.IN(LD_DATA), .LOAD(Y[7]), .CLK(CLK), .OUT(VALUE7), .CLRN(CLRN));

Mux8_4b my_muxP(.S2(RP2), .S1(RP1), .S0(RP0), .W0(VALUE0),
 .W1(VALUE1), .W2(VALUE2), .W3(VALUE3), .W4(VALUE4), .W5(VALUE5),
 .W6(VALUE6), .W7(VALUE7), .F(DATAP));
Mux8_4b my_muxQ(.S2(RQ2), .S1(RQ1), .S0(RQ0), .W0(VALUE0),
 .W1(VALUE1), .W2(VALUE2), .W3(VALUE3), .W4(VALUE4), .W5(VALUE5),
 .W6(VALUE6), .W7(VALUE7), .F(DATAQ));
 
 assign DATAP3 = DATAP[3];
 assign DATAP2 = DATAP[2];
 assign DATAP1 = DATAP[1];
 assign DATAP0 = DATAP[0];
 assign DATAQ3 = DATAQ[3];
 assign DATAQ2 = DATAQ[2];
 assign DATAQ1 = DATAQ[1];
 assign DATAQ0 = DATAQ[0];
 endmodule