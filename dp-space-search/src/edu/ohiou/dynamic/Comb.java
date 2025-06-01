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
	//	int count =0;
		List<Patient> runList =new ArrayList<>();
		int n=iterable.size();
		int maxComb=calPossComb(n,r);
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
			for (int ii = 0; ii < r; ii++)
			{
				runList.add(iterable.get(indices[ii])) ;
			}
			resList.add(runList);
			
			//count++;
			//System.out.println("count " + count);

//			if (count > maxComb) {
//
//				throw new Exception("aborting infintie loop");
//			}
		}
		return resList;

	}
	public static  int calPossComb(int n, int r)
	{    if (r>n)
		return 0;
	if  (r==0 || r ==n)
	{
		return 1;
	}
	int value=1;
	for (int i=1; i<=r;i++)
	{
		value= value*(n-i+1)/i;
	}
		return value;
	}

	public static void main(String[] args)
	{

		List<Patient>a = new ArrayList<>();
		ArrayList<Integer> res = new ArrayList<Integer>();
		res.add(1);
		int size = 10;
		for (int i = 0; i<size; i++) {
		a.add(new Patient(1,3,res));
		}

		Comb b= new Comb();
		try {
			
			
			List<List<Patient>> test=b.createCombinations(a, size-2);
			//System.out.println( b.createCombinations(a, 3));
			//System.out.println(test);
			System.out.println("combination: " +test.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}






