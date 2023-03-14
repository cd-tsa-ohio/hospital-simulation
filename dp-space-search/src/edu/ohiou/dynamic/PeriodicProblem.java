package edu.ohiou.dynamic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.ohiou.dynamic.DPExample1.Example2Panel;
import edu.ohiou.mfgresearch.labimp.spacesearch.BlindSearcher;
import edu.ohiou.mfgresearch.labimp.spacesearch.ComparableSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.InformedSearcher;
import edu.ohiou.mfgresearch.labimp.spacesearch.Searchable;

public class PeriodicProblem extends ComparableSpaceState {
// parameters
	
	double minimumPAtient;
	// need to have decision day instead of decisions
	//variable for free capacity
	ArrayList <Integer> decisions= new ArrayList<Integer>();
	
	static int data[][]= {{0,0,1,1,1},{0,1,1,1,0},{1,0,0,0,0},{0,1,1,1,0},{1,1,1,1,0}};
	;
	static int capacity []= {2,2,2,2,2};
	static 	{printIndex = true;}
	//constructors
	public PeriodicProblem() {
		node = new DefaultMutableTreeNode(this);
		decisions=new ArrayList<Integer>();
	}
	public PeriodicProblem(PeriodicProblem s,int decision) {
		decisions=new ArrayList<Integer>(s.decisions);	
		decisions.add(decision);
		parent = s;
		node = new DefaultMutableTreeNode(this);
		if (decisions.size()<=5 && isFeasible()) {
			evaluate();
		}
	}//methods
	private boolean isFeasible() {
		int sum=0;
		for (int i: decisions) {
			sum+=i;
			
		}
	
		return sum<=5 && decisions.size()<=5;
	}

	public static void main(String[] args) {

		PeriodicProblem ss = new PeriodicProblem();
		PeriodicProblem gs = new PeriodicProblem();
		BlindSearcher bs = new BlindSearcher (ss, gs);
		bs.setApplet();
		bs.display("Periodic Problem");
	}
	
	public String toString () {
		return  super.toString() +"aba";
	}
// parent class
	// make only feasible states
	//also needs to have date
	@Override
	public Set<Searchable> makeNewStates() {
Set<Searchable>  states=  new HashSet<Searchable>();
		
		
		
		return states;
	}

	@Override
	public boolean equals(Searchable s) {
		PeriodicProblem sse2 = (PeriodicProblem) s;
		// TODO Auto-generated method stub
		return  decisions.equals(sse2.decisions);
	}

	@Override
	public int[] setSearchTypes() {
		int [] searchTypes = {BlindSearcher.BEST_FIRST,BlindSearcher.DEPTH_FIRST};
		// TODO Auto-generated method stub
		return searchTypes;
	}
	
	@Override
	public double evaluate() {
		for (int i=0;i<decisions.size();i++) {
			minimumPAtient+= data[i][decisions.get(i)];								
		}		 System.out.print("\n" + toString() + ">Total effectivness is" + minimumPAtient);
		return minimumPAtient;
	}
	
	
class Example2Panel extends JPanel {
		
	}
public void init () {
	panel = new Example2Panel ();
}
}
