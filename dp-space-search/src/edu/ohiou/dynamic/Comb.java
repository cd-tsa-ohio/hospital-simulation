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


	public static 	List<List<Patient>> createCombinations(List<Patient> iterable, int r) throws Exception 
	{
		List<List<Patient>>resList = new ArrayList<>();
		int count =0;

		List<Patient> runList =new ArrayList<>();
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
		boolean broken = false;
		while (a)
		{
			runList =new ArrayList<>();
			int i =0;
			for ( i = r - 1; i >= 0; i--)
			{
				broken = false;
				if ( indices[i] != i + n - r)
				{
					broken = true;
					break;
				}
			}	
			if (!broken) {
				return resList;
			}
			indices[i] += 1;	

			for (int j = i+1 ; j < r; j++) {
				indices[j] = indices[j - 1] + 1;
			}
			System.out.println("indices: " + Arrays.toString(indices));

			for (int ii = 0; ii < r; ii++)
			{
				runList.add(iterable.get(indices[ii])) ;
			}
			resList.add(runList);
			System.out.println("run list " + runList);
			count++;
			System.out.println("count " + count);
			if (count > 15) {
				throw new Exception("aborting infintie loop");
			}
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
		a.add(5);
		Comb b= new Comb();
		try {
			//System.out.print( b.createCombinations(a, 3));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}






