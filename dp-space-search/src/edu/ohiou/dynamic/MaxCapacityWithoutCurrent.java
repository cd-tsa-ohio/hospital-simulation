package edu.ohiou.dynamic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.ohiou.mfgresearch.labimp.spacesearch.ComparableSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.HeuristicFunction;
//look at capacity without currentpatient
public class MaxCapacityWithoutCurrent implements HeuristicFunction {
	
	PeriodicProblemDay state;
	public MaxCapacityWithoutCurrent(ComparableSpaceState state)
	{
		  this.state = (PeriodicProblemDay)state;
	}
	public double evaluate() 
	{
		double result=0;
		if (state.canBeGoal()==true)
		{
		return result;	
		}
				
		
		for (int res=0; res<state.capacitylist.size();res++) {//going over resources
			int futureDays=state.currentDay+1;
			int cap=0;
			int futureDayPat=0;
			double prevResult=result;	
			ArrayList<Patient> lastDayPat=new ArrayList <Patient>();
		while (futureDays<=state.capacitylist.get(0).size())  // should this be  <= to include the last day
		{
			//int takenCapacity = 0;
			int patforNextDay=0;
			int count=0;					
				ArrayList<Patient> patients=state.map.get(futureDays);				
				if(state.map.get(futureDays)!=null)
				{
					lastDayPat.addAll(patients);
				}
			    for (Patient patient : lastDayPat) {
			      if(  patient.isStayingDay(futureDays)&& patient.resources.get(res)==1)//patient staying next day we get it
			      {
			    	  count=count+1; 
			      }
			    }        			
			    cap=state.capacitylist.get(res).get(futureDays-1);		
			    //cap=state.capacitylist.get(count).get(futureDays-1);
			    result = Math.max(result,  Math.max(0, count-cap));				    
			futureDays++;					
			
	}
		result=Math.max(result, prevResult);
		
		}
		int futurePat = state.futurePatientCount();
		return futurePat - result;
	}
}
