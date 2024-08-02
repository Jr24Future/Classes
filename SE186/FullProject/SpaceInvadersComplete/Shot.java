import java.io.File;
import java.net.URL;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;

public class Shot extends SpaceCharacter
{

    public Shot()
    {
        super();
    }

    boolean goUp;

    public Shot(int x, int y, int w, int h, int s, String u)
    {
        super(x, y, w, h, s, "shot.png");
        setY(600);
        goUp = false;
    }

    public  void move(int d){

        if(getY()<0){
            goUp=false;
            setY(600);
        }

        if(goUp)
            setY(getY()-getSpeed());
    }

    public void draw(Graphics window){
        window.drawImage(getImage(),getX(),getY(),getWidth(),getHeight(),null);
    }


    public void setLeftRight(int d){
        if(d==37){
            moveLeft = true;
        }

        if(d==39){
            moveRight = true;
        }

    }

    public void stop(){
        moveLeft=false;
        moveRight=false;

    }

}
