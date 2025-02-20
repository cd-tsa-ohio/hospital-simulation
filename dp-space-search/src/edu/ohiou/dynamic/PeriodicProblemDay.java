package edu.ohiou.dynamic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;


import javax.swing.*;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.poi.ss.usermodel.*;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.ohiou.mfgresearch.labimp.basis.ViewObject;
import edu.ohiou.mfgresearch.labimp.spacesearch.*;
import edu.ohiou.mfgresearch.labimp.table.ModelTable;
import edu.ohiou.mfgresearch.labimp.table.RectangularTableModel;
import edu.ohiou.mfgresearch.labimp.table.TableCellGenerator;

public class PeriodicProblemDay extends ComparableSpaceState {
	// parameters

	//int NextDayCap;
	int currentDay;
	static String XLSX_FOLDER;
	//static int data[][]= {{1,3},{1,2},{2,2},{3,1}};
	static List<int[]> data = new ArrayList<>();	  
    static List<List<Integer>> capacitylist = new ArrayList<>();  
    static List<int[]> resourcedata = new ArrayList<>();	
    static int resLabel = 11111111;
	//creating a map of patients objects with key value an integer
	static  Map  <Integer,ArrayList<Patient>>  map= new HashMap <Integer,ArrayList<Patient>> ();
	static 	{printIndex = true;}
   static  ArrayList <Patient> nd= new ArrayList <Patient> ();
   ArrayList <Patient> statePat= new ArrayList<Patient>();  
  Set<Patient> acceptedPatients = new HashSet<>();
  static String currentHeuristic="";
  double maxPatientToTake;
   static {
//	   XLSX_FOLDER = getProperty(PeriodicProblemDay.class, "XLSX_FOLDER");
   }
	//constructors 
	public PeriodicProblemDay()
	{
		node = new DefaultMutableTreeNode(this);
		//statePat=new ArrayList<Patients>();
	}
	public PeriodicProblemDay(PeriodicProblemDay s,ArrayList <Patient> decisions, int cd)
	{
		//removed on 9/26 found them redundany
//		if (currentDay < capacity.size())
//			
//		{
//			NextDayCap=capacity.get(cd-1); 
//			nextDayCap();
//		}
		currentDay = cd;	
		parent = s;
		this.acceptedPatients=new HashSet<Patient>(s.acceptedPatients) ;

		this.statePat=decisions;	
		this.acceptedPatients.addAll(decisions);
		
		node = new DefaultMutableTreeNode(this);
		
	}
	
	//methods
	private boolean isFeasible() 
	{
		return true;
	}
	  public double maxPatTaken() {
		    return maxPatientToTake;
		  }
	  public void calculateMaxPatToGoal() {
		    HeuristicFunction heuristic;
		    try {
		      if (this.currentHeuristic.equalsIgnoreCase(""))
		    	  heuristic = getHeuristic();
		      else 
		          heuristic = getHeuristic(this.currentHeuristic);
		      maxPatientToTake = heuristic.evaluate();
		    } catch (HeuristicException e) {
		    	maxPatientToTake = Double.NaN;
		    }
		      }
	//This is second thing- if it is last day (check)
	//Ways:size of the capacity is the day data()
		//patient with max day of the stay 	
	public boolean canBeGoal() {
		//boolean allPatientsExplored=statePat.isEmpty();
		return currentDay==capacitylist.get(0).size();
	}
	
//	public boolean isSearchComplete(SpaceSearcher ss) {
//		boolean lastDay=ss.getOpen().isEmpty();
//		return canBeGoal()&&lastDay;
//	}
	public boolean isBetterThan(Searchable inState) {
		DefaultSpaceState other = (DefaultSpaceState) inState;
		return this.evaluate() > other.evaluate();
		
	}
	
	public Comparator getComparator() {
		return new PDComparator();
	}

	//this need to be static
	
	public void  createPatients ()
	
	{
	
		for (int i=0;i<data.size();i++) 
		{
			//retrieving elements from data the 0 index their arrival day, their lenght of stay by 1
			  ArrayList<Integer> resReq =new ArrayList<Integer> ();
				for(int k=0;k<2;k++)
				{
					resReq.add(resourcedata.get(i)[k]);					
				}
			ArrayList<Patient> pat=map.get(data.get(i)[0]);	
			if (pat==null)
			{	
				//we create a patient with arrival day and los, and put that in the map with the key as arrival day
				//ArrayList<Patient> Npat=new ArrayList<Patient> ();
				ArrayList<Patient> Npat=new ArrayList<Patient> ();
				//Npat.add(new Patient(data.get(i)[0],data.get(i)[1]));
				Npat.add(new Patient(data.get(i)[0],data.get(i)[1],resReq));		
				map.put(data.get(i)[0],Npat);
			}
			else
			{	
				//otherwise we create new patient add this into the existing patients who arrive on that day
				pat.add(new Patient(data.get(i)[0],data.get(i)[1],resReq));
			}

		}	

	}
	
