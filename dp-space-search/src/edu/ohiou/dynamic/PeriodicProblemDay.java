package edu.ohiou.dynamic;

import java.awt.BorderLayout;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.tree.DefaultMutableTreeNode;


//import edu.ohiou.dynamic.DPExample1.Example2Panel;
//import edu.ohiou.dynamic.TestClass.Patients;

import edu.ohiou.mfgresearch.labimp.spacesearch.BlindSearcher;
import edu.ohiou.mfgresearch.labimp.spacesearch.ComparableSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.InformedSearcher;
import edu.ohiou.mfgresearch.labimp.spacesearch.Searchable;
import edu.ohiou.mfgresearch.labimp.spacesearch.SpaceSearcher;
import edu.ohiou.mfgresearch.labimp.table.ModelTable;
import edu.ohiou.mfgresearch.labimp.table.RectangularTableModel;
import edu.ohiou.mfgresearch.labimp.table.TableCellGenerator;


public class PeriodicProblemDay extends ComparableSpaceState {
	// parameters

	int NextDayCap;

	int PatientTaken;
	int currentDay;
	// need to have decision day instead of decisions
	//variable for free capacity
	//array list of patients
	ArrayList <Patients> statePat= new ArrayList<Patients>();

	//static int data[][]= {{0,0,1,1,1},{0,1,1,1,0},{1,0,0,0,0},{0,1,1,1,0},{1,1,1,1,0}};
//	static int data[][]= {{1,3},{1,2},{3,1},{4,1}};
	static int data[][]= {{1,3},{1,2},{2,1},{3,1}};

	static int capacity []= {2,2,2,2,1,1,1};
	static  Map  <Integer,ArrayList<Patients>>  map= new HashMap <Integer,ArrayList<Patients>> ();
	static 	{printIndex = true;}
   static  ArrayList <Patients> nd= new ArrayList <Patients> ();
   static ArrayList <Patients> allAceepted= new ArrayList <Patients> ();
   
	//constructors
	public PeriodicProblemDay()
	{
		node = new DefaultMutableTreeNode(this);
		statePat=new ArrayList<Patients>();
//		panel = new PeriodicDayPanel ();
	}
	public PeriodicProblemDay(PeriodicProblemDay s,ArrayList <Patients> decisions, int cd)
	{
		currentDay = cd;
		NextDayCap=capacity[cd]; // it is next day, but array index starts at 0
		parent = s;
		this.statePat=decisions;
		nextDayCap();
		node = new DefaultMutableTreeNode(this);
	}

	//methods
	private boolean isFeasible() 
	{
		return true;
	}
	
	public boolean canBeGoal() {
		return false;
	}
	
	public boolean isSearchComplete(SpaceSearcher ss) {
		return false;
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
		NextDayCap=capacity[currentDay+1];
		for (Patients p:statePat)
		{
			if(p.isStayingDay(currentDay+1))
			{
				NextDayCap-=1;

			}
		}
		return NextDayCap;
		
	}

	public static void main(String[] args)
	{

		PeriodicProblemDay ss = new PeriodicProblemDay();
		ss.currentDay = 0;
		PeriodicProblemDay gs = new PeriodicProblemDay();
		gs.currentDay = 10;
		
		BlindSearcher bs = new BlindSearcher (ss, gs);
		
		ss.createPatients();
		System.out.print(map);
		//System.out.print(getALlPatient());
		bs.setApplet();
		bs.display("Periodic Problem");
	}


	public String toString () 
	{


		return super.toString() + "PPD"+ "->" + currentDay + ",pat " + statePat + ","  + evaluate()+"flagged patient "+nd ;




	}
	// parent class
	// make only feasible states
	//also needs to have date
	@Override


	//if nextDay  Cap=0
	//	then
	// create new State and copy all patients from previous state and new patient lost
	// call NextDayCap method 
	// find code for permutation and combination and test it independently

