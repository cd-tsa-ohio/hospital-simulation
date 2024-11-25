package edu.ohiou.dynamic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.poi.ss.usermodel.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.sun.org.apache.xpath.internal.operations.Bool;
import edu.ohiou.mfgresearch.labimp.basis.ViewObject;
import edu.ohiou.mfgresearch.labimp.spacesearch.BlindSearcher;
import edu.ohiou.mfgresearch.labimp.spacesearch.ComparableSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.DefaultSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.InformedSearcher;
import edu.ohiou.mfgresearch.labimp.spacesearch.Searchable;
import edu.ohiou.mfgresearch.labimp.spacesearch.SpaceSearcher;
import edu.ohiou.mfgresearch.labimp.table.ModelTable;
import edu.ohiou.mfgresearch.labimp.table.RectangularTableModel;
import edu.ohiou.mfgresearch.labimp.table.TableCellGenerator;
import java.util.stream.IntStream;
public class PeriodicProblemDay extends ComparableSpaceState {
	// parameters

	int NextDayCap;
	int PatientTaken;
	int currentDay;
	int EvaluatePatients;
	// need to have decision day instead of decisions
	//variable for free capacity
	//array list of patients	
	static String XLSX_FOLDER;
	//static int data[][]= {{1,3},{1,2},{2,2},{3,1}};
	static List<int[]> data = new ArrayList<>();	
    static ArrayList <Integer> capacity =new ArrayList<>();
	//creating a map of patients objects with key value an integer
	static  Map  <Integer,ArrayList<Patient>>  map= new HashMap <Integer,ArrayList<Patient>> ();
	static 	{printIndex = true;}
   static  ArrayList <Patient> nd= new ArrayList <Patient> ();
   ArrayList <Patient> allAceepted= new ArrayList <Patient> ();
 //this holds informaiton about patients
   ArrayList <Patient> statePat= new ArrayList<Patient>();  
  Set<Patient> combinedSet = new HashSet<>();
  
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
		this.combinedSet=new HashSet<Patient>(s.combinedSet) ;

		this.statePat=decisions;	
		this.combinedSet.addAll(decisions);
		
