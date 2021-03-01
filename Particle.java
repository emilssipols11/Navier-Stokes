package app;

import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Vertex;
import org.jsfml.system.Vector2f;

public class Particle {

    public Particle(){
    };


    public Particle(float positionX, float positionY){
        this.positionX=positionX;
        this.positionY=positionY;
        this.velocityX= 1;
        this.velocityY= 1;
        this.part[0]= new Vertex(new Vector2f(this.positionX, this.positionY));
    };


    public Particle(float positionX, float positionY, float velocityX, float velocityY){
        this.positionX=positionX;
        this.positionY=positionY;
        this.velocityX= velocityX;
        this.velocityY= velocityY;
        this.part[0]= new Vertex(new Vector2f(this.positionX, this.positionY));
    };

    public void setVelocityX(float velocityX){
      this.velocityX=velocityX;
    };
    public void setVelocityY(float velocityY){
        this.velocityY=velocityY;
    };
    public void setPositionX(float positionX){
        this.positionX = positionX;
    };
    public void setPositionY(float positionY){
       this.positionY = positionY;
    };

    public void move(){
        this.positionX +=(this.velocityX);
        this.positionY += (this.velocityY);
    };

    public void render(RenderWindow w, Field field, int t){

        
        this.part[0]= new Vertex(new Vector2f(this.positionX, this.positionY));
        w.draw(part, PrimitiveType.POINTS);
        if(positionX>=10 && positionX<=990 && positionY>=10 && positionY<=990){
            int x = (int)Math.floor(((positionX-10)*field.getSize())/980);
            int y = (int)Math.floor(((positionY-10)*field.getSize())/980);
            if (x>-1 && x<100 && y>-1 && y<100){
                velocityX = field.getCell(x,y,t).getDirection().getComponentX()*10;
                velocityY = field.getCell(x,y,t).getDirection().getComponentY()*10;
            }
        }
        this.move();

    }


    public float getVelocityX(){return this.velocityX;};
    public float getVelocityY(){return this.velocityY;};
    public float getVel(){return velocityX+velocityY;};


    private float velocityX;
    private float velocityY;
    private float positionX;
    private float positionY;
    private Vertex part[]=new Vertex[1];

}

