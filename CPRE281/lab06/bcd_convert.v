module bcd_convert(S, N2, N1);
input [0:4]S;
output [0:3]N2,N1;
assign N2={
			(~S[4]&S[3]&S[1])|(~S[4]&S[3]&S[2])|(S[3]&S[2]&S[1])|(S[4]&~S[3]&~S[2]),
			(S[4]&S[2])|(S[4]&S[3]),
			1'b0,
			1'b0,
};
assign N1={
			S[0],
			(~S[4]&~S[3]&S[1])|(~S[3]&S[2]&S[1])|(~S[4]&S[3]&S[2]&~S[1])|(S[4]&~S[3]&~S[2]&~S[1])|(S[4]&S[3]&~S[2]&S[1]),
			(~S[4]&~S[3]&S[2])|(~S[4]&S[2]&S[1])|(S[4]&S[3]&~S[2])|(S[4]&~S[2]&~S[1]),
			(S[3]&~S[2]&~S[1])|(S[4]&~S[3]&~S[2]&S[1]),
};
endmodule