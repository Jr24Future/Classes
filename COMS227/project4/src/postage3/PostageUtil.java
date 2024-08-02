package postage3;

public class PostageUtil {
	public static double computePostage(double weight)
    {
        double cost =.47;
        if(weight>1)
            cost =cost+Math.ceil(weight-1)*.21;
        if(weight>3.5)
        	cost=cost+.47;
        return cost;
    }
}
