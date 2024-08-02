module seven_seg_decoder_BUSOUT(OUTARR,INARR);
	input [3:0] INARR;
	output [6:0] OUTARR;
	reg A,B,C,D,E,F,G,H; 
	assign OUTARR[6]=G;
	assign OUTARR[5]=F;
	assign OUTARR[4]=E;
	assign OUTARR[3]=D;
	assign OUTARR[2]=C;
	assign OUTARR[1]=B;
	assign OUTARR[0]=A;
	
	wire X0=INARR[0];
	wire X1=INARR[1];
	wire X2=INARR[2];
	wire X3=INARR[3];	
	always @ (X0 or X1 or X2 or X3)
	begin
		case({X3,X2,X1,X0})
			4'b0000:{A,B,C,D,E,F,G}=7'b0000001;
			4'b0001:{A,B,C,D,E,F,G}=7'b1001111;
			4'b0010:{A,B,C,D,E,F,G}=7'b0010010;
			4'b0011:{A,B,C,D,E,F,G}=7'b0000110;
			4'b0100:{A,B,C,D,E,F,G}=7'b1001100;
			4'b0101:{A,B,C,D,E,F,G}=7'b0100100;
			4'b0110:{A,B,C,D,E,F,G}=7'b0100000;
			4'b0111:{A,B,C,D,E,F,G}=7'b0001111;
			4'b1000:{A,B,C,D,E,F,G}=7'b0000000;	
			4'b1001:{A,B,C,D,E,F,G}=7'b0000100;
			4'b1010:{A,B,C,D,E,F,G}=7'b0001000;
			4'b1011:{A,B,C,D,E,F,G}=7'b1100000;
			4'b1100:{A,B,C,D,E,F,G}=7'b0110001;
			4'b1101:{A,B,C,D,E,F,G}=7'b1000010;
			4'b1110:{A,B,C,D,E,F,G}=7'b0110000;
			4'b1111:{A,B,C,D,E,F,G}=7'b0111000;
		endcase
	end
endmodule