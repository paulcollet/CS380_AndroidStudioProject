/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs380pro2;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Random;
/**
 *
 * @author cs471001_18
 */
public class Movement implements Runnable 
{
    Random rand = new Random();
    ArrayList<Square> options;  
    ArrayList<ArrayList<Square>> board; 
    Player current;
    Square temp = new Square();
    int choice; 
    
    public Movement(Player toon, ArrayList<ArrayList<Square>> grid)
    {
        this.board = grid;
        this.current = toon; 
    }
    
    @Override
    public synchronized void run()
    {       
        while(!(current.out||current.wins))
        {
            findOptions();
            
            //if player has no carrot
            if(current.carrot == false)
            { 
                checkChoices();                
                lookForCarrot();                
            }
            else //toon has a carrot
            {
                lookForMountain();            
            }
            
            //Marvin cheats
            if(!current.moved && current.name == 'M')
            {
                cheatingMarvin();               
            }
            
            //if toon still hasn't moved yet
            while(!current.moved) 
            {                
                if(!(options.size() == 0))//if there are options left
                {
                    randomMove();
                }               
                //else the toon can't move
            }             
  
            //ensures this thread will wait          
            while(current.moved)
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
    }   
    
    //method for move action
    public void moveTo(int choice)
    {        
        //move to new square
        options.get(choice).occupied = true;
        current.nextSquare(options.get(choice).x, options.get(choice).y);
        options.get(choice).occupant = current;  
        
        //leave old square
        temp.occupied = false;                               
    }
    
    /**
     * squareLock(int choice)
     * @param choice
     * @return boolean (whether there was a previous lock on the square) 
     */
    public boolean squareLock(int choice)
    {
        boolean suspect = false;
        
        //data preservation       
        while(options.get(choice).lock)
        {
            suspect = true;
            
            //wait
            try{
                sleep(2);
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
        
        //lock for data protection
        options.get(choice).lock = true;
        
        return suspect;
    }
    
    public void checkChoices()
    {
        for(choice = 0; choice < options.size(); choice++)
        {                     
            //if the mountain is an option, remove it
            //not ideal, but since we're altering the list...
            if(options.get(choice).mountain == true ||
               (!(current.name == 'M') && 
               options.get(choice).occupied == true))    
            {
                options.remove(choice);
            }
        }   
    }
    
    public void findOptions()
    {
        options = new ArrayList<>(); 

        //adding to player options with (0,0) of matrix at upper left
        if(current.x > 0) //left option if not in column 0
        {
            temp = board.get(current.x-1).get(current.y);
            options.add(temp);
        }
        //right option if not in last column 
        if(current.x < board.size()-1) 
        {
            temp = board.get(current.x+1).get(current.y);
            options.add(temp);
        }
        //up option if not in row 0
        if(current.y > 0) 
        {
            temp = board.get(current.x).get(current.y-1);
            options.add(temp);
        }
        //down option if not in last row
        if(current.y < board.size()-1) 
        {
            temp = board.get(current.x).get(current.y+1);
            options.add(temp);
        }

        //reusing temp here for current player location
        temp = board.get(current.x).get(current.y);        
    }
    
    //looks in neighbouring squares for carrots first
    public void lookForCarrot()
    {      
        for(choice = 0; choice < options.size(); choice++)
        {      
            if (options.get(choice).carrot)
            {
                if(squareLock(choice)) checkChoices();

                //remove carrot from square and add it to player
                options.get(choice).carrot = false;
                current.carrot = true;

                moveTo(choice);
                current.moved = true;

                options.get(choice).lock = false;
                break;
            }                    
        }    
    }
    
    //look in neighbouring squares for mountain first
    public void lookForMountain()
    {        
        for(choice = 0; choice < options.size(); choice++)
        {                    
            if(options.get(choice).mountain == true)
            {
                 moveTo(choice); 
                 current.moved = true;
                 current.wins = true;
                 break;
            }
        }         
    }
    
    public void cheatingMarvin()
    {
        //Marvin's cheats work better if he observes the others first
        try{
            sleep(20);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        for(choice = 0; choice < options.size(); choice++)
        {  
            //if adjacent spot has a toon w/ a carrot
            if(options.get(choice).occupied &&
               options.get(choice).occupant.carrot)  
            {
                if(squareLock(choice)) checkChoices();

                current.carrot = true;
                options.get(choice).occupant.out = true;

                moveTo(choice);
                current.moved = true;

                //unlock for other players
                options.get(choice).lock = false;
                break;
            }    
        }        
    }
    
    public void randomMove()
    {
        choice = rand.nextInt(options.size());

        if(squareLock(choice)) checkChoices();
        moveTo(choice);
        current.moved = true;

        options.get(choice).lock = false;        
    }
}
