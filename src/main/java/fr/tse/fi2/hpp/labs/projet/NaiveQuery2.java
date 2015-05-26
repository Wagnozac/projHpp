package fr.tse.fi2.hpp.labs.projet;

import java.util.List;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.GridPoint;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class NaiveQuery2 extends AbstractQueryProcessor {
	
	private long delta = 900000; // 15 min en millisecondes.
	private List<DebsRecord> currentRecs = null;
	private List<String> currentResults = null;

	public NaiveQuery2(QueryProcessorMeasure measure, List<DebsRecord> currentRecs) {
		super(measure);
		
		this.currentRecs=currentRecs;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		float pickLat=record.getPickup_latitude();
		float pickLong= record.getPickup_longitude();
		float dropLat= record.getDropoff_longitude();
		float dropLong= record.getDropoff_longitude();
		GridPoint pick = convert(pickLat,pickLong);
		GridPoint drop = convert(dropLat,dropLong);
		currentRecs.add(record);
		int size =currentRecs.size();
		float medianMoney= 0f;
		for (int i = 0; i< size;i++)
		{
			if (record.getDropoff_datetime()>currentRecs.get(i).getDropoff_datetime()+delta)
			{
				currentRecs.remove(i);
			}
			
			if (pick == convert(currentRecs.get(i).getPickup_latitude(),currentRecs.get(i).getPickup_longitude()))
					{
						
					}
			
			

		}

	}

}
