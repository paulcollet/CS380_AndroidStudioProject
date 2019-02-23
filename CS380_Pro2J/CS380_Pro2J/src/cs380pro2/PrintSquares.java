
package cs380pro2;
import java.util.ArrayList;
/**
 *
 * @author cs471001_18
 */
public class PrintSquares 
{

    public PrintSquares(ArrayList<ArrayList<Square>> grid)
    {
        Square temp = new Square();
        
        System.out.print("\n");
        border(grid.size());
        
        for (int row = 0; row < grid.size(); row++)
        {
           for (int col = 0; col < grid.get(row).size(); col++) 
           {  
              temp = grid.get(row).get(col);
              temp.print();
              System.out.print("\t");
           }
           System.out.print("\n");
        }  
        
        border(grid.size());
        System.out.print("\n");
        
    }
    
    public void border(int num)
    {
        for (int i = 0; i < num - 1; i++)
        {
            System.out.print("--------");
        }
        System.out.print("-\n");
    }
}
