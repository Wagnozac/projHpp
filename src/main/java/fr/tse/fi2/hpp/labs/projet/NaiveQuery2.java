package fr.tse.fi2.hpp.labs.projet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.lang.Math;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.GridPoint;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class NaiveQuery2 extends AbstractQueryProcessor {
	
	// variables for getMedianFare
	private static long delta = 900000; // 15 min en millisecondes.
	private static List<DebsRecord> currentRecs ;
	private static List<PaireGridMoney> gridAndMoney;
	private static List<PaireGridMoney> medianFare;
	private static List<QuadGridRatioTaxiMedian> mostProfitableAreas;
	private static List<PaireGridTaxi> emptyTaxis;
	private static String prefixe="";
	private static String line="";
	private static String suffixe="";
	
	
	// variables for getEmptyTaxis 
	private static long epsilon = 1800000;// 30 min en millisecondes.
	private static List<DebsRecord> currentTaxisPick ;
	private static List<QuadTimeGridTaxi> gridAndTaxis;
	
	
	public NaiveQuery2(QueryProcessorMeasure measure) {
		super(measure);
		
		currentRecs=new ArrayList<DebsRecord>();
		gridAndMoney=new ArrayList<PaireGridMoney>();
		medianFare=new ArrayList<PaireGridMoney>();
		mostProfitableAreas=new ArrayList<QuadGridRatioTaxiMedian>();// à réécrire FIRST!!!!!!!!!!!! avec des hashmap ouech!
		emptyTaxis=new ArrayList<PaireGridTaxi>();
		//currentTaxis=new ArrayList<DebsRecord>();
		currentTaxisPick=new ArrayList<DebsRecord>();
		gridAndTaxis=new ArrayList<QuadTimeGridTaxi>();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		long delayStart = System.nanoTime();
		medianFare.clear();
		gridAndMoney.clear();
		getMedianFare(record);
		gridAndTaxis.clear();
		emptyTaxis.clear();
		getEmptyTaxis(record);
		mostProfitableAreas.clear();
		getMostProfitableAreas(record);
		long delayEnd = System.nanoTime();
		suffixe=", "+(delayEnd-delayStart);
		
		ecrireQuery2(record);
		
		
		

	}
	
	
	public static void getMedianFare( DebsRecord record)
	{
		medianFare.clear();
		currentRecs.add(record);
		
	
		while (record.getDropoff_datetime()>currentRecs.get(0).getDropoff_datetime()+delta)
		{
			currentRecs.remove(0);
		}
		int size =currentRecs.size();
		
		// Here we fill the current records and we remove the ones which are too old
		for (int i = 0; i< size;i++)
		{
			

			GridPoint coord = convert(currentRecs.get(i).getPickup_latitude(),currentRecs.get(i).getPickup_longitude());
			PaireGridMoney paireGridMoney = new PaireGridMoney(coord,currentRecs.get(i).getTip_amount()+currentRecs.get(i).getFare_amount());
			gridAndMoney.add(paireGridMoney);
			
			
				
		}
		
		// this allows us to find the median fares of the GridPoint present in the current Records
		while (gridAndMoney.size()!=0) 
		{
			int size1= gridAndMoney.size();
			int x = gridAndMoney.get(size1-1).getGrid().getX();
			int y = gridAndMoney.get(size1-1).getGrid().getY();
			List<Float> money= new ArrayList<Float>();
			
			for (int i =size1-1; i>=0;i--)
			{
				
				if( gridAndMoney.get(i).getGrid().equals(new GridPoint(x,y)))
				{
						money.add(gridAndMoney.get(i).getMoney());
						gridAndMoney.remove(i);
				}
				
			}
			
			PaireGridMoney res =new PaireGridMoney(new GridPoint(x,y),Median(money));
			medianFare.add(res);
			
		}
		
	}
	
	
	public static void getEmptyTaxis( DebsRecord record)
	{
		
		
		// Here we fill the current Taxis which picked and we remove the ones which are too old
		
		currentTaxisPick.add(record);
		

		while (record.getDropoff_datetime()>currentTaxisPick.get(0).getPickup_datetime()+epsilon)
		{
			currentTaxisPick.remove(0);
		}
		
		int sizePick =currentTaxisPick.size();
		for (int i = 0; i< sizePick;i++)
		{

			
		
			
			long tempsP = currentTaxisPick.get(i).getPickup_datetime();
			long tempsD = currentTaxisPick.get(i).getDropoff_datetime();
			GridPoint coordDrop = convert(currentTaxisPick.get(i).getDropoff_latitude(),currentTaxisPick.get(i).getDropoff_longitude());
			QuadTimeGridTaxi quadTimeGridTaxi = new QuadTimeGridTaxi(tempsP,tempsD,coordDrop,currentTaxisPick.get(i).getMedallion());
			gridAndTaxis.add(quadTimeGridTaxi);
			
			
			
		}
		

		
			int size= gridAndTaxis.size();
			List<String> taxiVus=new ArrayList<String>();
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
					List<GridPoint> gridWithETaxis= new ArrayList<GridPoint>();
					int sizeE= emptyTaxis.size();
					for (int j =0; j<sizeE;j++)
					{
						
						gridWithETaxis.add(emptyTaxis.get(j).getGrid());
						
					}
						
						
						if (gridWithETaxis.contains(gridAndTaxis.get(i).getGridDrop()))
						{
							
							int indice =gridWithETaxis.indexOf(gridAndTaxis.get(i).getGridDrop());
							emptyTaxis.get(indice).setTaxi(emptyTaxis.get(indice).getTaxi()+1);
						}
						else
						{
							emptyTaxis.add(new PaireGridTaxi(gridAndTaxis.get(i).getGridDrop(),1));
						}
					
					taxiVus.add(gridAndTaxis.get(i).getTaxi());
					gridAndTaxis.remove(i);
					
				}
				
			}
			
		
	}
	
	
	
	
	public static void getMostProfitableAreas (DebsRecord record)
	{
		
	
		//Map<Integer,GridPoint> medianFareGrid=new HashMap<Integer,GridPoint>();
		//Map<Integer,GridPoint> emptyTaxisGrid=new HashMap<Integer,GridPoint>();
		List<PaireGridTaxi> medianFareGrid=new ArrayList<PaireGridTaxi>();
		List<PaireGridMoney> emptyTaxisGrid=new ArrayList<PaireGridMoney>();
//		medianFareGrid.clear();
//		emptyTaxisGrid.clear();
//		Set<GridPoint> set1 = new HashSet<GridPoint>() ;
//		Set<GridPoint> set2 = new HashSet<GridPoint>() ;

		for (int i = medianFare.size()-1; i>=0;i--)
			{

				medianFareGrid.add(new PaireGridTaxi(medianFare.get(i).getGrid(),0)); // nécessaire pour pouvoir utiliser contains 
//				System.out.println("sous X : " + medianFareGrid.get(medianFare.size()-1-i).getGrid().getX() +"sous Y : " + medianFareGrid.get(medianFare.size()-1-i).getGrid().getY());
			}
			
		for (int j = emptyTaxis.size()-1; j>=0;j--)
			{

				emptyTaxisGrid.add(new PaireGridMoney(emptyTaxis.get(j).getGrid(),0));// nécessaire pour pouvoir utiliser contains 
//				System.out.println("taxi X : " + emptyTaxis.get(j).getGrid().getX() +"taxi Y : " + emptyTaxis.get(j).getGrid().getY());
		}
			
		
		//int ii=0;
		//System.out.println("Taille empty taxis : " + emptyTaxis.size());
		
		//System.out.println("Taille median fare : " + medianFare.size());
		
		
		for (int j =emptyTaxis.size()-1; j>=0;j--)
		{
				if (!medianFare.contains(emptyTaxisGrid.get(j)))
				{
					emptyTaxis.remove(j);
				}

		}

//		System.out.println("Taille empty taxis1 : " + emptyTaxis.size());
//		
//		System.out.println("Taille median fare1 : " + medianFare.size());
		for (int j =0; j<medianFare.size();j++)
		{

				if (!emptyTaxis.contains(medianFareGrid.get(j)))
				{
					
					emptyTaxis.add(new PaireGridTaxi(medianFareGrid.get(j).getGrid(),0));

				}

		}
//		System.out.println("Taille empty taxis : " + emptyTaxis.size());
//		
//		System.out.println("Taille median fare : " + medianFare.size());
		
		
		

//		while (emptyTaxis.size()!=medianFare.size())
//		{
//			//System.out.println("Taille empty taxis : " + emptyTaxis.size());
//			
//			//System.out.println("Taille median fare : " + medianFare.size());
//			
//			//System.out.println("indice : " + ii);
//
//			
//			if (!medianFareGrid.contains(emptyTaxisGrid.get(ii)))
//			{
//						{
//								emptyTaxis.remove(ii);
//								ii++;
//						}
//				
//			}
//			
//			if (!emptyTaxisGrid.contains(medianFareGrid.get(ii)))
//			{
//				emptyTaxis.add(new PaireGridTaxi(medianFare.get(ii).getGrid(),0));
//				//System.out.println("X: "+emptyTaxis.get(ii).getGrid().getX()+ " Y: "+ emptyTaxis.get(ii).getGrid().getY());
//				ii++;
//				
//			}
//			else
//			{
//				
//				
//				ii++;
//			}	
//		}
		
			
		Collections.sort(emptyTaxis);
		Collections.sort(medianFare);
		
		for (int i=0; i<medianFare.size();i++)
		{
			if (emptyTaxis.get(i).getTaxi()!=0)
			{
			mostProfitableAreas.add(new QuadGridRatioTaxiMedian(medianFare.get(i).getGrid(),medianFare.get(i).getMoney()/emptyTaxis.get(i).getTaxi(),emptyTaxis.get(i).getTaxi(),medianFare.get(i).getMoney()));
			}
		}
		Collections.sort(mostProfitableAreas);
	}
	
	
	public void ecrireQuery2 (DebsRecord record)
	{
		
		Date pickup = new Date(record.getPickup_datetime());
		Date dropoff = new Date(record.getDropoff_datetime());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String lineTemp = "";
		
		// ***** Début de la ligne *****
		prefixe = ""+df.format(pickup).toString()+", "+ df.format(dropoff).toString()+" ";
		
		for (int i = 0; i<10;i++)
		{
			if (i<mostProfitableAreas.size())
			{
				String cellID = ""+mostProfitableAreas.get(i).getGrid().getX()+"."+ mostProfitableAreas.get(i).getGrid().getY();
				String empTax = ""+mostProfitableAreas.get(i).getTaxi() ;
				String median = ""+mostProfitableAreas.get(i).getMedian();
				if (i==0)
					lineTemp+= cellID +", "+ empTax + ", " + median;
				else
					lineTemp+=", "+ cellID +", "+ empTax + ", " + median;
			}
			else
			{
				if (i==0)
					lineTemp+="NULL";
				else
					lineTemp+=", " + "NULL";
				
			}
			
		}
		
		if (!lineTemp.equals(line))
		{
			line=lineTemp;
			writeQuery2(prefixe+", "+line+suffixe);
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