	public Set<Searchable> makeNewStates() 
	{
		Set<Searchable>  states=  new HashSet<Searchable>();		
		ArrayList <Patients> newStPat= new ArrayList <Patients> ();
		ArrayList <Patients> nextDyPat= map.get(currentDay+1);
	
		
		for (Patients p2 : statePat)
		{
			if(p2.isStayingDay(currentDay+1))

			{
				newStPat.add(p2);
			}
		}	
		states.add(new PeriodicProblemDay(this,newStPat,currentDay+1));
		if (nextDyPat !=null)
		{
			
			for (Patients p : nextDyPat)
			{
				ArrayList <Patients> allNewPat= new ArrayList <Patients> (newStPat);
				
				if (nextDayCap()>0)
				{
					allNewPat.add(p);
		
				}	
				else
				{
					nd.add(p);
				}
				states.add( new PeriodicProblemDay(this,allNewPat,currentDay+1));
				allAceepted.addAll(allNewPat);
			}
		}
			
		
		return states;
	}

	@Override
	public boolean equals(Searchable s) {
		PeriodicProblemDay sse2 = (PeriodicProblemDay) s;
		// TODO Auto-generated method stub
		return  sse2.statePat==statePat && sse2.currentDay== currentDay ;
	}
	
	public int hashCode() {
		return  statePat.hashCode() + currentDay;	
	}

	@Override
	public int[] setSearchTypes() {
		int [] searchTypes = {BlindSearcher.BREADTH_FIRST,BlindSearcher.DEPTH_FIRST};
		// TODO Auto-generated method stub
		return searchTypes;
	}

	@Override
	public double evaluate() 
	{		
		return statePat.size();
	}
	
	public void init () 
	{		
			panel = new PeriodicDayPanel ();
	}

// get all patient method
	public static ArrayList<Patients> getAllPatient ()
	{
		ArrayList <Patients>  p= new ArrayList();
		for (int i =1;i<=map.size();i++)
		{
			p.addAll( map.get(i));
		
		}
	 return p;
	}

	
//classes	
class PeriodicDayPanel extends JPanel 
{
	public PeriodicDayPanel () {
		ArrayList  p= new ArrayList  ();
		
		Object [] ps=getAllPatient().toArray();
				
		Object [] days  = {1,2,3,4,5};
		Object [] ps2=statePat.toArray();
		RectangularTableModel rtm = new RectangularTableModel (ps, days, new PDGenerator());
		RectangularTableModel rtm2 = new RectangularTableModel (ps2, days, new PDGenerator());
    
	ModelTable mt = new ModelTable(rtm);
	ModelTable mt2 = new ModelTable(rtm2);
	this.add(new JScrollPane(mt), BorderLayout.CENTER);
	this.add(new JScrollPane(mt2),BorderLayout.NORTH);
	//this.add(new JLabel(statePat.toString()),BorderLayout.SOUTH);
	}
	
	
	}
//implement a static method getallPatients and in panel i call the method

class PDGenerator implements TableCellGenerator {

	@Override
	public Object makeTableCell(Object o1, Object o2) {
		Patients p1 = (Patients) o1;
		Integer i2 = (Integer) o2;
		// TODO Auto-generated method stub
		
		return  p1.isStayingDay(i2);
	}

	@Override
	public void updateRelation(Object o1, Object o2, Object dataValue) {
		// TODO Auto-generated method stub
		
	}
	
}



// give name in patient class make variable

}
class Patients
{
	int arrivalDay;
	int los;
	String name;
	static int count=0;
	public Patients(int a, int b) 
	{
		count ++;
		this.name="P"+ count;
		this.arrivalDay =a;
		this.los=b;

	}
	@Override
	public String toString ()
	{
		return (  name +" <arr " + arrivalDay + ",los " + los +">");
	}
	public boolean isStayingDay(int day)
	{
		return day>=arrivalDay && day<arrivalDay+los;
	}


}

