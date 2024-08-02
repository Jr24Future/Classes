module lab3step1(C,G,W,A);
		input C,G,W;
		output A;
		
		not(Cn,C);
		not(Gn,G);
		not(Wn,W);
		or(x,Cn,G,Wn);
		or(y,C,Gn,W);
		and(A,x,y);
		
endmodule