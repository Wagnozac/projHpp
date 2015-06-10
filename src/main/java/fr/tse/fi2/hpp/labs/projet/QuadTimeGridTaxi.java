package fr.tse.fi2.hpp.labs.projet;

import fr.tse.fi2.hpp.labs.beans.GridPoint;

public class QuadTimeGridTaxi {
	
    long tempsPick;
    long tempsDrop;
    GridPoint gridDrop;
    String taxi;
	public QuadTimeGridTaxi(long tempsPick, long tempsDrop,
			GridPoint gridDrop, String taxi) {
		super();
		this.tempsPick = tempsPick;
		this.tempsDrop = tempsDrop;
		this.gridDrop = gridDrop;
		this.taxi = taxi;
	}
	public long getTempsPick() {
		return tempsPick;
	}
	public void setTempsPick(long tempsPick) {
		this.tempsPick = tempsPick;
	}
	public long getTempsDrop() {
		return tempsDrop;
	}
	public void setTempsDrop(long tempsDrop) {
		this.tempsDrop = tempsDrop;
	}
	public GridPoint getGridDrop() {
		return gridDrop;
	}
	public void setGridDrop(GridPoint gridDrop) {
		this.gridDrop = gridDrop;
	}
	public String getTaxi() {
		return taxi;
	}
	public void setTaxi(String taxi) {
		this.taxi = taxi;
	}
    
    

}