package edu.ohiou.dynamic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
		int futureDays=state.currentDay+1;//=
		
		int cap=0;
		int futureDayPat=0;
		ArrayList<Patient> lastDayPat=new ArrayList <Patient>();
		ArrayList<Integer> evaluatedPat=new ArrayList <Integer>();
		ArrayList<Patient> prevDaysPat=new ArrayList <Patient>();
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
			    int ignoredPat=Math.max((count-cap), 0);
			    evaluatedPat.add(ignoredPat);
			    
			 //   result=result+Math.min(cap, count);
			
			futureDays++;				
		}
			
			for (int i=state.currentDay-1;i>=0;i--)
			{
				prevDaysPat.addAll(prevDaysPat);
			}
			int totalPrevDayPat=prevDaysPat.size();
			//int currPat= state.combinedSet.size();
			Object [] allPat=state.getAllPatient().toArray();
			int totalPat=allPat.length;
			result=(totalPat-totalPrevDayPat)-(Collections.max(evaluatedPat));
			//result=
	}
		return result;
	}
}
