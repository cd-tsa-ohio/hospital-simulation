/*********************************************
 * OPL 12.8.0.0 Model
 * Author: HP
 * Creation Date: Jul 23, 2022 at 3:35:17 PM
 *********************************************/
int t =...;
range T= 1..t;
range T2= 2..t;
//number of resources
int r =...;
range R=1..r;
// region
int i = ...;
range I = 1..i;
range I2=1..i;
//patient types
//int k= ...;
//range K= 1..k;


// Model Parameters
//patient coming each day each region
int D [T][I]=...;
//discharge time for each patient
//int del[K]=...;
// number of available resorces at wach region at begining of horizon
int E [T][I][R]=...;
// patient that are discharged
int X[T][I]=...;



//DECISON VARIABLES
//number of added heatthcare resources
dvar int+ n[T][I][R];
//amount of new capacity added
//dvar int+ c [I][R][T];
dvar int+ alpha;
dvar int+ rc [T][I];


//number of healthcare resources that are transhipped
dvar int+ q[T][I][I2];
//dvar int+ v [I][R][T];// capacity of healthcare resource at region i in period t
//patient taken each day
 //dvar int+ x[I][K][T]; //patient that are accepted
 dvar int+ x[T][I];
 dvar int+ w [T][I];//patient that are transferred 
 dvar int+ NW [T][I]; //patient that canot even be transferred
dvar int+ kb[T][I][R];//number of available resources at the begining of horizon
//dvar int+ ke [T][I][R];// resouces that are remaing at the end of a period
//dvar int+ p[I][I2][T]; // patien that are sent from one region to another
dvar int+ p[T][I][I2];
dvar int+ P[T][I];
dvar int+ UX[T][I];
dvar boolean y[T][I];
 // objective function
// dexpr float total = sum (i in I)sum (t in T)NW[i][t]+ sum (i in I)sum (r in R)sum (t in T)n[i][r][t]  +sum (i in I)sum (r in R)sum (t in T)c[i][r][t];
dexpr float total = sum (t in T)sum (i in I)(x[t][i]+UX[t][i]);
 maximize total;
 subject to{
 alpha<=1; 
 // availble capacity
 forall (t in T){ 
  forall ( i in I){
 forall (r in R){
 kb[t][i][r]==E [t][i][r]; 
 
 }
 } 
}
//remaning capaicty is daily capcity  minus patient taken x
 forall (t in T){ 
 
  forall ( i in I){
 forall (r in R){
 rc[t][i]==kb [t][i][r]-x[t][i]; 
 
 }
 } 
}
// patient taken is less than demand and capacity

 forall ( t in T){
 forall (i in I) {
 forall (r in R){
 x[t][i]<=kb [t][i][r] && x[t][i]<= D[t][i ];


  // xi is less than capacity
 }
 

 }
 }
// total number of patients taken is transfered suceesul and daily patient taken
  forall ( t in T){
 forall (i in I) {

 UX[t][i]==x [t][i]+ sum ( j in I : j!=i)p[t][j][i]; 
 
 // xi is less than capacity
 
 }
 }

//patient that are not admitted after transfer is nw , w is transfeered, p is transferred succeful

  forall ( t in T){
 //forall (r in R) {
 forall (i in I){

 
//NW[i][t]>=w[i][t]-sum (i in I)p [i][i2][k][t]; 
NW[t][i]==w[t][i]-sum ( j in I : j!=i)p [t][i][j]; 
  
 //}
 

 }
 }
 //patient that are transfferred
 forall ( t in T){
 //forall (r in R) {
 forall (i in I){
//w[t][i]<=D[t][i] * alpha
w[t][i]==D[t][i]-x [t][i];
 //}
  
 }
 }
 //patient transferred from  a region to another should be less than the transferred patient of that region
  forall ( t in  T){

 forall (i in I ){
sum (j in I: j!=i)p[t][i][j]<= w[t][i];
 }
 }
 

//pateint accepted to other region should be less than the capacity of that region
 forall ( t in  T){

 forall (i in I ){
sum (j in I: j!=i)p[t][j][i]<=rc[t][i];
 }
 }
 
}
 