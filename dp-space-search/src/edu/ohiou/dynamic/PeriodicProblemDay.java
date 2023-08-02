package edu.ohiou.dynamic;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
	
	

	
	static int data[][]= {{1,3},{1,2},{2,1},{3,1}};
	
    static ArrayList <Integer> capacity =new ArrayList<>();
	//static int capacity []= {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2};
	
	//creating a map of patients objects with key value an integer
	static  Map  <Integer,ArrayList<Patients>>  map= new HashMap <Integer,ArrayList<Patients>> ();
	static 	{printIndex = true;}
   static  ArrayList <Patients> nd= new ArrayList <Patients> ();
   static ArrayList <Patients> allAceepted= new ArrayList <Patients> ();
 //this holds informaiton about patients
   ArrayList <Patients> statePat= new ArrayList<Patients>();
	//constructors 
	public PeriodicProblemDay()
	{
		node = new DefaultMutableTreeNode(this);
		//statePat=new ArrayList<Patients>();
	}
	public PeriodicProblemDay(PeriodicProblemDay s,ArrayList <Patients> decisions, int cd)
	{
		currentDay = cd;
		NextDayCap=capacity.get(cd); 
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
			//retrieving elements from data the 0 index their arrival day, their lenght of stay by 1
			ArrayList<Patients> pat=map.get(data[i][0]);
			//if there are no patients on this day in the map
			if (pat==null)
			{	
				//we create a patient with arrival day and los, and put that in the map with the key as arrival day
				ArrayList<Patients> Npat=new ArrayList<Patients> ();
				Npat.add(new Patients(data[i][0],data[i][1]));
				map.put(data[i][0],Npat);
			}
			else
			{	
				//otherwise we create new patient add this into the existing patients who arrive on that day
				pat.add(new Patients(data[i][0],data[i][1]));
			}

		}	

	}
	
	private int nextDayCap()

	{
		NextDayCap=capacity.get(currentDay+1);
		//statepat is the arraylist of patient currently staying in the hospital
		for (Patients p:statePat)
		{
			if(p.isStayingDay(currentDay+1))
			{
				NextDayCap-=1;

			}
		}
		return NextDayCap;
		
	}
	//excel read
	public static List<Integer> getCapData ( ) throws IOException
	{ 
		FileInputStream fis=new FileInputStream(new File("C:\\Users\\HP\\Documents\\DataFile2.xlsx"));  
		//creating workbook instance that refers to .xls file  
		Workbook wb=new XSSFWorkbook(fis);   
	
		//List <Object> capacity= new ArrayList <Object>();
		CellRangeAddress cellRange = CellRangeAddress.valueOf("B27:K27");
		Sheet sheet= wb.getSheetAt(0);
		//CellRangeAddress range2= CellRangeAddress.valueOf(range);
	//	CellWalk cellwalk= new CellWalk (sheet, range2);
		for (int i = cellRange.getFirstRow(); i<=cellRange.getLastRow();i++)
		{
		
			Row row= sheet.getRow(i);
			 for (int colNum = cellRange.getFirstColumn(); colNum <= cellRange.getLastColumn(); colNum++) 
			 {
	                Cell cell = row.getCell(colNum);
	                Object cellValue = getValues(cell);
	                capacity.add((Integer) cellValue);
	            }
		}
		
			
	
		for (Integer i: capacity )
		{
			System.out.print(i);
			System.out.println("\n");
		}
		return capacity;
	}
	
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

		PeriodicProblemDay ss = new PeriodicProblemDay();
		//represent starting state witht eh current day set to 0
		ss.currentDay = 0;
		PeriodicProblemDay gs = new PeriodicProblemDay();
		//represent goal state of the problem with currrentday set to 10
		gs.currentDay = 10;
		
		BlindSearcher bs = new BlindSearcher (ss, gs);
		//calling createpatient on ss instance this will populat the map
		ss.createPatients();
		System.out.print(map);
		//System.out.print(getALlPatient());
		try {
			getCapData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bs.setApplet();
		bs.display("Periodic Problem with the capacity " + (capacity));
		
	}


	public String toString () 
	{

		
		return super.toString() + "PPD"+ "->" + currentDay + ",pat " + statePat + ","  + evaluate() ;




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
	

	//this method create new states or instances of ppd based on current state 
	public Set<Searchable> makeNewStates() 
	{
		//to store new states in states
		Set<Searchable>  states=  new HashSet<Searchable>();		
		//arraylist of patients who are my new state patients
		ArrayList <Patients> newStPat= new ArrayList <Patients> ();
		//list of patients staying nex day
		ArrayList <Patients> nextDyPat= map.get(currentDay+1);	
		//iterating over statepat which stores information of all the patient based on current state 
		for (Patients p2 : statePat)
		{	//patients are staying next day we are adding them to newstpat
			if(p2.isStayingDay(currentDay+1))
			{
				newStPat.add(p2);
			}
		}	
		//cpmment out 192, start with i=0 remove if of capacity check
	//untill here i am adding the existing patietns already into the list of decisons for resulting state 
		states.add(new PeriodicProblemDay(this,newStPat,currentDay+1));
		//now comes the new patient
		if (nextDyPat !=null)
		{
			int nextDayCap= nextDayCap();			
			if (nextDayCap>0)				
			{
//			List<Integer> iterable=new ArrayList<>();
//			iterable.add(0);
//			iterable.add(1);
//			iterable.add(2);
//			iterable.add(3);	
			List<Patients> nxtDyPatIter = nextDyPat;
			List<List<Patients>> nextDayComb=new ArrayList <>();
			//so if we have capcity and there a next day patients we create combinations
			try {	
				for (int i=1;i<=nextDayCap;i++)
				{
				List<List<Patients>> combinations = Comb.createCombinations(nxtDyPatIter, i);
				nextDayComb.addAll(combinations);
	                
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (List<Patients> ndi :nextDayComb)
			{
				ArrayList <Patients> allNewPat= new ArrayList <Patients> (newStPat);
				for (Patients i : ndi )
			{					
				allNewPat.add(i);						
			}
				states.add( new PeriodicProblemDay(this,allNewPat,currentDay+1));				
				//allAceepted.addAll(allNewPat);
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

