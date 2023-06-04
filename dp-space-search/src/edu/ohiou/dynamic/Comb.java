package edu.ohiou.dynamic;

import java.util.ArrayList;
import java.util.List;

public class Comb 
{

	public Comb() {
		// TODO Auto-generated constructor stub
	}

	
public static 	List<List<Integer>> createCombinations(List<Integer> iterable, int r) 
{
	List<List<Integer>>resList = new ArrayList<>();
	
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
		
		for (int j = i ; j < r; j++) {
			indices[j] = indices[j - 1] + 1;
		}
		for (int ii = 0; ii < r; ii++)
		 {
			runList.add(iterable.get(indices[ii])) ;
	     }

		System.out.print(resList);
		}
		
	

	return resList;

}

public static void main(String[] args)
{
	List<Integer>a = new ArrayList<>();
  a.add(1);
  a.add(2);
  a.add(3);
  a.add(4);
 // a.add(5);
  Comb b= new Comb();
 System.out.print( b.createCombinations(a, 2));

}
}






