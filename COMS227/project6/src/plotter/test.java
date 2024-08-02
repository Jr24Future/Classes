import java.util.*;
public class test {
    public static void main(String[] args) {
        System.out.println("enter the text: ");
       Scanner cti = new Scanner(System.in);     
       String a = cti.nextLine();
        System.out.println("enter number of positions= ");
        int b = cti.nextInt();
       String firstPart = a.substring(0,b);   // line 1
       b--;
       a = a.substring(b); 
       String m = a + firstPart ;             // line 2
        System.out.println("now it is "+ m);
    }
    
}