/*********************************************
 * OPL 12.8.0.0 Model
 * Author: HP
 * Creation Date: Oct 28, 2022 at 11:50:42 PM
 *********************************************/

int NPatients=...;
range patients=1..NPatients;

int Ndays=...;
range days=1..Ndays;
int capacity[days]=...;
int x[days][patients]=...;
int T=...;
range t=1..T;
int type[t][patients]=...;

dvar boolean y[patients];
dvar int+ a[days][patients];
dvar int+ at [t][patients];
//patient taken each day
//dexpr int Pd=sum(i in days)sum(m in patients)x[i][m]*y[m];
//objective function maximise total patients
//previous prob
//dexpr float totalpatients = sum (i in patients)y[i];
dexpr float totalpatients = sum (i in patients)y[i];
//dexpr int patientsperday=sum (i in days)
maximize totalpatients;
subject to{
//total capacity
forall (i in days){
sum( m in patients)x[i][m]*y[m]<=capacity[i];

}

forall (i in days){
forall ( m in patients){

a[i][m]==x[i][m]*y[m];

}
}



}
 