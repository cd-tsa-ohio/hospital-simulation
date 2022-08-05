/**
 * 
 */
package edu.ohiou.dynamic;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.ohiou.dynamic.SpaceSearcherEx2.Example2Panel;
import edu.ohiou.mfgresearch.labimp.spacesearch.BlindSearcher;
import edu.ohiou.mfgresearch.labimp.spacesearch.ComparableSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.DefaultSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.Searchable;
import edu.ohiou.mfgresearch.labimp.spacesearch.SpaceSearcher;

/**
 * @author HP
 *
 */
public class DPExample1 extends ComparableSpaceState {
	static LinkedList regions = new LinkedList();
	static LinkedList resources = new LinkedList();
	//LinkedList visitedStage = new LinkedList();
	//LinkedList unvisitedStage = new LinkedList();
	double totaleffectivness;
    double maxEffGoal;
   ArrayList <Integer> decisions= new ArrayList<Integer>();
	
	static String currentHeuristic="";
	static double values[][] = { { 0, 45, 70, 90, 105,120 }, { 0, 20, 45, 75,110,150 },
			{ 0, 50, 70, 80, 100,130 } };
	//Dictionary values= new Hashtable();
	
	static {
		regions.add(new String("1"));
		regions.add(new String("2"));
		regions.add(new String("3"));
			
		}
	  
	public DPExample1(int decision) {
		
		decisions.add(decision);
		node = new DefaultMutableTreeNode(this);
		
	}
	
 	public DPExample1(DPExample1 s,int decision) {
		decisions=new ArrayList<Integer>(s.decisions);
		
		decisions.add(decision);
		node = new DefaultMutableTreeNode(this);
		
	}
 	public void computeValues() {
 		if (decisions.size()==3) {
 			for (int j=0;j<3;j++) {
 			for (int i=0;i<decisions.size();i++) {
 			totaleffectivness= values[j][i];
 			System.out.print("Total effectivness is" + totaleffectivness);
 			break;
 			}
 		
 			}
 		}
 		
 	}
 	public boolean CanBeadded(int cs) {
		if(cs<6) {
			return true;
			
		}
		return false;
	}

	public DPExample1() {
		node = new DefaultMutableTreeNode(this);
		// TODO Auto-generated constructor stub
	}
	public String toString() {
		 
				return super.toString() + "DP"+ decisions;
				
			}
	@Override
	public boolean canBeGoal() {
		return decisions.size() == 3;
	}
	public boolean isBetterThan(Searchable inState) {
		return false;
	}
	@Override
	public boolean equals(Searchable s) {
		DPExample1 sse2 = (DPExample1) s;
		// TODO Auto-generated method stub
		return  decisions == sse2.decisions;
	}
	
	public int hashCode() {
		return  decisions.hashCode();	
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DPExample1 ss = new DPExample1();
		DPExample1 gs = new DPExample1();
		
		BlindSearcher bs = new BlindSearcher (ss, gs);
		bs.setApplet();
		bs.display("Showing Mandvi space search");
		

	}
	
	@Override
	public Set<Searchable> makeNewStates() {
		Set<Searchable>  states=  new HashSet<Searchable>();
		
		DPExample1 s1= new DPExample1(this,0);
		DPExample1 s2= new DPExample1(this,1);	
		DPExample1 s3= new DPExample1(this,2);	
		DPExample1 s4= new DPExample1(this,3);	
		DPExample1 s5= new DPExample1(this,4);	
		DPExample1 s6= new DPExample1(this,5);	
		
		if (s1.isFeasible()) states.add(s1);
		
		//s1.computeValues();
		if (s2.isFeasible()) states.add(s2);
		if (s3.isFeasible()) states.add(s3);
		if (s4.isFeasible()) states.add(s4);
		if (s5.isFeasible()) states.add(s5);
		if (s6.isFeasible()) states.add(s6);
		
		return states;
	}

	private boolean isFeasible() {
		int sum=0;
		for (int i: decisions) {
			sum+=i;
			
		}
	
		return sum<=5 && decisions.size()<=3;
	}

	@Override
	public int[] setSearchTypes() {
		int [] searchTypes = {SpaceSearcher.BREADTH_FIRST,SpaceSearcher.DEPTH_FIRST};
		// TODO Auto-generated method stub
		return searchTypes;
	}
class Example2Panel extends JPanel {
		
	}
public void init () {
	panel = new Example2Panel ();
}


}
