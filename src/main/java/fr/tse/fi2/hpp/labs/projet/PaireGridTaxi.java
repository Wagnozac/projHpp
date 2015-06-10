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
			int nombre1 = ((PaireGridTaxi) other).getTaxi(); 
			int nombre2 = this.getTaxi(); 
		    if (nombre1 > nombre2)  return -1; 
		    else if(nombre1 == nombre2) return 0; 
		    else return 1; 
		   
		}
		

}

	

