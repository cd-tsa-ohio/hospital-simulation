/*********************************************
 * OPL 12.8.0.0 Model
 * Author: HP
 * Creation Date: Nov 29, 2022 at 1:27:28 AM
 *********************************************/
int NPatients=...;
range patients=1..NPatients;

int Ndays=...;
range days=1..Ndays;
int capacity[days]=...;
int x[patients][days]=...;
int T=...;
range t=1..T;
//int type[t][patients]=...;

dvar boolean y[patients];
dvar int+ a[patients][days];
dvar int+ at [t][patients];
//patient taken each day
//dexpr int Pd=sum(i in days)sum(m in patients)x[i][m]*y[m];
//objective function maximise total patients
//previous prob
//dexpr float totalpatients = sum (i in patients)y[i];
dexpr float totalpatients = sum (i in days)sum (m in patients)a[m][i];
//dexpr int patientsperday=sum (i in days)
maximize totalpatients;
subject to{
//total capacity
forall (i in days){
sum( m in patients)x[m][i]*y[m]<=capacity[i];

}

forall (i in days){
forall ( m in patients){

a[m][i]==x[m][i]*y[m];

}
}

 

}
 