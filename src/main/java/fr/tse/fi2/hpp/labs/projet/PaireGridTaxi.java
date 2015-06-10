package fr.tse.fi2.hpp.labs.projet;

import java.util.List;

import fr.tse.fi2.hpp.labs.beans.GridPoint;

public class PaireGridTaxi implements java.lang.Comparable {
	
	    GridPoint grid;
	    int taxi;

	    public PaireGridTaxi(GridPoint grid, int taxi)
	    {
	        this.grid = grid;
	        this.taxi=taxi;
	    }
	    
	    public GridPoint getGrid()
	    {
			return grid;
	    }
	    
	    public int getTaxi()
	    {
			return taxi;
	    }
	    
	    public void setGrid(GridPoint grid)
	    {
			this.grid=grid;
	    }
	    
	    public void setTaxi(int taxi)
	    {
			this.taxi=taxi;
	    }

		@Override
		public int compareTo(Object other) 
		{ 
			GridPoint prep1 = ((PaireGridTaxi) other).getGrid();
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

	

