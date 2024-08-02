module Decoder3to8(EN, Y, W);
 input [2:0]W;
 input EN;
 output [7:0]Y;
 reg[7:0]Y;
 always@(W or EN)
 begin
 if(EN)
 begin
 Y=8'd0;
 case(W)
 3'b000:Y[0]=1'b1;
 3'b001:Y[1]=1'b1;
 3'b010:Y[2]=1'b1;
 3'b011:Y[3]=1'b1;
 3'b100:Y[4]=1'b1;
 3'b101:Y[5]=1'b1;
 3'b110:Y[6]=1'b1;
 3'b111:Y[7]=1'b1;
 endcase
 end
 else
 Y=8'b0;
 end
 endmodule