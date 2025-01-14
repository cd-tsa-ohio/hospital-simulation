package edu.ohiou.dynamic;

import java.util.ArrayList;
import java.util.Map;

import edu.ohiou.mfgresearch.labimp.spacesearch.ComparableSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.HeuristicFunction;

public class MaxPatOptimization implements HeuristicFunction {

	
	
		//ComparableSpaceState state;
		PeriodicProblemDay state;
		  public MaxPatOptimization (ComparableSpaceState state) {
		    this.state = (PeriodicProblemDay)state;
			 // this.state = state;
		  }
		@Override
		public double evaluate() {
			//double result=0+state.combinedSet.size();
			double result=0;
			if (state.canBeGoal()==true)
			{
			return result;	
			}
			int futureDays=state.currentDay+1;
			while (futureDays<state.capacity.size())  // should this be  <= to include the last day
			{
				//if ((futureDays+1)<state.capacity.size())
					
				
				if(state.map.get(futureDays)!=null)
				{
					//result=result+state.map.get(state.currentDay+1).size()+state.combinedSet.size();
					result=result+state.map.get(futureDays).size();
				
				}
				futureDays++;
					
			}
			return result;
		}
		
	

	}
