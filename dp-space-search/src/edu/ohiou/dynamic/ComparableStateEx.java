package edu.ohiou.dynamic;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.ohiou.dynamic.SpaceSearcherEx2.Example2Panel;
import edu.ohiou.mfgresearch.labimp.spacesearch.BlindSearcher;
import edu.ohiou.mfgresearch.labimp.spacesearch.ComparableSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.Searchable;
import edu.ohiou.mfgresearch.labimp.spacesearch.SpaceSearcher;

public class ComparableStateEx extends ComparableSpaceState {

	int counter = 0;
	int value;
	static int total = 0;
	static int MAX = 10;
	static int MIN = 5;

	public ComparableStateEx() {
		// TODO Auto-generated constructor stub
		node = new DefaultMutableTreeNode(this);		
		total++;
		counter = total;
		init();
		value = setValue();
	}

	@Override
	public Set<Searchable> makeNewStates() {
		// TODO Auto-generated method stub
		Set<Searchable> states = new HashSet<Searchable>();
		states.add(new ComparableStateEx());
		states.add(new ComparableStateEx());
		return states;
	}
	
	private int setValue () {
	
		Random random = new Random();
//		return random.nextInt(MAX - MIN) + MIN;
		return counter + 5;
}


	@Override
	public int[] setSearchTypes() {
		int [] searchTypes = {SpaceSearcher.BREADTH_FIRST};
		// TODO Auto-generated method stub
		return searchTypes;
	}
	
	public boolean canBeGoal() {
		return value == 10;
	}
	public boolean isBetterThan(Searchable inState) {
		return false;
	}
	
	public void init () {
		panel = new Example2Panel ();
	}
	
	public String toString() {
		return "Space Searcher Example-" + counter + "-" + value;
	}
	
	@Override
	public boolean equals(Searchable s) {
		ComparableStateEx sse2 = (ComparableStateEx) s;
		// TODO Auto-generated method stub
		return  value == sse2.value;
	}
	
	public int hashCode() {
		return new Integer(value).hashCode();	
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ComparableStateEx ss = new ComparableStateEx();
		ComparableStateEx gs = new ComparableStateEx();
		gs.value += 3;
		BlindSearcher bs = new BlindSearcher (ss, gs);
		bs.setApplet();
		bs.display("Showing Mandvi space search");

	}
	
	class Example2Panel extends JPanel {
		
	}


}
