package edu.ohiou.dynamic;

import java.awt.BorderLayout;

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
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.sun.org.apache.xpath.internal.operations.Bool;

import edu.ohiou.dynamic.PeriodicProblemDay.PeriodicDayPanel;
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
// 9/25/2024 modifying this class to include multiple resources
public class SpaceSearchModel2 extends ComparableSpaceState{
	int NextDayCapRes1;
	int NextDayCapRes2;
	int PatientTaken;
	int currentDay;
	static String XLSX_FOLDER;
	static List<int[]> patientdata = new ArrayList<>();	
	static List<int[]> resourcedata = new ArrayList<>();	
    static ArrayList <Integer> capacityRes1 =new ArrayList<>();
    static ArrayList <Integer> capacityRes2 =new ArrayList<>();
    static ArrayList <Integer> resource1 =new ArrayList<>();
    static ArrayList <Integer> resource2 =new ArrayList<>();
	//creating a map of patients objects with key value an integer//this is used in createpatient and getallpatient
    //getallpatient is used in pandel
	static  Map  <Integer,ArrayList<Patient>>  map= new HashMap <Integer,ArrayList<Patient>> ();
	
	static 	{printIndex = true;}
	//statepat store info of all patients based on current day
   ArrayList <Patient> statePat= new ArrayList<Patient>();  
   //most likely storing info of all combined patients taken in that state
   Set<Patient> combinedSet = new HashSet<>();
  static {
	   XLSX_FOLDER = getProperty(PeriodicProblemDay.class, "XLSX_FOLDER");
  }
	public SpaceSearchModel2() {
		node = new DefaultMutableTreeNode(this);
	}
	public SpaceSearchModel2(SpaceSearchModel2 s,ArrayList <Patient> decisions, int cd)
	{
		
		currentDay = cd;	
		parent = s;
		this.combinedSet=new HashSet<Patient>(s.combinedSet) ;
		this.statePat=decisions;	
		this.combinedSet.addAll(decisions);		
		node = new DefaultMutableTreeNode(this);
		
	}  
	private boolean isFeasible() 
	{
		return true;
	}	
	public boolean canBeGoal() {
		//boolean allPatientsExplored=statePat.isEmpty();
		return currentDay==capacityRes1.size();
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
	
	public void  createPatients ()
	{
		for (int i=0;i<patientdata.size();i++) 
		{
			int arrivalDay = patientdata.get(i)[0];
	        int los = patientdata.get(i)[1];
			
				 int resource1 = resourcedata.get(i)[0];
		         int resource2 = resourcedata.get(i)[1];
			//retrieving elements from data the 0 index their arrival day, their lenght of stay by 1
			ArrayList<Patient> pat=map.get(arrivalDay);
			//if there are no patients on this day in the map
			if (pat==null)
			{	
				//we create a patient with arrival day and los, and put that in the map with the key as arrival day
				ArrayList<Patient> newPatientList =new ArrayList<Patient> ();
				newPatientList .add(new Patient(arrivalDay,los,resource1, resource2));
				map.put(arrivalDay, newPatientList);
			}
			else
			{	
				//otherwise we create new patient add this into the existing patients who arrive on that day
				pat.add(new Patient(arrivalDay, los, resource1, resource2));
			}
			}		
	}
	private int nextDayCap(int a)

	{
		if (currentDay>=capacityRes1.size())
				{
			return 0;
				}
		
		NextDayCapRes1=capacityRes1.get(currentDay);
		NextDayCapRes2=capacityRes2.get(currentDay);
		
		//statepat is the arraylist of patient currently staying in the hospital		
		if ( a==1) 
		{
			for (Patient p:statePat)
			{
				if(p.isStayingDay(currentDay+1)&&p.isRequiresResource1(p))
				{
					NextDayCapRes1-=1;

				}
			}
		return NextDayCapRes1;
		}	
		else
		{
			for (Patient p:statePat)
			{
			if(p.isStayingDay(currentDay+1)&&p.isRequiresResource2(p))
				{
				NextDayCapRes2-=1;

				}
			}
			return NextDayCapRes2;
		}
	}
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
		CellRangeAddress resource1cellRange2 = CellRangeAddress.valueOf(resource1Range.getRefersToFormula());
		CellRangeAddress resource2Range2 = CellRangeAddress.valueOf(resource2Range.getRefersToFormula());
		CellRangeAddress capacity2cellRange2 = CellRangeAddress.valueOf(capacity2Range.getRefersToFormula());
		//9/24/24: iterating over 1 sheet, need to iterate over multiple sheets to add multi region logic
		Sheet sheet= wb.getSheetAt(0);		
		
			//filling capcity 1
			 for (int colNum = capacitycellRange.getFirstColumn(); colNum <= capacitycellRange.getLastColumn(); colNum++) 
			 {
				 Row row= sheet.getRow(capacitycellRange.getFirstRow());   
				 Cell cell = row.getCell(colNum);
	             Object cellValue = getValues(cell);
	             capacityRes1.add((Integer) cellValue);	   
	          }
			 //filling capcity2
			 for (int colNum = capacity2cellRange2.getFirstColumn(); colNum <= capacity2cellRange2.getLastColumn(); colNum++) 
			 {
				 Row row= sheet.getRow(capacity2cellRange2.getFirstRow());   
				 Cell cell = row.getCell(colNum);
	             Object cellValue = getValues(cell);
	             capacityRes2.add((Integer) cellValue);	   
	          }
			 //filling resources
			 for (int i = resource1cellRange2.getFirstRow(); i<=resource1cellRange2.getLastRow();i++)
			 {
				 
				 Row row= sheet.getRow(i);
				 int colNum = resource1cellRange2.getFirstColumn();
				 Cell cell = row.getCell(colNum);
	             int cellValue = getValues(cell);
	            // resource1.add((Integer) cellValue);	
	             int colNum2 = resource1cellRange2.getFirstColumn()+1;
	             Cell cell2 = row.getCell(colNum2);
	             int cellValue2 = getValues(cell2);
	           //  resource2.add((Integer) cellValue2);
	           resourcedata.add(new int [] {cellValue,cellValue2});
	           
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
			 patientdata.add(new int [] {FirstDay,los});
		}			
		wb.close();			
		return capacityRes1;
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
	public static void main(String[] args) {
		SpaceSearchModel2 is = new SpaceSearchModel2();
		//represent starting state witht eh current day set to 0
		is.currentDay = 0;
		SpaceSearchModel2 gs = new SpaceSearchModel2();
		SpaceSearcher ss = null;
		String searchString = "BL";
		if (searchString .equalsIgnoreCase("BLIND")) {
			ss = new BlindSearcher (is, gs);
		}
		else {
			ss = new InformedSearcher (is, gs);
		}	
		try {
			getData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		is.createPatients();
		ss.setApplet();
		ss.display(ss.toString() + " Periodic Problem with the capacity 1 and capacity 2 " + (capacityRes1)+(capacityRes2));
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
		if (this.currentDay==capacityRes1.size())
		{			
			return states;
		}
		ArrayList <Patient> newStPat= new ArrayList <Patient> ();
		ArrayList <Patient> nextDyPat= map.get(currentDay+1);//next day patient for all resources 
		ArrayList <Patient> nextDyPatR1=  new ArrayList <Patient> ();//next day patient for resource 2
		ArrayList <Patient> nextDyPatR2=  new ArrayList <Patient> ();//next day patient for resource 2
		ArrayList <Patient> previousDyPat= new ArrayList <Patient> ();	
		//iterating over statepat which stores information of all the patient based on current state 
		for (Patient p2 : statePat)
		{	//patients are staying next day we are adding them to newstpat
			if(p2.isStayingDay(currentDay+1))
			{
				newStPat.add(p2);
			}			
		}		
		states.add(new SpaceSearchModel2(this,newStPat,currentDay+1));
		//now comes the new patient
		if (nextDyPat !=null)
		{
			for (Patient patRes1:nextDyPat)
			{
				if (patRes1.isRequiresResource1(patRes1))
				{
					nextDyPatR1.add(patRes1);
				}
				if (patRes1.isRequiresResource2(patRes1))
				{
					nextDyPatR2.add(patRes1);
				}
			}
			//this needs to be modifies based on two capcity
			int nextDayCap= nextDayCap(1);
			int nextDayCap2=nextDayCap(2);					
			
			List<Patient> nxtDyPatIter = nextDyPatR1;
			List<Patient>nextDayPatIterR2=nextDyPatR2;
			List<List<Patient>> nextDayCombR1=new ArrayList <>();
			List<List<Patient>> nextDayCombR2=new ArrayList <>();
			List<List<Patient>> nextDayComb=new ArrayList <>();				
			try {	
				if (nextDayCap>0)
				{
				for (int i=1;i<=nextDayCap;i++)
					{
				List<List<Patient>> combinations = Comb.createCombinations(nxtDyPatIter, i);
				nextDayCombR1.addAll(combinations);
					}
				}
				if(nextDayCap2>0)
				{
					for (int i=1;i<=nextDayCap2;i++) 
					{
				List<List<Patient>> combinationsR2 = Comb.createCombinations(nextDayPatIterR2, i);
				nextDayCombR2.addAll(combinationsR2);
					}
				}
			//System.out.print(nextDayCombR1);	
			//System.out.print(nextDayCombR2);			
			} catch (Exception e) {
				e.printStackTrace();
			}
			// iam intering over combination of nextday which i require R1		
			for (List<Patient> ndi :nextDayCombR1)
			{	
				//creating an empty list to store element iteratively, this will store [P1]		
				for (Patient i : ndi )
				{	
					for (List<Patient> ndi2 :nextDayCombR2)
				{		
						ArrayList <Patient> innerCombs= new ArrayList <Patient> ();		
						innerCombs.addAll(ndi);
					for (Patient i2: ndi2)
					{
						innerCombs.add(i2);
					}
					nextDayComb.add(innerCombs);
					
				}						
				}				
			}
			List<List<Patient>> nextDayCombCopy=new ArrayList <>(nextDayComb);
			
			for (List<Patient> ndi :nextDayCombCopy)
			{
				for (Patient i :ndi)
				{
					if (i.isRequiresResource1(i)&& i.isRequiresResource2(i))
					{
						if (Collections.frequency(ndi, i) <2 ) 
						{
							nextDayComb.remove(ndi);
					}
				}
			}
			}
			System.out.print(nextDayComb);
			for (List<Patient> ndi :nextDayComb)
			{
				ArrayList <Patient> allNewPat= new ArrayList <Patient> (newStPat);
				for (Patient i : ndi )
			{					
				allNewPat.add(i);
			}
				
				states.add( new SpaceSearchModel2(this,allNewPat,currentDay+1));		
				
			}
		
		}	
		
		return states;
	}
	@Override
	public boolean equals(Searchable s) {
		SpaceSearchModel2 sse2 = (SpaceSearchModel2) s;
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
	class PeriodicDayPanel extends JPanel 
	{
		public PeriodicDayPanel () {
			Object [] allPat=getAllPatient().toArray();
		// sormaz: Mandvi we should create this array from data 	
			//Object [] days= {1,2,3,4,5,6,7,8,9,10};
			ArrayList <Integer>  daysList= new ArrayList();
			for(int i=1;i<=capacityRes1.size();i++) {
				daysList.add(i);
			}
			Object [] days= daysList.toArray();
			Object [] statePat=combinedSet.toArray();
			//Object[] resource1list=resourcedata.toArray();
			RectangularTableModel problemTM = new RectangularTableModel (allPat, days, new PDGenerator());
			RectangularTableModel stateTM = new RectangularTableModel (statePat, days, new PDGenerator());
			//RectangularTableModel resourceTable = new RectangularTableModel (resource1list,days, new PDGenerator());
			
			ModelTable problemTable = new ModelTable(problemTM);
			ModelTable stateTable = new ModelTable(stateTM);
			//ModelTable resource2Table = new ModelTable(resourceTable);
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
				//JScrollPane resourceScrollPane = new JScrollPane(resource2Table);
				tabbedPane.addTab("State table", null, stateScrollPane,
		                  "Display selected state table, where rows show currently selected patients in the state" 
		                		  + "and checkmarks show when selected patients need resources");
			//	//tabbedPane.addTab("Resource table", null, resourceScrollPane,
		                //  "Display patient resources");
				this.add(new JScrollPane(tabbedPane));
				tabbedPane.setSelectedComponent(stateScrollPane);
			}
		}
		
		
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
			SpaceSearchModel2 pd1 = (SpaceSearchModel2) o1;
			SpaceSearchModel2 pd2 = (SpaceSearchModel2) o2;
			if (pd1.evaluate() <= pd2.evaluate()) {
			        
				return -1; }
			else
			{	return 1; }
		}
		
	}

}
