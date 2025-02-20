package edu.ohiou.dynamic;

import java.util.ArrayList;
import java.util.Map;

import edu.ohiou.mfgresearch.labimp.spacesearch.ComparableSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.HeuristicFunction;

public class MaxPatOptimization implements HeuristicFunction {
		PeriodicProblemDay state;
		  public MaxPatOptimization (ComparableSpaceState state) {
		    this.state = (PeriodicProblemDay)state;
		  }
		@Override
		public double evaluate() {
			double result=0;
			if (state.canBeGoal()==true)
			{
			return result;	
			}
			int futureDays=state.currentDay+1;
			while (futureDays<=state.capacitylist.get(0).size())  
			{
				if(state.map.get(futureDays)!=null)
				{
					result=result+state.map.get(futureDays).size();				
				}
				futureDays++;				
			}
			return result;
		}
	}
