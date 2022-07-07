/*********************************************
 * OPL 12.8.0.0 Model
 * Author: mandvi
 * Creation Date: Mar 29, 2022 at 12:09:01 AM
 *********************************************/

// model sets
 int t=...;
 range T=1..t;
 int u=...; //not accepted patients
 range U=0..u;
 int k= ...;// umber of pateients that came in
 range K= 1..k;
 
 int i=...; //set of regions
 range I=1..i;
 int tr=...;
range TR  = 1..tr; //set of patients that are transferred to another region
//paramters

int D[I][k][T]=...;
int C[I][T]=...;

 //decision variables
 dvar boolean NA [K][I];
 dexpr int p [I][TR][T]= sum (tr in TR)sum (i in I)sum(t in T);
 
 