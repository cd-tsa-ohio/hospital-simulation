package edu.ohiou.dynamic;

import java.util.ArrayList;
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

	@Override
	public double evaluate() 
	{
		double result=0;
		if (state.canBeGoal()==true)
		{
		return result;	
		}
		int futureDays=state.currentDay+1;//=2
		int cap=0;
		int futureDayPat=0;
		ArrayList<Patient> lastDayPat=new ArrayList <Patient>();
		while (futureDays<=state.capacity.size())  // should this be  <= to include the last day
		{
			
			int patforNextDay=0;
			int count=0;
		
			{
				ArrayList<Patient> patients=state.map.get(futureDays);
				
				if(state.map.get(futureDays)!=null)
				{
					lastDayPat.addAll(patients);
				}
			    for (Patient patient : lastDayPat) {
	
			      if(  patient.isStayingDay(futureDays))//patient staying next day we get it
			      {
			    	  count=count+1; 
			      }
			    }        
			
			    cap=state.capacity.get(futureDays-1);;	
			    result=result+Math.min(cap, count);
			
			futureDays++;				
		}

	}
		return result;
	}
}
