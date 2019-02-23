
package cs380pro2;

import java.util.Random;
/**
 *
 * @author cs471001_18
 */
public class Player
{
    int a,b; //last position
    int x,y; //current position
    char name;
    boolean placed;
    boolean carrot;
    boolean out;
    boolean wins;
    boolean moved; //in case this needs to be accessed outside of thread
    
    //sets toon char
    public Player(char name)
    {
       this.name = name; 
    }
    
    public void nextSquare(int x, int y)
    {
        this.a = this.x;
        this.b = this.y;
        
        this.x = x;
        this.y = y;
    }
    
    public void moveBack()
    {
        x = a;
        y = b;
    }
    
    
    //prints toon char and whether it has a carrot
    public void print()
    {
        System.out.print(name);
        
        if(carrot == true)
            System.out.print("(C)");
    }
    
    //strictly for my purposes
    public void printLocation()
    {
        System.out.print(name + " at " + x + "," + y);
        System.out.print(" out: " + out + "\n");
    }
}
