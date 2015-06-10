package fr.tse.fi2.hpp.labs.projet;

import fr.tse.fi2.hpp.labs.beans.GridPoint;

public class QuadGridRatioTaxiMedian implements Comparable {
	
    GridPoint grid;
    float ratio;
    int taxi;
    float median;

	
    public QuadGridRatioTaxiMedian(GridPoint grid, float ratio, int taxi,
			float median) {
		super();
		this.grid = grid;
		this.ratio = ratio;
		this.taxi = taxi;
		this.median = median;
	}


	public GridPoint getGrid() {
		return grid;
	}


	public void setGrid(GridPoint grid) {
		this.grid = grid;
	}


	public float getRatio() {
		return ratio;
	}


	public void setRatio(float ratio) {
		this.ratio = ratio;
	}


	public int getTaxi() {
		return taxi;
	}


	public void setTaxi(int taxi) {
		this.taxi = taxi;
	}


	public float getMedian() {
		return median;
	}


	public void setMedian(float median) {
		this.median = median;
	}


	@Override
	public int compareTo(Object other) 
	{ 

		float nombre1=((QuadGridRatioTaxiMedian) other).getRatio();
		float nombre2=this.getRatio();
	    if (nombre1 > nombre2)  return -1; 
	    else if(nombre1 == nombre2) return 0; 
	    else return 1; 

	   
	}
	
	
}
