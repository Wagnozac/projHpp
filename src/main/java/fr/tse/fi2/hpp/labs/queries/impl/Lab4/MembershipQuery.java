package fr.tse.fi2.hpp.labs.queries.impl.Lab4;

import java.util.ArrayList;
import java.util.List;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class MembershipQuery extends AbstractQueryProcessor {

	private List<DebsRecord> recs = null;

	public MembershipQuery(QueryProcessorMeasure measure) {
		super(measure);
		recs = new ArrayList<>();
	}

	@Override
	protected void process(DebsRecord record) {
		recs.add(record);

	}

	public List<DebsRecord> getRecs() {
		return recs;
	}

	public void setRecs(List<DebsRecord> recs) {
		this.recs = recs;
	}

	
	public boolean searchRec (float longD, float latD, float longA, float latA,  String plaque ){
		
		for (int i =0; i<recs.size();i++){
			
			DebsRecord a=recs.get(i);
				if (a.getPickup_longitude()==longD){
					if (a.getPickup_latitude()==latD){
						if (a.getDropoff_longitude()==longA){
							if (a.getDropoff_latitude()==latA){
								if (a.getHack_license().equals(plaque)){
									return true;
								}		
							}
						}		
					}
				}
					
			
		}
		return false;
		
		
		
	}
	
}