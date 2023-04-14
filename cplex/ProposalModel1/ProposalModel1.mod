/*********************************************
 * OPL 12.8.0.0 Model
 * Author: HP
 * Creation Date: Mar 28, 2023 at 10:27:27 PM
 *********************************************/
int NPatients=...;
range patients=1..NPatients;

int Ndays=...;
range days=1..Ndays;
int capacity1[days]=...;// make one capacity
int capacity2[days]=...;
int x[patients][days]=...;
int T=...;
range t=1..T;
int AL[patients]=...;
int ResReq1[patients]=...; // resource requirement for resource 1// make one with patient and resources with n days 
int ResReq2[patients]=...; // resource requirement for resource 2

//int type[t][patients]=...;

dvar boolean y[patients];
dvar int+ a[patients][days];
dvar int+ at [t][patients];
//patient taken each day
//dexpr int Pd=sum(i in days)sum(m in patients)x[i][m]*y[m];
//objective function maximise total patients
//previous prob
//dexpr float totalpatients = sum (i in patients)y[i];
//dexpr float totalpatients = sum (i in patients)y[i]*AL[i];
dexpr float totalpatients = sum (i in patients)y[i];
//dexpr int patientsperday=sum (i in days)
maximize totalpatients;
subject to{
//total capacity
forall (i in days){
sum( m in patients)x[m][i]*ResReq1[m]*y[m]<=capacity1[i];

}
forall (i in days){
sum( m in patients)x[m][i]*ResReq2[m]*y[m]<=capacity2[i];

}

forall (i in days){
forall ( m in patients){

a[m][i]==x[m][i]*y[m];

}
}

 

}
 