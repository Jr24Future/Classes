module mux_4x1(W3,W2,W1,W0,S,F);
input W0,W1,W2,W3;
input [1:0]S;
output F;

assign F=S[1]?(S[0]?W3:W2):(S[0]?W1:W0);

endmodule