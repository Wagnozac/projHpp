package fr.tse.fi2.hpp.labs.projet;

import fr.tse.fi2.hpp.labs.beans.GridPoint;

public class PaireGridMoney implements java.lang.Comparable {
	
    GridPoint grid;
    float money;

    public PaireGridMoney(GridPoint grid, float money)
    {
        this.grid = grid;
        this.money = money;
    }
    
    public GridPoint getGrid()
    {
		return grid;
    }
    
    public float getMoney()
    {
		return money;
    }
    
    public void setGrid(GridPoint grid)
    {
		this.grid=grid;
    }
    
    public void setMoney(float money)
    {
		this.money=money;
    }

    @Override
	public int compareTo(Object other) 
	{ 
		GridPoint prep1 = ((PaireGridMoney) other).getGrid();
		GridPoint prep2= this.getGrid();
		
		if (prep1.getX()==prep2.getX())
		{
			int nombre1=prep1.getY();
			int nombre2=prep2.getY();
		    if (nombre1 > nombre2)  return -1; 
		    else if(nombre1 == nombre2) return 0; 
		    else return 1; 
			
		}
		
		else
		{
		int nombre1=prep1.getX();
		int nombre2=prep2.getX();
	    if (nombre1 > nombre2)  return -1; 
	    else if(nombre1 == nombre2) return 0; 
	    else return 1; 
		}
	   
	}
}
