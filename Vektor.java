package app;

import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Vertex;
import org.jsfml.system.Vector2f;

public class Vektor {
    private float originX;
    private float originY;
    private float componentX;
    private float componentY;
    private Vertex[] triangle;



    public Vektor(float componentX, float componentY){
        triangle = new Vertex[3];
        this.componentX = componentX;
        this.componentY = componentY;
        this.originX = 0;
        this.originY = 0;
    }

    public Vektor(float componentX, float componentY, float originX, float originY){
        triangle = new Vertex[3];
        this.componentX = componentX;
        this.componentY = componentY;
        this.originX = originX;
        this.originY = originY;
    }


    public void setComponentX(float componentX) {
        this.componentX = componentX;
    }
    public float getComponentX(){
        return this.componentX;
    }

    public float getComponentY(){
        return this.componentY;
    }

    public void setComponentY(float componentY){
        this.componentY = componentY;
    }
    public float getLenght(){
        return (float)Math.sqrt(Math.pow(this.componentX, 2) + Math.pow(this.componentY,2) );
    }

    public void normalize(){
        float tempx = (this.componentX)/this.getLenght();
        float tempy = (this.componentY)/this.getLenght();
        this.componentX = 20*tempx;
        this.componentY = 20*tempy;
    }


    public void render(RenderWindow w){
        Vertex[] arr= new Vertex[2];
        arr[0] = new Vertex(new Vector2f(this.originX, this.originY));
        arr[1] = new Vertex(new Vector2f(this.originX + this.componentX, this.originY + this.componentY));

        this.triangle[0] = new Vertex(new Vector2f( this.originX + this.componentX + (new Point(this.componentY, (-1)*this.componentX).getX()*0.1f ), this.originY + this.componentY + new Point(this.componentY, (-1)*this.componentX).getY()*0.1f));
        this.triangle[1] = new Vertex(new Vector2f(this.originX + this.componentX + new Point(this.componentY, (-1f)*this.componentX).getX()*(-0.1f), this.originY + this.componentY + new Point(this.componentY, (-1)*this.componentX).getY()*(-0.1f)));
        this.triangle[2] = new Vertex(new Vector2f( this.originX + this.componentX+ this.componentX/5, this.originY + this.componentY + this.componentY/5));

        w.draw(this.triangle,PrimitiveType.TRIANGLES );
        w.draw(arr, PrimitiveType.LINES);
    }
    

    
}