		node = new DefaultMutableTreeNode(this);
		
	}
	//methods
	private boolean isFeasible() 
	{
		return true;
	}
	//This is second thing- if it is last day (check)
	//Ways:size of the capacity is the day data()
		//patient with max day of the stay 	
	public boolean canBeGoal() {
		//boolean allPatientsExplored=statePat.isEmpty();
		return currentDay==capacity.size();
	}
	
	public boolean isSearchComplete(SpaceSearcher ss) {
		boolean lastDay=ss.getOpen().isEmpty();
		return canBeGoal()&&lastDay;
	}
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
			ArrayList<Patient> pat=map.get(data.get(i)[0]);
			//if there are no patients on this day in the map
			if (pat==null)
			{	
				//we create a patient with arrival day and los, and put that in the map with the key as arrival day
				ArrayList<Patient> Npat=new ArrayList<Patient> ();
				Npat.add(new Patient(data.get(i)[0],data.get(i)[1]));
				map.put(data.get(i)[0],Npat);
			}
			else
			{	
				//otherwise we create new patient add this into the existing patients who arrive on that day
				pat.add(new Patient(data.get(i)[0],data.get(i)[1]));
			}

		}	

	}
	
	private int nextDayCap()

	{
		if (currentDay>=capacity.size())
				{
			return 0;
				}
		NextDayCap=capacity.get(currentDay);
		//statepat is the arraylist of patient currently staying in the hospital
		for (Patient p:statePat)
		{
			if(p.isStayingDay(currentDay+1))
			{
				NextDayCap-=1;

			}
		}
		return NextDayCap;
		
	}
	//excel read
	
	public static List<Integer> getData ( ) throws IOException
	
	{ 
		File excelFile = ViewObject.chooseFile("XLSX", "Excel", "Choose the file with the example data ");
		FileInputStream fis=new FileInputStream(excelFile);  
		Workbook wb=new XSSFWorkbook(fis); 
		//getting name of the ranges
		Name capacityrangename=wb.getName("Capacity");
		Name patientdatarangename= wb.getName("PatientData");	
		Name resource1Range=wb.getName("Resource1");
		Name resource2Range=wb.getName("Resource2");
		Name capacity2Range=wb.getName("capacity2");
		
		CellRangeAddress capacitycellRange = CellRangeAddress.valueOf(capacityrangename.getRefersToFormula());
		CellRangeAddress patientcellRange2 = CellRangeAddress.valueOf(patientdatarangename.getRefersToFormula());
		CellRangeAddress resource1cellRange2 = CellRangeAddress.valueOf(patientdatarangename.getRefersToFormula());
		CellRangeAddress resource2Range2 = CellRangeAddress.valueOf(patientdatarangename.getRefersToFormula());
		CellRangeAddress capacity2cellRange2 = CellRangeAddress.valueOf(patientdatarangename.getRefersToFormula());
		//9/24/24: iterating over 1 sheet, need to iterate over multiple sheets to add multi region logic
		Sheet sheet= wb.getSheetAt(0);		
		for (int i = capacitycellRange.getFirstRow(); i<=capacitycellRange.getLastRow();i++)
		{
			Row row= sheet.getRow(i);
			 for (int colNum = capacitycellRange.getFirstColumn(); colNum <= capacitycellRange.getLastColumn(); colNum++) 
			 {
	                Cell cell = row.getCell(colNum);
	                Object cellValue = getValues(cell);
	                capacity.add((Integer) cellValue);	   
	          }
			 
		}	
		
		for (int i = patientcellRange2.getFirstRow(); i<=patientcellRange2.getLastRow();i++)
		{
			Row row= sheet.getRow(i);
			int los=0;		
			int FirstDay=0;			
			 for (int colNum = patientcellRange2.getFirstColumn(); colNum <= patientcellRange2.getLastColumn(); colNum++) 
			 {			 	
	                Cell cell = row.getCell(colNum);
	                Object cellValue = getValues(cell);	                
	                if (cellValue != null && ((Integer)cellValue) != 0) 
	                {	                	
	                	if (los-((Integer)cellValue)==-1)
	                	{
	                		FirstDay=colNum;
	                		los++;                		
	                	}
	                	else 
	                	{
	                		los++;
	                	}              
	                }                
	          }
			 data.add(new int [] {FirstDay,los});
		}			
		wb.close();			
		return capacity;
	}
//this method getting values from excel	
	public static Integer getValues (Cell cell)
	
	{
		if (cell==null)
		{
			return 0;
		}
		switch (cell.getCellType())
		{
		case  Cell.CELL_TYPE_NUMERIC:			
			return (int) cell.getNumericCellValue();
	
		case Cell.CELL_TYPE_STRING:
			return Integer.parseInt(cell.getStringCellValue());
		case  Cell.CELL_TYPE_BLANK:		
			return 0;
		default:
			return 0;
	
		}		
	}
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
		String searchString = "BL";
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
			getData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		is.createPatients();
		ss.setApplet();
		ss.display(ss.toString() + " Periodic Problem with the capacity " + (capacity));
