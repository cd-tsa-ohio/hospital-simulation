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


public class PeriodicProblemDay extends ComparableSpaceState {
	// parameters

	int NextDayCap;

	int PatientTaken;
	int currentDay;
	// need to have decision day instead of decisions
	//variable for free capacity
	//array list of patients
	ArrayList <Patients> decisions= new ArrayList<Patients>();

	//static int data[][]= {{0,0,1,1,1},{0,1,1,1,0},{1,0,0,0,0},{0,1,1,1,0},{1,1,1,1,0}};
	static int data[][]= {{1,3},{1,2},{3,1},{4,1}};

	static int capacity []= {2,2,2,2,2};
	static  Map  <Integer,ArrayList<Patients>>  map= new HashMap <Integer,ArrayList<Patients>> ();
	static 	{printIndex = true;}
	//constructors
	public PeriodicProblemDay()
	{
		node = new DefaultMutableTreeNode(this);
		decisions=new ArrayList<Patients>();
	}
	public PeriodicProblemDay(PeriodicProblemDay s,ArrayList <Patients> decisions, int cd)
	{
		currentDay = cd;
		NextDayCap=capacity[cd];
		parent = s;
		this.decisions=decisions;
		nextDayCap();
		node = new DefaultMutableTreeNode(this);


	}
	//methods
	private boolean isFeasible() 
	{
		return true;
	}
	//this need to be static
	public void  createPatients ()
	{
		for (int i=0;i<data.length;i++) 
		{
			ArrayList<Patients> pat=map.get(data[i][0]);
			if (pat==null)
			{
				ArrayList<Patients> Npat=new ArrayList<Patients> ();
				Npat.add(new Patients(data[i][0],data[i][1]));
				map.put(data[i][0],Npat);
			}
			else
			{
				pat.add(new Patients(data[i][0],data[i][1]));
			}

		}	

	}
	private int nextDayCap()

	{
		for (Patients p:decisions)
		{
			if(p.isStayingDay(currentDay+1))
			{
				NextDayCap-=1;

			}
		}
	}

	public static void main(String[] args)
	{

		PeriodicProblemDay ss = new PeriodicProblemDay();
		PeriodicProblemDay gs = new PeriodicProblemDay();
		BlindSearcher bs = new BlindSearcher (ss, gs);
		ss.createPatients();
		System.out.print(map);
		bs.setApplet();
		bs.display("Periodic Problem");
	}


	public String toString () 
	{

		return super.toString() + "DP"+ decisions + "->" + currentDay + "#pat " + evaluate()
								+;



	}
	// parent class
	// make only feasible states
	//also needs to have date
	@Override


	//if nextDay  Cap=0
	//	then
	// create new State and copy all patients from previous state and new patient lost
	// call NextDayCap method 
	//

	public Set<Searchable> makeNewStates() 
	{
		Set<Searchable>  states=  new HashSet<Searchable>();		
		ArrayList <Patients> d2= new ArrayList <Patients> ();
		ArrayList <Patients> d1= map.get(currentDay+1);
		for (Patients p2 : decisions)
		{
			if(p2.isStayingDay(currentDay+1))

			{
				d2.add(p2);
			}
		}	
		if (d1 !=null)
		{
			for (Patients p : d1)
			{
				ArrayList <Patients> d3= new ArrayList <Patients> (d2);
				d3.add(p);
				states.add( new PeriodicProblemDay(this,d3,currentDay+1));
			}
		}
			
		
		return states;
	}

	@Override
	public boolean equals(Searchable s) {
		PeriodicProblemDay sse2 = (PeriodicProblemDay) s;
		// TODO Auto-generated method stub
		return  sse2.decisions==decisions && sse2.currentDay== currentDay ;
	}

	@Override
	public int[] setSearchTypes() {
		int [] searchTypes = {BlindSearcher.BEST_FIRST,BlindSearcher.DEPTH_FIRST};
		// TODO Auto-generated method stub
		return searchTypes;
	}

	@Override
	public double evaluate() 
	{		
		return decisions.size();
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
		public boolean isStayingDay(int day)
		{
			return day>=arrivalDay && day<arrivalDay+los;
		}


	}
}

