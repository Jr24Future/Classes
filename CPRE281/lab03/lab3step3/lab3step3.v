module lab3step3(A,B,C,D,F);
input A,B,C,D;
output F;

assign F = (~A&C&B)|(~A&C&D)|(A&~C&~D)|(A&~C&~B);
endmodule