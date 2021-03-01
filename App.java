package app;

import java.util.ArrayList;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

public class App {
    public static void main(String[] args) throws Exception {
        RenderWindow main_wind = new RenderWindow(new VideoMode(1100, 1100),"Fluids" );
        int num = 10000;
        Field field = new Field(50, 2);

        field.setVal(new Vektor(0, 5), 12, 1, 1);
        field.setVal(new Vektor(-2.768949f, 1.946204f), 11, 1, 1);
        field.setVal(new Vektor(2.5444732f, 1.0356596f), 13, 1, 1);

        ArrayList<Particle> particles = new ArrayList<>();
        ArrayList<Particle> deadParticles = new ArrayList<>();

        for(int i = 0; i<num; i++){
            particles.add(new Particle((float)Math.random()*980+10, (float)Math.random()*980+10));
        }
        int t = 1;
        while(main_wind.isOpen()){
            for(Event ev : main_wind.pollEvents()){
                if(ev.type== Event.Type.CLOSED){
                    main_wind.close();
                }
            }

            field.drawField(main_wind, t);
            for(int x = 0; x<50; x++){
                particles.add(new Particle((float)Math.random()*980+10, (float)Math.random()*980+10));
            }
        
            particles.add(new Particle((float)Math.random()*60+235, (float)Math.random()*21+20));
            
            for(Particle p : particles){
                p.render(main_wind, field, t);
                if(p.getVel() == 0){
                    deadParticles.add(p);
                }
            }
            for(Particle p : deadParticles){
                particles.remove(p);
            } 
            deadParticles.clear();
            main_wind.display();
            main_wind.clear();
        }
    }
    
}