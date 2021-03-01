package app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsfml.graphics.RenderWindow;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.LUDecomposition;

public class Field {

    private Cell[][][] field;
    
    public Field(int count, int time)throws IOException{

        field = new Cell[count][count][time];

        for(int ti = 0; ti<time; ti++){
            for(int x = 0; x<count; x++){
                for(int y = 0;y<count;y++){
                    field[x][y][ti] = new Cell(x, y, count);
                }
            }
        }
        int speed = 5;
        int pres = 1;
        for(int ti = 0; ti<time; ti++){
            for(int x = 11; x<14; x++){
                field[x][0][ti].setV(0, speed);
                field[x][0][ti].setPressure(pres);
            }
        }

        for(int ti = 0; ti<time-1; ti++){

            // for(int j = 0; j<count; j++){
            //     field[j][count-1][ti].setPressure(field[j][count-2][ti].getP());
            // }
            // for(int j = 1; j<count; j++){
            //     field[0][j][ti].setPressure(field[1][j][ti].getP());
            //     field[count-1][j][ti].setPressure(field[count-2][j][ti].getP());
            // }

            DoubleMatrix2D coef = new DenseDoubleMatrix2D((int)Math.pow(count-2, 2), (int)Math.pow(count-2, 2));
            
            int helper = 0;

            for(int x = 0; x<count-2; x++){
                for(int y = 0; y<count-2; y++){
                    coef.set(helper, helper, -4);
                    if(x-1 != -1){
                        coef.set(helper, (x-1)*(count-2)+y, 1);
                    }
                    if(x+1 != count-2){
                        coef.set(helper, (x+1)*(count-2)+y, 1);
                    }
                    if(y-1 != -1){
                        coef.set(helper, x*(count-2)+y-1, 1);
                    }
                    if(y+1 != count-2){
                        coef.set(helper, x*(count-2)+y+1, 1);
                    }
                    helper++;
                }
            }

            DoubleMatrix2D velocity = new DenseDoubleMatrix2D((int)Math.pow(count-2, 2), 1);
            helper = 0;
            for(int x = 1; x<count-1; x++){
                for(int y = 1; y<count-1; y++){
                    velocity.set(helper, 0, -0.5*(Math.pow(field[x+1][y][ti].getDirection().getComponentX()-field[x-1][y][ti].getDirection().getComponentX(), 2)
                    +2*(field[x+1][y][ti].getDirection().getComponentX()-field[x-1][y][ti].getDirection().getComponentX())*(field[x][y+1][ti].getDirection().getComponentY()-field[x][y-1][ti].getDirection().getComponentY())
                    + Math.pow(field[x][y+1][ti].getDirection().getComponentY()-field[x][y-1][ti].getDirection().getComponentY(), 2) ) );
                    helper++;
                }
            }
            for(int x = 11; x<14; x++){
                double val = velocity.get(x*(count-2), 0);   // ?
                velocity.set(x*(count-2), 0, val-pres);
            }

            LUDecomposition eq = new LUDecomposition(coef);
            DoubleMatrix2D values;

            values = eq.solve(velocity);

            helper = 0;
            for(int x = 1; x<count-1; x++){
                for(int y = 1; y<count-1; y++){
                    field[x][y][ti].setPressure((float)values.get(helper, 0));
                    helper++;
                }
            }

            float beta  = 0.2f;

            coef.assign(0);

            helper = 0;
            for(int x = 0; x<count-2; x++){
                for(int y = 0; y<count-2; y++){
                    coef.set(helper, helper, (1-4*beta));
                    if(x-1 != -1){
                        coef.set(helper, (x-1)*(count-2)+y, beta);
                    }
                    if(x+1 != count-2){
                        coef.set(helper, (x+1)*(count-2)+y, beta);
                    }
                    if(y-1 != -1){
                        coef.set(helper, x*(count-2)+y-1, beta);
                    }
                    if(y+1 != count-2){
                        coef.set(helper, x*(count-2)+y+1, beta);
                    }
                    helper++;
                }
            }

            velocity.assign(0);

            helper = 0;
            for(int x = 1; x<count-1; x++){
                for (int y = 1; y<count-1; y++){
                    velocity.set(helper, 0, field[x][y][ti].getDirection().getComponentX());
                    helper++;
                }
            }
            

            values.assign(0);

            coef.zMult(velocity, values);
            

            helper = 0;
            for(int x = 1; x<count-1; x++){
                for (int y = 1; y<count-1; y++){
                    double presDif = (field[x+1][y][ti].getP() - field[x-1][y][ti].getP())/2;
                    double calculation = field[x][y][ti].getDirection().getComponentX()*(field[x+1][y][ti].getDirection().getComponentX() - field[x-1][y][ti].getDirection().getComponentX())
                    +field[x][y][ti].getDirection().getComponentX()*( (field[x][y+1][ti].getDirection().getComponentY()-field[x][y-1][ti].getDirection().getComponentY())/2)
                    +field[x][y][ti].getDirection().getComponentY()*((field[x][y+1][ti].getDirection().getComponentX()-field[x][y-1][ti].getDirection().getComponentX())/2);
                    double val = values.get(helper, 0);   // ?
                    values.set(helper, 0, val - presDif - calculation);
                    helper++;
                }
            }
            
            DoubleMatrix2D velocityY = new DenseDoubleMatrix2D((int)Math.pow(count-2, 2), 1);
            DoubleMatrix2D valuesY = new DenseDoubleMatrix2D((int)Math.pow(count-2, 2), 1);
            
            helper = 0;
            for(int x = 1; x<count-1; x++){
                for (int y = 1; y<count-1; y++){
                    velocityY.set(helper, 0, field[x][y][ti].getDirection().getComponentY());
                    helper++;
                }
            }

            coef.zMult(velocityY, valuesY);

            for(int x = 11; x<14; x++){

                double val = valuesY.get(x*(count-2), 0);   // ?
                valuesY.set(x*(count-2), 0, val+beta*speed);
            }

            helper = 0;
            for(int x = 1; x<count-1; x++){
                for (int y = 1; y<count-1; y++){
                    double presDif = (field[x][y+1][ti].getP() - field[x][y-1][ti].getP())/2;
                    double calculation = field[x][y][ti].getDirection().getComponentX()*( (field[x+1][y][ti].getDirection().getComponentY()-field[x-1][y][ti].getDirection().getComponentY())/2 )
                    + field[x][y][ti].getDirection().getComponentY()*( (field[x+1][y][ti].getDirection().getComponentX()-field[x-1][y][ti].getDirection().getComponentX())/2 )
                    + field[x][y][ti].getDirection().getComponentY()*(field[x][y+1][ti].getDirection().getComponentY() - field[x][y-1][ti].getDirection().getComponentY());
                    double val = valuesY.get(helper, 0);   // ?
                    valuesY.set(helper, 0, val - presDif - calculation);
                    helper++;
                }
            }
            
            helper = 0;
            for(int x = 1; x<count-2; x++){
                for(int y = 1; y<count-1; y++){
                    field[x][y][ti+1].setV((float)values.get(helper, 0), (float)valuesY.get(helper, 0));
                    helper++;
                }
            }

        }
        // File file = new File(file path);
        // FileWriter w = new FileWriter(file);
        // for(int ti = 0; ti<time-1; ti++){
        //     for(int x = 0; x<count; x++){
        //         for(int y = 0; y<count; y++){
        //             w.write(x+"\t"+y+"\t"+ti+"\t"+field[x][y][0].getP()+"\n");
        //         }
        //     }
        // }
        // w.close();

    }

    public int getSize(){
        return field.length;
    }

    public Cell getCell(int x, int y, int time){
        return field[x][y][time];
    }
    public void setVal(Vektor vek, int x, int y, int time ){
        field[x][y][time].setV(vek.getComponentX(), vek.getComponentY());
    }


    public void drawField(RenderWindow main_wind, int time){

            for(int x=0; x<field.length; x++){

                for(int y=0; y<field.length ;y++){

                    //field[x][y][time].getDirection().normalize();

                   // field[x][y][time].getDirection().render(main_wind);

                }

            }

    }

}