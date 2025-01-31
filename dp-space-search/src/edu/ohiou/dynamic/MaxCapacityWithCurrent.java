package edu.ohiou.dynamic;

import java.util.ArrayList;

import edu.ohiou.mfgresearch.labimp.spacesearch.ComparableSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.HeuristicFunction;

public class MaxCapacityWithCurrent implements HeuristicFunction {
	PeriodicProblemDay state;
	public MaxCapacityWithCurrent(ComparableSpaceState state)
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
		//int futureDayPat=0;
		
		ArrayList<Patient> lastDayPat=new ArrayList <Patient>();
		while (futureDays<=state.capacity.size())  // should this be  <= to include the last day
		{
			int takenCapacity = 0;
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
			    
			    for (Patient patient : state.acceptedPatients) {
			    	
				      if(  patient.isStayingDay(futureDays))//patient staying next day we get it
				      {
				    	  takenCapacity=takenCapacity+1; 
				      }
				    } 
			    cap=state.capacity.get(futureDays-1)-takenCapacity;
			    
			    result = Math.max(result,  Math.max(0, count-cap));
			    
//			    result=result+Math.min(cap, count);
			
			futureDays++;				
		}
	}
		int futurePat = state.futurePatientCount();
		return futurePat - result;
	}
}
