package edu.ohiou.dynamic;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.ohiou.mfgresearch.labimp.spacesearch.BlindSearcher;
import edu.ohiou.mfgresearch.labimp.spacesearch.DefaultSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.Searchable;
import edu.ohiou.mfgresearch.labimp.spacesearch.SpaceSearcher;

public class SpaceSearcherEx2 extends DefaultSpaceState {
	
	int counter = 0;
	int value;
	static int total = 0;
	static int MAX = 10;
	static int MIN = 5;

	public SpaceSearcherEx2() {
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
		states.add(new SpaceSearcherEx2());
		states.add(new SpaceSearcherEx2());
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
		SpaceSearcherEx2 sse2 = (SpaceSearcherEx2) s;
		// TODO Auto-generated method stub
		return  value == sse2.value;
	}
	
	public int hashCode() {
		return new Integer(value).hashCode();	
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpaceSearcherEx2 ss = new SpaceSearcherEx2();
		SpaceSearcherEx2 gs = new SpaceSearcherEx2();
		gs.value += 3;
		BlindSearcher bs = new BlindSearcher (ss, gs);
		bs.setApplet();
		bs.display("Showing Mandvi space search");

	}
	
	class Example2Panel extends JPanel {
		
	}

}
