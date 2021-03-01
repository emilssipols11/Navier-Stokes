package app;

import org.jsfml.system.Vector2f;

public class Cell {

    private float xPos;
    private float yPos;
    private int num;
    private static float dif = 980;
    private float pressure;
    private Vector2f center;
    private Vektor dirV;

        
    public Cell(int x, int y, int num){
        this.num = num;
        xPos = 10 + (dif/num)*x;
        yPos = 10 + (dif/num)*y;
        center = new Vector2f(xPos+(dif/num), yPos+(dif/num));
        yPos -= 440;
        xPos -= 440;
        dirV = new Vektor(0, 0, center.x, center.y);
        xPos = 10 + (dif/num)*x;
        yPos = 10 + (dif/num)*y;
        
    }

    public float getP(){
        return pressure;
    }

    public void setPressure(float p){
        this.pressure = p;
    }

    public Vektor getDirection(){
        return dirV;
    }

    public void setV(float x, float y){
        this.dirV.setComponentX(x);
        this.dirV.setComponentY(y);
    }

    public Vector2f getCenter(){
        return center;
    } 
    
}   
