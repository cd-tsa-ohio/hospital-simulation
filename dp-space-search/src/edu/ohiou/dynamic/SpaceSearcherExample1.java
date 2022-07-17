package edu.ohiou.dynamic;

import java.util.Set;

import javax.media.j3d.BranchGroup;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.ohiou.mfgresearch.labimp.spacesearch.BlindSearcher;
import edu.ohiou.mfgresearch.labimp.spacesearch.DefaultSpaceState;
import edu.ohiou.mfgresearch.labimp.spacesearch.Searchable;
import edu.ohiou.mfgresearch.labimp.spacesearch.SpaceSearcher;

public class SpaceSearcherExample1 extends DefaultSpaceState {

	public SpaceSearcherExample1() {
		// TODO Auto-generated constructor stub
		node = new DefaultMutableTreeNode();
		init();
	}
	
	public void init() {
		panel = new ExamplePanel();
	}

	@Override
	public Set<Searchable> makeNewStates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Searchable s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] setSearchTypes() {
		// TODO Auto-generated method stub
		int [] types = {SpaceSearcher.DEPTH_FIRST,
					SpaceSearcher.DEPTH_FIRST
							};
		return types;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpaceSearcherExample1 f = new SpaceSearcherExample1();
		SpaceSearcherExample1 nf = new SpaceSearcherExample1();
		BlindSearcher searcher = new BlindSearcher(f, nf);
//	     Searchable g = f.runSpaceSearch(nf);
//	    System.out.println(g.printPath());
			searcher.setApplet();
			searcher.display("Space Searcher example");
	}

	@Override
	public BranchGroup createSceneGraph() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BranchGroup createAnimationGraph() {
		// TODO Auto-generated method stub
		return null;
	}
	
	class ExamplePanel extends JPanel {
		
	}

}