	int futurePatientCount() {
		
		int patientCount = 0;
			for (Map.Entry<Integer,ArrayList<Patient>>entry : map.entrySet())
			{					
				if (entry.getKey() > this.currentDay ) {
					patientCount += entry.getValue().size();
				}
			}		
		return patientCount;
	}
	
//	private int nextDayCap()
//
//	{
//		if (currentDay>=capacity.size())
//				{
//			return 0;
//				}
//		NextDayCap=capacity.get(currentDay);
//		//statepat is the arraylist of patient currently staying in the hospital
//		for (Patient p:statePat)
//		{
//			if(p.isStayingDay(currentDay+1))
//			{
//				NextDayCap-=1;
//
//			}
//		}
//	
//		return NextDayCap;
//		
//	}
	public boolean isNotFeasible2( )
	{	
	
			for (int res=0; res<capacitylist.size();res++)
			{
				int totalResReq=0;


				for (Patient p:statePat)
				{
					if((p.resources.get(res)==1))

					{
						totalResReq+=1;

					}
				}
				if (nextDayCap2(res, currentDay, totalResReq)<0)
				{
					return true; 
				}
			}			
		return false;
	}
	private int nextDayCap2(int resource ,int day,int num)

	{		
		int capacityAvailable=capacitylist.get(resource).get(day) - num;
		return capacityAvailable  ;
		
	}
	
	//excel read
	
//this method getting values from excel	