//		ss.setSearchOrder(SpaceSearcher.BEST_FIRST);
//		ss.runOptSpaceSearch(3);
//
		
	}


	public String toString () 
	{		
		return super.toString() + "PPD"+ "->" + currentDay + ",pat " + statePat + ","  + evaluate() ;
	}	
	public Set<Searchable> makeNewStates() 
	{			
		//to store new states in states
		Set<Searchable>  states=  new HashSet<Searchable>();	
		System.out.println("The current day is "+ this.toString()+" ..."+ currentDay );		
		if (this.currentDay==capacity.size())
		{			
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
		states.add(new PeriodicProblemDay(this,newStPat,currentDay+1));
		//now comes the new patient
		if (nextDyPat !=null)
		{
			int nextDayCap= nextDayCap();			
			if (nextDayCap>0)				
			{
			List<Patient> nxtDyPatIter = nextDyPat;
			List<List<Patient>> nextDayComb=new ArrayList <>();
			
			try {	
				for (int i=1;i<=nextDayCap;i++)
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
				for (Patient i : ndi )
			{					
				allNewPat.add(i);
			}
				
				states.add( new PeriodicProblemDay(this,allNewPat,currentDay+1));		
				
			}
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
		int [] searchTypes = {BlindSearcher.BREADTH_FIRST,BlindSearcher.DEPTH_FIRST, SpaceSearcher.BEST_FIRST, SpaceSearcher.ASTARALGORITHM};
		return searchTypes;
	}

	@Override
	public double evaluate() 
	{		
		return combinedSet.size();
	}
	
	public void init () 
	{		
			panel = new PeriodicDayPanel ();
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
		
		 public Component getTableCellRendererComponent(
                JTable table, Object color,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
			 
			 Component component = super.getTableCellRendererComponent
			 	(table, color, isSelected, hasFocus, row, column);
			 component.setBackground(Color.cyan);
//			 if (isOnVisitedPath(row+1, column)) {
//				 component.setBackground(Color.yellow);
//			 }
//			 else {
//				 component.setBackground (Color.white);
//			 }
			 return component;
		 }
	}
	
class PeriodicDayPanel extends JPanel 
{
	public PeriodicDayPanel () {
		Object [] allPat=getAllPatient().toArray();
	// sormaz: Mandvi we should create this array from data 	
		//Object [] days= {1,2,3,4,5,6,7,8,9,10};
		ArrayList <Integer>  daysList= new ArrayList();
		for(int i=1;i<=capacity.size();i++) {
			daysList.add(i);
		}
		Object [] days= daysList.toArray();
		Object [] statePat=combinedSet.toArray();
		RectangularTableModel problemTM = new RectangularTableModel (allPat, days, new PDGenerator());
		RectangularTableModel stateTM = new RectangularTableModel (statePat, days, new PDGenerator());
		RectangularTableModel rtm = new RectangularTableModel (allPat, days, new PDGenerator());
		RectangularTableModel rtm2 = new RectangularTableModel (statePat, days, new PDGenerator());
		ModelTable problemTable = new ModelTable(problemTM);
		problemTable.setDefaultRenderer(Boolean.class, new PDCellRenderer());
		ModelTable stateTable = new ModelTable(stateTM);
		boolean  useSplitPane = false;
		if (useSplitPane) {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				new JScrollPane(problemTable), new JScrollPane(stateTable));
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);
		
		this.add(splitPane);
		}
		else {
			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Problem table", null, new JScrollPane(problemTable),
	                  "Display initial problem table, where checkmarks show when patients need resources");
			JScrollPane stateScrollPane = new JScrollPane(stateTable);
			
			tabbedPane.addTab("State table", null, stateScrollPane,
	                  "Display selected state table, where rows show currently selected patients in the state" 
	                		  + "and checkmarks show when selected patients need resources"); 
			this.add(new JScrollPane(tabbedPane));
			tabbedPane.setSelectedComponent(stateScrollPane);
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
		
		return  p1.isStayingDay(i2);
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
		if (pd1.evaluate() <= pd2.evaluate()) {
		        
			return -1; }
		else
		{	return 1; }
	}
	
}

class Patient
{
	int arrivalDay;
	int los;
	String name;
	static int count=0;
	int resource1;
	int resource2;
	public Patient(int a, int b) 
	{
		count ++;
		this.name="P"+ count;
		this.arrivalDay =a;
		this.los=b;

	}
	public Patient(int a, int b,int c ,int d) 
	{
		count ++;
		this.name="P"+ count;
		this.arrivalDay =a;
		this.los=b;
		this.resource1=c;
		this.resource2=d;

	}
	@Override
	public String toString ()
	{
		return (  name +" <arr " + arrivalDay + ",los " + los +">"+"res1"+">"+resource1+"res2"+">"+resource2);
	}
	public boolean isStayingDay(int day)
	{
		return day>=arrivalDay && day<arrivalDay+los;
	}

	public boolean isRequiresResource1(Patient a)
	{
		return a.resource1==1 ;
	}
	public boolean isRequiresResource2(Patient a)
	{
		return a.resource2==1 ;
	}
	
}


