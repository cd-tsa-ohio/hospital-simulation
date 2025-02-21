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
		for (int res=0; res<state.capacitylist.size();res++) 
		{//going over resources
			int futureDays=state.currentDay+1;
			int cap=0;
			int futureDayPat=0;
			double prevResult=result;	
			ArrayList<Patient> lastDayPat=new ArrayList <Patient>();		
			while (futureDays<=state.capacitylist.get(0).size()) 
		{
			int takenCapacity = 0;
			int patforNextDay=0;
			int count=0;			
			
				ArrayList<Patient> patients=state.map.get(futureDays);
				
				if(state.map.get(futureDays)!=null)
				{
					lastDayPat.addAll(patients);
				}
			    for (Patient patient : lastDayPat) {
	
			      if(  patient.isStayingDay(futureDays)&& patient.resources.get(res)==1)
			      {
			    	  count=count+1; 
			      }
			    }  			    
			    for (Patient patient : state.acceptedPatients) {
			    	
				      if(  patient.isStayingDay(futureDays)&& patient.resources.get(res)==1)
				      {
				    	  takenCapacity=takenCapacity+1; 
				      }
				    } 
			    cap=state.capacitylist.get(res).get(futureDays-1)-takenCapacity;			    
			    result = Math.max(result,  Math.max(0, count-cap));
			futureDays++;				
		
			
	}
			result=Math.max(result, prevResult);
		}	
		int futurePat = state.futurePatientCount();
		return futurePat - result;
	}
}
