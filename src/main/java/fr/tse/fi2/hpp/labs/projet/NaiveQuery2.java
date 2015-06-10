package fr.tse.fi2.hpp.labs.projet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.GridPoint;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class NaiveQuery2 extends AbstractQueryProcessor {
	
	// variables for getMedianFare
	private static long delta = 900000; // 15 min en millisecondes.
	private static List<DebsRecord> currentRecs = null;
	private List<String> currentResults = null;
	private static List<PaireGridMoney> gridAndMoney= null;
	private static List<PaireGridMoney> medianFare=null;
	private static List<PaireGridTaxi> EmptyTaxis=null;
	
	
	// variables for getEmptyTaxis 
	private static long epsilon = 1800000;// 30 min en millisecondes.
	private static List<DebsRecord> currentTaxis = null;
	private static List<DebsRecord> currentTaxisPick = null;
	private static List<QuadrupleTimeGridTaxi> gridAndTaxis=null;
	
	
	public NaiveQuery2(QueryProcessorMeasure measure, List<DebsRecord> currentRecs) {
		super(measure);
		
		this.currentRecs=currentRecs;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		getMedianFare(record);
		

	}
	
	
	public static void getMedianFare( DebsRecord record)
	{
		float pickLat=record.getPickup_latitude();
		float pickLong= record.getPickup_longitude();
		float dropLat= record.getDropoff_longitude();
		float dropLong= record.getDropoff_longitude();
		GridPoint pick = convert(pickLat,pickLong);
		GridPoint drop = convert(dropLat,dropLong);
		currentRecs.add(record);
		int size =currentRecs.size();
		List<Integer> ind=null; 
		
		// Here we fill the current records and we remove the ones which are too old
		for (int i = 0; i< size;i++)
		{
			if (record.getDropoff_datetime()>currentRecs.get(i).getDropoff_datetime()+delta)
			{
				ind.add(i);
			}
			else
			{
			GridPoint coord = convert(currentRecs.get(i).getPickup_latitude(),currentRecs.get(i).getPickup_longitude());
			PaireGridMoney paireGridMoney = new PaireGridMoney(coord,currentRecs.get(i).getTip_amount()+currentRecs.get(i).getFare_amount());
			gridAndMoney.add(paireGridMoney);
			}
			
		for (int j=ind.size()-1; j>=0;j--)
		{
			currentRecs.remove(ind.get(i));
		}
			
			
		}
		
		// this allows us to find the median fares of the GridPoint present in the current Records
		while (gridAndMoney.size()!=0) 
		{
			size= gridAndMoney.size();
			int x = gridAndMoney.get(size-1).getGrid().getX();
			int y = gridAndMoney.get(size-1).getGrid().getY();
			List<Float> money= null;
			
			for (int i =size-1; i>=0;i--)
			{
				
				if( gridAndMoney.get(i).getGrid().getX()==x)
				{
					if (gridAndMoney.get(i).getGrid().getY()==y)
					{
						money.add(gridAndMoney.get(i).getMoney());
						gridAndMoney.remove(i);
					}
				}
				
			}
			PaireGridMoney res =new PaireGridMoney(new GridPoint(x,y),Median(money));
			medianFare.add(res);
			
		}
		
	}
	
	
	public static void getEmptyTaxis( DebsRecord record)
	{
		
		float pickLat=record.getPickup_latitude();
		float pickLong= record.getPickup_longitude();
		float dropLat= record.getDropoff_longitude();
		float dropLong= record.getDropoff_longitude();
		GridPoint pick = convert(pickLat,pickLong);
		GridPoint drop = convert(dropLat,dropLong);
		currentTaxis.add(record);
		int size =currentTaxis.size();
		List<Integer> ind=null;
		
		
//		// Here we fill the current Taxis which dropped and we remove the ones which are too old
//		for (int i = 0; i< size;i++)
//		{
//			if (record.getDropoff_datetime()>currentTaxis.get(i).getDropoff_datetime()+epsilon)
//			{
//				currentTaxis.remove(i);
//				break;
//			}
//			else
//			{
//			long tempsP = currentTaxis.get(i).getPickup_datetime();
//			long tempsD = currentTaxis.get(i).getDropoff_datetime();
//			GridPoint coordDrop = convert(currentTaxis.get(i).getDropoff_latitude(),currentTaxis.get(i).getDropoff_longitude());
//			QuadrupleTimeGridTaxi quadrupleTimeGridTaxi = new QuadrupleTimeGridTaxi(tempsP,tempsD,coordDrop,currentTaxis.get(i).getMedallion());
//			gridAndTaxis.add(quadrupleTimeGridTaxi);
//			}
//			
//		}
		
		// Here we fill the current Taxis which picked and we remove the ones which are too old
		currentTaxisPick.add(record);
		int sizePick =currentTaxisPick.size();
		for (int i = 0; i< sizePick;i++)
		{
			if (record.getDropoff_datetime()>currentTaxisPick.get(i).getPickup_datetime()+epsilon)
			{
				ind.add(i);
			}
			
			else
			{
			long tempsP = currentTaxisPick.get(i).getPickup_datetime();
			long tempsD = currentTaxisPick.get(i).getDropoff_datetime();
			GridPoint coordDrop = convert(currentTaxisPick.get(i).getDropoff_latitude(),currentTaxisPick.get(i).getDropoff_longitude());
			QuadrupleTimeGridTaxi quadrupleTimeGridTaxi = new QuadrupleTimeGridTaxi(tempsP,tempsD,coordDrop,currentTaxisPick.get(i).getMedallion());
			gridAndTaxis.add(quadrupleTimeGridTaxi);
			}
			
			
			for (int j=ind.size()-1;i>=0;i--)
			{
				currentTaxisPick.remove(ind.get(j));
			}
			
		}
		

		
			size= gridAndTaxis.size();
			int xD = gridAndTaxis.get(size-1).getGridDrop().getX();
			int yD = gridAndTaxis.get(size-1).getGridDrop().getY();
			long tempsDrop = gridAndTaxis.get(size-1).getTempsDrop();
			long tempsPick = gridAndTaxis.get(size-1).getTempsPick();
			String medaille = gridAndTaxis.get(size-1).getTaxi();
			PaireGridTaxi temp=null;
			List<String> taxiVus=null;
			int taxisVides= 0;
			
			for (int i =size-1; i>=0;i--)
			{
				if (gridAndTaxis.get(i).getTempsDrop()>record.getPickup_datetime())
				{
						gridAndTaxis.remove(i);
				}
				else if (taxiVus.contains(gridAndTaxis.get(i).getTaxi()))
				{
						gridAndTaxis.remove(i);
				}
				
				else
				{
					List<GridPoint> gridWithETaxis= null;
					int sizeE= EmptyTaxis.size();
					for (int j =0; j<sizeE;j++)
					{
						
						gridWithETaxis.add(EmptyTaxis.get(j).getGrid());
						
					}
						
						
						if (gridWithETaxis.contains(gridAndTaxis.get(i).getGridDrop()))
						{
							
							int indice =gridWithETaxis.indexOf(gridAndTaxis.get(i).getGridDrop());
							EmptyTaxis.get(indice).setTaxi(EmptyTaxis.get(indice).getTaxi()+1);
						}
						else
						{
							EmptyTaxis.add(new PaireGridTaxi(gridAndTaxis.get(i).getGridDrop(),1));
						}
					
					taxiVus.add(gridAndTaxis.get(i).getTaxi());
					gridAndTaxis.remove(i);
					
				}
				
			}
			
		
	}
	
	
	
	public static Float Median(List<Float> money)
	{
	    Collections.sort(money);
	 
	    if (money.size() % 2 == 1)
		return (float) money.get((money.size()+1)/2-1);
	    else
	    {
		float lower = (float) money.get(money.size()/2-1);
		float upper = (float) money.get(money.size()/2);
	 
		return (float) ((lower + upper) / 2.0);
	    }	
	}

}