	public static void main(String[] args) 
	{

		PeriodicProblemDay is = new PeriodicProblemDay();
		//represent starting state witht eh current day set to 0
		is.currentDay = 0;
		PeriodicProblemDay gs = new PeriodicProblemDay();
		//represent goal state of the problem with currrentday set to 10
		//9/24/ need to look at this 
		//gs.currentDay = 6;
		SpaceSearcher ss = null;
		String searchString = "BLID";
		
		if (searchString .equalsIgnoreCase("BLIND")) {
			ss = new BlindSearcher (is, gs);
		}
		else {
			ss = new InformedSearcher (is, gs);
		}	
		//calling createpatient on ss instance this will populat the map
		
		//System.out.print(map);
		//System.out.print(getALlPatient());
		try {
			ExcelRead.getData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		is.createPatients();
		is.display();
		ss.setApplet();
		//ss.display(ss.toString() + " Periodic Problem with the capacity " + (capacity));
		ss.display(ss.toString() + " Periodic Problem with the capacity " + (capacitylist));
	//	ss.setSearchOrder(SpaceSearcher.BEST_FIRST);
//		ss.runOptSpaceSearch(3);
//
		ZonedDateTime startTime = ZonedDateTime.now();
	//	Searchable res = ss.runSpaceSearch(SpaceSearcher.REACH_GOAL);
		
		ZonedDateTime endTime = ZonedDateTime.now();
//		System.out.println("DNS 2025 Result " + res);
		System.out.println("DNS 2025 Duration from " + startTime + " to " + endTime);
//	((ComparableSpaceState) res).display();
		
	}

//
	public String toString () 
	{		
		return super.toString() + "PPD"+ "->" + currentDay +  ","  + "Taken->"+ evaluate() +" CanBe->" + maxPatientToTake +" Total->" + (evaluate ()+ maxPatientToTake) ;
		//return super.toString() + "PPD"+ "->" + currentDay + ",pat " + statePat + ","  + evaluate() ;
	}	
//	public Set<Searchable> makeNewStatesOld() 
//	{			
//		//to store new states in states
//		Set<Searchable>  states=  new HashSet<Searchable>();	
//		
//		System.out.println("The current day is "+ this.toString()+" ..."+ currentDay );		
//		if (this.currentDay==capacity.size())
//		{		
//			//this.calculateMaxPatToGoal();
//			return states;
//		}
//		// dns 092023 test if we are in the last day.
//		//arraylist of patients who are my new state patients
//		ArrayList <Patient> newStPat= new ArrayList <Patient> ();
//		ArrayList <Patient> nextDyPat= map.get(currentDay+1);
//		ArrayList <Patient> previousDyPat= new ArrayList <Patient> ();	
//		//iterating over statepat which stores information of all the patient based on current state 
//		for (Patient p2 : statePat)
//		{	//patients are staying next day we are adding them to newstpat
//			if(p2.isStayingDay(currentDay+1))
//			{
//				newStPat.add(p2);
//			}								
//		}	
//		//in every new state 
//		PeriodicProblemDay ts= new  PeriodicProblemDay(this,newStPat,currentDay+1);
//		states.add(ts);
//		ts.calculateMaxPatToGoal();
//		if (nextDyPat !=null)
//		{
//			int nextDayCap= nextDayCap();			
//			if (nextDayCap>0)				
//			{
//			List<Patient> nxtDyPatIter = nextDyPat;
//			List<List<Patient>> nextDayComb=new ArrayList <>();
//			
//			try {	
//				for (int i=1;i<=nextDayCap;i++)
//				{
//				List<List<Patient>> combinations = Comb.createCombinations(nxtDyPatIter, i);
//				nextDayComb.addAll(combinations);
//	                
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			for (List<Patient> ndi :nextDayComb)
//			{
//				ArrayList <Patient> allNewPat= new ArrayList <Patient> (newStPat);
//				for (Patient i : ndi )
//			{					
//				allNewPat.add(i);
//			}
//				PeriodicProblemDay ps= new  PeriodicProblemDay(this,allNewPat,currentDay+1);
//				states.add(ps);
//				//states.add( new PeriodicProblemDay(this,allNewPat,currentDay+1));		
//				ps.calculateMaxPatToGoal();
//			}
//		}
//		
//		}	
//		
//		return states;
//	}
	public Set<Searchable> makeNewStates() 
	{			
		//to store new states in states
		Set<Searchable>  states=  new HashSet<Searchable>();	
		System.out.println("The current day is "+ this.toString()+" ..."+ currentDay );		
		if (this.currentDay==capacitylist.get(0).size())
		{		
			//this.calculateMaxPatToGoal();
			return states;
		}
		// dns 092023 test if we are in the last day.
		//arraylist of patients who are my new state patients
		ArrayList <Patient> newStPat= new ArrayList <Patient> ();
		ArrayList <Patient> nextDyPat= map.get(currentDay+1);
	
		ArrayList <Patient> previousDyPat= new ArrayList <Patient> ();	
		
		//iterating over statepat which stores information of all the patient based on current state 
		for (Patient p2 : statePat)
		{	//patients are staying next day we are adding them to newstpat
			if(p2.isStayingDay(currentDay+1))
			{
				newStPat.add(p2);
				
			}								
		}	
		//in every new state 
		PeriodicProblemDay ts= new  PeriodicProblemDay(this,newStPat,currentDay+1);
		states.add(ts);
		ts.calculateMaxPatToGoal();
		if (nextDyPat !=null)
		{		
			{
			List<Patient> nxtDyPatIter = nextDyPat;
			List<List<Patient>> nextDayComb=new ArrayList <>();
			
			try {	
				for (int i=1;i<=nxtDyPatIter.size();i++)
				{
				List<List<Patient>> combinations = Comb.createCombinations(nxtDyPatIter, i);
				nextDayComb.addAll(combinations);
	                
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (List<Patient> ndi :nextDayComb)
			{
				ArrayList <Patient> allNewPat= new ArrayList <Patient> (newStPat);
			//	if (isNotFeasible2(ndi)==false)
				{
			//	for (Patient i : ndi )
			{					
			//	allNewPat.add(i);
			allNewPat.addAll(ndi); // uncomment this
			}
				}
				PeriodicProblemDay ps= new  PeriodicProblemDay(this,allNewPat,currentDay+1);

//				if (!ps.isNotFeasible2(ndi)) { // uncomment this
				//states.add(ps);
//				}                               // uncomment this
			//ps.calculateMaxPatToGoal();

				if (!ps.isNotFeasible2())
				{
				states.add(ps);
				}
			ps.calculateMaxPatToGoal();

					
			}
		}
		
		}	
		
		return states;
	}
	
	@Override
	public boolean equals(Searchable s) {
		PeriodicProblemDay sse2 = (PeriodicProblemDay) s;
		// TODO Auto-generated method stub
		boolean res = sse2.statePat==statePat && sse2.currentDay== currentDay ;

		return  res;
	}
	
	public int hashCode() {
		return  statePat.hashCode() + currentDay;	
	}

	@Override
	public int[] setSearchTypes() {
		int [] searchTypes = {BlindSearcher.BREADTH_FIRST,BlindSearcher.DEPTH_FIRST, SpaceSearcher.BEST_FIRST, SpaceSearcher.ASTARALGORITHM};
		return searchTypes;
	}

	@Override
	public double evaluate() 
	{	
		return acceptedPatients.size();

	}

	public void init () 
	{		
			panel = new PeriodicDayPanel ();
	}
	
	public String toToolTipString() {
		return "I am a toolTipString";
	}

// get all patient method
	public static ArrayList<Patient> getAllPatient ()
	{
		ArrayList <Patient>  patientList= new ArrayList();
		for (Map.Entry<Integer,ArrayList<Patient>>entry : map.entrySet())
		{
		{			
			patientList.addAll(entry.getValue());	
		}
		}
	 return patientList;
	}

	
//classes	
	
	class PDCellRenderer extends DefaultTableCellRenderer  {

		 public PDCellRenderer() {
			// TODO Auto-generated constructor stub
		}
		 public Component getTableCellRendererComponent(
				 JTable table, Object value,
				 boolean isSelected, boolean hasFocus,
				 int row, int column) {

			 Component component = super.getTableCellRendererComponent
					 (table, value, isSelected, hasFocus, row, column);
			 // do not color resource column
//			 if (column ==1) {
//				 component.setBackground(Color.white);
//				 return component;
//			 }
			 int number = 0;
			 try {
				 number = (Integer) value;
			 } catch (Exception e) {
//				 System.out.println("Never leave the empty catch clause, value is " + value);
			 }
			 // color yellow patients and days when resource is required
			 if ( number == 1) { 
				 component.setBackground(Color.yellow);
			 }
			 else {
				 component.setBackground(Color.white);
			 }
			 Patient p= (Patient) table.getValueAt(row, 0);
			 if (p instanceof PatientCopy) {
				 PatientCopy pc = (PatientCopy) p;
				 p = pc.patient;
			 }
			 // color accepted patients green
			 if (number==1 && acceptedPatients.contains(p) ) {
				 component.setBackground(Color.green);
			 }
			 else {
				 // color eerlier not-accepted patients red
				 if (number==1 && p.arrivalDay<=currentDay) {
					 component.setBackground(Color.red);
				 }
				 else {
					 // color the next day patient choice cyan
					 if (number==1 && (column==currentDay+2)) {
						 component.setBackground(Color.cyan);
					 }
				 }
			 }
			 // make different shades for different resources
			 int numResources = capacitylist.size();
			 int i = row % numResources;
			 int colorLevel = 255 - i * 10;
			 if (number == 0 || column == 1) { 
				 component.setBackground(new Color (colorLevel, colorLevel, colorLevel));
			 }
			 // to make current day stand out
			 if (number == 0 && currentDay == column - 1) {
				 component.setBackground(Color.lightGray);
			 }
				 
			 return component;
		 }
	}

class PeriodicDayPanel extends JPanel 
{
	public PeriodicDayPanel () 
	{
		Object [] modelPat=getAllPatient().toArray();
		ArrayList <Integer>  daysList= new ArrayList();
		daysList.add(resLabel);
		for(int i=1;i<=capacitylist.get(0).size();i++) {
			daysList.add(i);
		}
		Object [] columns = daysList.toArray();		
		
		ArrayList<Patient> multiPat = new ArrayList<Patient>();
		for (Patient p : getAllPatient()) {
			for (int i = 0; i < p.resources.size(); i++) {
				if (i == 0) {
					multiPat.add(p);
				} else {
					multiPat.add(new PatientCopy(p, i+1));
				}
			}
		}
		
		modelPat = multiPat.toArray();
		
		Object [] statePat=acceptedPatients.toArray();
		RectangularTableModel problemTM = new RectangularTableModel (modelPat, columns, new PDGenerator());
		RectangularTableModel stateTM = new RectangularTableModel (statePat, columns, new PDGenerator());
		ModelTable problemTable = new ModelTable(problemTM);
		problemTable.setDefaultRenderer(Integer.class, new PDCellRenderer());
		problemTable.setDefaultRenderer(String.class, new PDCellRenderer());
		problemTable.setDefaultRenderer(Object.class,new PDCellRenderer());
	//	ModelTable stateTable = new ModelTable(stateTM);
		//stateTable.setDefaultRenderer(Integer.class, new PDCellRenderer());
		boolean  useSplitPane = false;
		setLayout(new BorderLayout());
		if (useSplitPane) {
//		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
//				new JScrollPane(problemTable), new JScrollPane(stateTable));
//		splitPane.setOneTouchExpandable(true);
//		splitPane.setDividerLocation(150);
	
	//	this.add(splitPane,BorderLayout.CENTER);

		}
		else {
			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Problem table", null, new JScrollPane(problemTable),
	                  "Display initial problem table, where checkmarks show when patients need resources");
	//		JScrollPane stateScrollPane = new JScrollPane(stateTable);
			
//			tabbedPane.addTab("State table", null, stateScrollPane,
//	                  "Display selected state table, where rows show currently selected patients in the state" 
//	                		  + "and checkmarks show when selected patients need resources"); 
			JPanel interimPanel = new JPanel();
			interimPanel.add(problemTable);
			this.add(new JScrollPane(problemTable),BorderLayout.CENTER); //  interimPanel 
	//		tabbedPane.setSelectedComponent(stateScrollPane);
			try {
				this.add(new JLabel(getHeuristic().getClass().getName()),BorderLayout.SOUTH);
			} catch (HeuristicException e) {
				// TODO Auto-generated catch block
				this.add(new JLabel("Heruistic not defined"),BorderLayout.SOUTH);
			}
		}
	}	
}
//implement a static method getallPatients and in panel i call the method


// give name in patient class make variable

}

class PDGenerator implements TableCellGenerator {

	@Override
	public Object makeTableCell(Object o1, Object o2) {
		Patient p1 = (Patient) o1;		
		Integer i2 = (Integer) o2;
		// TODO Auto-generated method stub
		if (i2 ==PeriodicProblemDay.resLabel) {
			if (p1 instanceof PatientCopy) {
				PatientCopy pc = (PatientCopy) p1;
				return pc.resource;
			}
			else {
				return 1;
			}
		}
		if (p1 instanceof PatientCopy) {
			PatientCopy pc = (PatientCopy) p1;
			return  (pc.isStayingDay(i2) && pc.usesResource()) ? 1 : " ";
		}
		else {
			return  (p1.isStayingDay(i2) && p1.usesResource(0)) ? 1 : " ";
		}
	}

	@Override
	public void updateRelation(Object o1, Object o2, Object dataValue) {
		// TODO Auto-generated method stub
		
	}
	
}

class PDComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		if (((Searchable)o1).equals((Searchable)o2))
			return 0;
		PeriodicProblemDay pd1 = (PeriodicProblemDay) o1;
		PeriodicProblemDay pd2 = (PeriodicProblemDay) o2;
		if (pd1.evaluate() + pd1.maxPatientToTake <= pd2.evaluate() + pd2.maxPatientToTake) {
		        
			return 1; }
		else
		{	return -1; }
	}
	
