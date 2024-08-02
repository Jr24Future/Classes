module mProj(P,W,X,Y,Z);
input W,X,Y,Z;
output P;

assign P=(~W&~X&Y)|(~X&Y&Z)|(~W&X&Z)|(X&~Y&Z);

endmodule