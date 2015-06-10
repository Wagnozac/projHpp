package fr.tse.fi2.hpp.labs.projet;

import fr.tse.fi2.hpp.labs.beans.GridPoint;

public class PaireGridMoney {
	
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
}
