module adder_full(Ci, X, Y, S, Co);
	output S, Co;
	input Ci, X, Y;
	
	assign S = X ^ Y ^ Ci;
	assign Co = (X & Y) | ((X ^ Y) & Ci);
endmodule

module adder_4bit(Cin, X, Y, S, Cout);
	output [3:0] S;
	output Cout;
	input [3:0] X, Y;
	input Cin;
	
	wire [2:0] carries;
	
	adder_full a1(Cin,        X[0], Y[0], S[0], carries[0]);
	adder_full a2(carries[0], X[1], Y[1], S[1], carries[1]);
	adder_full a3(carries[1], X[2], Y[2], S[2], carries[2]);
	adder_full a4(carries[2], X[3], Y[3], S[3], Cout);
	
endmodule
			