	public boolean equals(Object o1, Object o2) {
		PeriodicProblemDay pd1 = (PeriodicProblemDay) o1;
		PeriodicProblemDay pd2 = (PeriodicProblemDay) o2;
		// TODO Auto-generated method stub
		boolean res = pd1.statePat==pd2.statePat && pd2.currentDay== pd2.currentDay ;
		return  res;
	}
}

class Patient
{
	int arrivalDay;
	int los;
	String name;
	static int count=0;
//	int resource1;
//	int resource2;
	 ArrayList<Integer> resources ;
	//List<int[]> resources;	
	public Patient () {
		
	}
	 
	 public boolean usesResource(int res) {
		// TODO Auto-generated method stub
		return resources.get(res) == 1;
	}

	public Patient(int a, int b) 
	{
		count ++;
		this.name="P"+ count;
		this.arrivalDay =a;
		this.los=b;

	}
	public Patient(int a, int b,ArrayList<Integer> resources) 
	{
		count ++;
		this.name="P"+ count;
		this.arrivalDay =a;
		this.los=b;
		this.resources=resources;
		//this.resource2=d;

	}
	@Override
	public String toString ()
	{
		
		//return (  name +" <arr " + arrivalDay + ",los " + los +">"+"res1"+">"+resource1+"res2"+">"+resource2);
		//for (int i = 0; i < resources.size(); i++) {
			int resource1 = resources.get(0);
	        int resource2 =resources.get(1);
			
		//}
		return (  name +" <arr " + arrivalDay + ",los " + los +","+"R:"+ resources + ">");
	}
	
	public boolean isStayingDay(int day)
	{
		return day>=arrivalDay && day<arrivalDay+los;
	}

//	public boolean isRequiresResource1(Patient a)
//	{
//		return a.resource1==1 ;
//	}
//	public boolean isRequiresResource2(Patient a)
//	{
//		return a.resource2==1 ;
//	}

	public String toToolTipString() {
		return name;
	}
	
	
}

class PatientCopy extends Patient {
	int resource;
	Patient patient;
	
	public PatientCopy (Patient p, int res) {
		patient = p;
		resource = res;	
	}
	
	public String toString () {
		return "";
	}
	public boolean isStayingDay(int day)
	{
		return patient.isStayingDay(day);
	}
	
	 public boolean usesResource() {
		// TODO Auto-generated method stub
		return patient.resources.get(resource-1) == 1;
	}
}



