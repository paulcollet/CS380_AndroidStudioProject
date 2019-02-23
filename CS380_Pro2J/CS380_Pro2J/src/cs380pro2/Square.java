
package cs380pro2;

/**
 *
 * @author cs471001_18
 */
public class Square 
{ 
    int x,y; //square coordinates
    boolean lock;
    boolean carrot;
    boolean mountain;
    boolean occupied;
    Player occupant;
 
    public void print()
    {
        if (occupied == false)
        {
            if (mountain == true) System.out.print('F');
            else if (carrot == true) System.out.print('C');
            else System.out.print('-');
        }
        else
        {
            occupant.print();
            if (mountain == true) System.out.print("(F)");
        }
    }
}
