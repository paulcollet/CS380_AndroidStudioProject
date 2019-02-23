
package cs380pro2;
import java.util.ArrayList;
import java.util.Random;
import static java.lang.Thread.sleep;
/**
 *
 * @author cs471001_18
 */

public class Game
{
    Random rand = new Random(); 
    ArrayList<Player> playerList = new ArrayList<>(4); 
    ArrayList<ArrayList<Square>> board = new ArrayList<>(5); 
    ArrayList<Thread> threadList = new ArrayList<>(playerList.size());  
    ArrayList<Player> winners = new ArrayList<>();
    PrintSquares turn;
    
    private int mountain = 1;
    private int carrots = 2;
    private int rowSize = 5; 
    private int colSize = 5;
    private int gridSize = rowSize * colSize;
    private int players = 4; 
    private int playersAvailable = 4;    

    private int mountainOdds = gridSize/mountain; 
    private int carrotOdds   = gridSize/carrots;  
    private int playerOdds   = gridSize/players;
    
    private boolean chance(int odds)
    {
        if (odds < 1) return true;  
        else return (rand.nextInt(odds) == 0);
    } 
    
    public void game() throws InterruptedException
    {           
        Player BuggsBunny = new Player('B');
        Player TazDevil   = new Player('D');
        Player TweetyBird = new Player('T');
        Player Marvin     = new Player('M');
       
        playerList.add(BuggsBunny);
        playerList.add(TazDevil);
        playerList.add(TweetyBird);
        playerList.add(Marvin);
        
        //create the board
        createBoard();
     
        //print starting board
        turn = new PrintSquares(board);
        for(Player toon:playerList) toon.printLocation();
        
        //set threads
        for(int toon = 0; toon < playerList.size(); toon++)
        {
            Movement ready = new Movement(playerList.get(toon),board);
            Thread set = new Thread(ready);
            threadList.add(set);
        }

        for(Thread go: threadList) go.start();
        
        //game loop
        gameLoop();
          
        //display the winners
        if(winners.size() == 1)
        {
            System.out.println(
                winners.get(0).name + " wins!");
        
        }
        else
        {
            System.out.println("It's a tie!");
            for(int toon = 0; toon < winners.size(); toon++)
            {
                if(toon == winners.size() - 2)
                {
                    System.out.print(winners.get(toon).name + " & ");
                }
                else if (toon == winners.size() - 1)
                {
                    System.out.print(winners.get(toon).name);
                }
                else
                {
                    System.out.print(winners.get(toon).name + ", ");
                }
            }
            System.out.print(" won!\n");
        }
    
    }
    
    /**
     * createBoard
     */
    public void createBoard()
    {
        for (int row = 0; row < rowSize; row++)
        {
           ArrayList<Square> rowList = new ArrayList<>();
            
           for (int col = 0; col < colSize; col++) 
           {   
               Square temp = new Square();
               
               if(mountain > 0 && chance(mountainOdds--))
               {
                   temp.mountain = true;
                   mountain--;                   
               }
               else if(carrots > 0 && chance(carrotOdds--))
               {
                  temp.carrot = true;
                  carrots--;               
               }
               else if(playersAvailable > 0 && chance(playerOdds--))
               {
                   int playerUsed = rand.nextInt(players);
                   
                   while(playerList.get(playerUsed).placed == true)
                   {
                        playerUsed = rand.nextInt(players);
                   }                        

                   temp.occupant = playerList.get(playerUsed);
                   temp.occupied = true;
                   playerList.get(playerUsed).placed = true; 
                   playerList.get(playerUsed).x = row;
                   playerList.get(playerUsed).y = col;
                   playersAvailable--;       
               }
               else
               {
                   //nothing
               }
               
               temp.x = row;
               temp.y = col;
               
               rowList.add(temp);                    
           }
           
           board.add(rowList);
        }        
    } //create board
    
    /**
     * gameLoop
     */
    public void gameLoop()
    {
        int turnCount = 0;
        
        while(winners.size() == 0)
        {                        
            //precaution to make sure everyone moved
            for(Player temp : playerList)
            {
                if(!temp.moved)
                {
                    try{
                        sleep(10);
                    }
                    catch (Exception e) 
                    {
                        e.printStackTrace();
                    }
                }                   
            }       
            
//            //precaution in case locks aren't enough
//            for(Player toon1 : playerList)
//            {
//                for(Player toon2 : playerList)
//                {
//                    if(toon1.x == toon2.x && toon1.y == toon2.y)
//                    {
//                        if(toon1.name == 'M') toon2.out = true;
//                        else if (toon1.name == 'M') toon1.out = true;
//                        else
//                        {   //sorry, toon2. You're more likely second.
//                            toon2.moveBack();
//                            
//                        }
//                    }
//                }
//            }
//            for(Player toon1 : playerList)
//            {
//                if(!toon1.out)
//                {
//                    board.get(toon1.x).get(toon1.y).occupied = true;
//                }
//                
//            }//precaution
            
            turn = new PrintSquares(board); 
            for(Player toon:playerList) toon.printLocation();
                       
            for(Player check: playerList)
            {
                if(check.wins) 
                {
                    winners.add(check);
                }
            } 
     
            //if someone wins, everyone else loses
            if(winners.size() > 0)
            {
                //change thread loop condition
                for(Player temp : playerList)
                {
                    if(!temp.wins) temp.out = true;                   
                } 
            }     
            
            turnCount++;
            
            //after three turns, mountain moves
            if(turnCount == 2)
            {   
                mountain = 1;               
                mountainOdds = 18; //25 - 7  
                
                Square temp = new Square();
                
                for(int row = 0; row < rowSize; row++)
                    for(int col = 0; col < colSize; col++)
                    {
                        temp = board.get(row).get(col);
                        temp.lock = true;
                        
                        if(temp.mountain)
                        {
                            temp.mountain = false;
                        }
                        else
                        {
                            if(!temp.occupied && !temp.carrot)
                            {   
                                if(mountain > 0 && chance(mountainOdds--))
                                {
                                    temp.mountain = true;
                                    mountain--;                   
                                }
                            }
                        }
                        
                        temp.lock = false;
                    }
            }
            
            //restart thread loops
            for(Player temp : playerList) temp.moved = false;          
        }        
    } // game loop
    
    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException
    {     
       Game newGame = new Game();
       newGame.game();
    }
}