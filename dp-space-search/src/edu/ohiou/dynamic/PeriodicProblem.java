package edu.ohiou.dynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;


//import edu.ohiou.dynamic.DPExample1.Example2Panel;
//import edu.ohiou.dynamic.TestClass.Patients;

import edu.ohiou.mfgresearch.labimp.spacesearch.BlindSearcher;
import edu.ohiou.mfgresearch.labimp.spacesearch.ComparableSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.InformedSearcher;
import edu.ohiou.mfgresearch.labimp.spacesearch.Searchable;

public class PeriodicProblem extends ComparableSpaceState {

// parameters
	
	int remainingCap;
	// need to have decision day instead of decisions
	//variable for free capacity
	ArrayList <Integer> decisions= new ArrayList<Integer>();
	
	//static int data[][]= {{0,0,1,1,1},{0,1,1,1,0},{1,0,0,0,0},{0,1,1,1,0},{1,1,1,1,0}};
	static int data[][]= {{1,3},{1,2},{3,1},{4,1}};
	
	static int capacity []= {2,2,2,2,2};
	static  Map    map= new HashMap();
	static 	{printIndex = true;}
	//constructors
	public PeriodicProblem()
	{
		node = new DefaultMutableTreeNode(this);
		decisions=new ArrayList<Integer>();
	}
	public PeriodicProblem(PeriodicProblem s,int decision)
	{
		decisions=new ArrayList<Integer>(s.decisions);	
		decisions.add(decision);
		parent = s;
		node = new DefaultMutableTreeNode(this);
		if (decisions.size()<=3 && isFeasible()) {
			evaluate();
		}
	}//methods
	private boolean isFeasible() 
	{
		return true;
	}
	public void  createPatients ()
	{
		for (int i=0;i<data.length;i++) 
		{
			for (int j=0;j<=2; j++)
			{
				map.put(i, new Patients(data[i][j],data[i][j+1]));
			break;			
			}		
		}	

	}

	public static void main(String[] args)
	{

		PeriodicProblem ss = new PeriodicProblem();
		PeriodicProblem gs = new PeriodicProblem();
		BlindSearcher bs = new BlindSearcher (ss, gs);
		ss.createPatients();
		System.out.print(map);
		bs.setApplet();
		bs.display("Periodic Problem");
	}

	
	public String toString () 
	{
		return super.toString() + "DP"+ decisions + "->" + remainingCap;

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
//		for (int i=0;i<decisions.size();i++) {
//			minimumPAtient+= data[i][decisions.get(i)];								
//		}		 System.out.print("\n" + toString() + ">Total effectivness is" + minimumPAtient);
//		return minimumPAtient;
		int cap =2;
		remainingCap=0;
		
		for (int i=0;i<decisions.size();i++)
		{
			remainingCap=cap-remainingCap;
			cap=remainingCap;
		}
		return remainingCap;
	}

	
//classes	
class Example2Panel extends JPanel 
{
		
	}
public void init () 
{
	panel = new Example2Panel ();
}

class Patients
{
	 int arrivalDay;
	 int los;
	 public Patients(int a, int b) 
	 {
			this.arrivalDay =a;
			this.los=b;
			
	}
	 @Override
	 public String toString ()
	 {
	 return ( "day " + arrivalDay + " los " + los);
	 }


}
}

