/*********************************************
 * OPL 12.8.0.0 Model
 * Author: mm286614
 * Creation Date: Jan 21, 2022 at 2:25:04 AM
 *********************************************/
// model parameters

int NPatients=...;
range patients=1..NPatients;

int Ndays=...;
range days=1..Ndays;
int capacity=...;
int x[days][patients]=...;


dvar boolean y[patients];
//patient taken each day
dexpr int Pd=sum(i in days)sum(m in patients)x[i][m]*y[m];
//objective function maximise total patients
//previous prob
//dexpr float totalpatients = sum (i in patients)y[i];
dexpr float totalpatients = sum (i in patients)y[i]+Pd;
//dexpr int patientsperday=sum (i in days)
maximize totalpatients;
subject to{

 
 
//total capacity
forall (i in days){
sum( m in patients)x[i][m]*y[m]<=capacity;
}

}


