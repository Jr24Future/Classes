import java.io.File;
import java.net.URL;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;

public class Ship extends SpaceCharacter
{

    public Ship()
    {
        super();
    }

    public Ship(int x, int y, int w, int h, int s, String u)
    {
        super(x, y, w, h, s, "player.png");
    }

    public void draw(Graphics window){
        window.drawImage(getImage(),getX(),getY(),getWidth(),getHeight(),null);
    }

    public  void move(int d){
        if(moveLeft)
            setX(getX()-getSpeed());

        if(moveRight){
            setX(getX()+getSpeed());
        }

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
