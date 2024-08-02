module mProj(B,W,X,Y,Z);
input W,X,Y,Z;
output B;

assign B=(W&Y&Z)|(X&~Y&Z)|(~W&~X&Y&~Z);

endmodule