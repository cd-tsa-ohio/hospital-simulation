package edu.ohiou.dynamic;

import java.io.IOException;
import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Comb 
{

	public Comb() {
		// TODO Auto-generated constructor stub
	}

	
public static 	List<List<Integer>> createCombinations(List<Integer> iterable, int r) throws Exception 
{
	List<List<Integer>>resList = new ArrayList<>();
//	Console c = System.console();
	int count =0;
	
	List<Integer> runList =new ArrayList<>();
	int n=iterable.size();
	if (r>n)
	{
		return resList;
		
	}
	
	int [] indices=new int[r]; // line 40 in python
	for (int jj= 0; jj<r;jj++)
	{
		indices[jj]=jj;
	}
	
	 for (int i = 0; i < r; i++)
	 {
		runList.add(iterable.get(indices[i])) ;
     }
	 resList.add(runList);// line 41
	 
    //System.out.print(resList);
	 
	boolean a = true;
	
	while (a)
	{
		runList =new ArrayList<>();
		int i =0;
		for ( i = r - 1; i >= 0; i--)
		{
			if ( indices[i] != i + n - r)
			{
				break;
			}
		
			
		return resList;
		
		}		
		indices[i] += 1;	
		
		for (int j = i+1 ; j < r; j++) {
			indices[j] = indices[j - 1] + 1;
		}
		System.out.println("indices: " + Arrays.toString(indices));
//		System.out.print("enter something>");
//		
//
//		String  ab = c.readLine();

		for (int ii = 0; ii < r; ii++)
		 {
			runList.add(iterable.get(indices[ii])) ;
	     }
		resList.add(runList);
		System.out.println("run list " + runList);
		count++;
		System.out.println("count" + count);
		if (count > 15) {
			throw new Exception("aborting infintie loop");
		}
		}
		
	

	return resList;

}

public static void main(String[] args)
{
//	Console c = System.console();
//	String  ab = c.readLine();
	
	List<Integer>a = new ArrayList<>();
  a.add(1);
  a.add(2);
  a.add(3);
  a.add(4);
  a.add(5);
  Comb b= new Comb();
 try {
	System.out.print( b.createCombinations(a, 3));
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

}
}






