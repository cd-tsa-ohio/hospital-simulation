/*********************************************
 * OPL 12.8.0.0 Model
 * Author: HP
 * Creation Date: Mar 30, 2022 at 12:04:38 AM
 *********************************************/
//model sets
//number of days

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
//int E [T][R]=...;


//DECISON VARIABLES
//number of added heatthcare resources
//dvar int+ n[I][R][T];
//amount of new capacity added
//dvar int+ c [I][R][T];

//number of healthcare resources that are transhipped
//dvar int+ q[I][I2][R][T];
//dvar int+ v [I][R][T];// capacity of healthcare resource at region i in period t
//patient taken each day
 //dvar int+ x[I][K][T]; //patient that are accepted
 dvar int+ x[T][I];
 dvar int+ w [T][I];//patient that are transferred 
 dvar int+ NW [T][I]; //patient that canot even be transferred
//dvar int+ kb[I][R][T];//number of available resources at the begining of horizon
//dvar int+ ke [I][R][T];// resouces that are remaing at the end of a period
//dvar int+ p[I][I2][T]; // patien that are sent from one region to another
dvar int+ p[T][I][I2];
 // objective function
// dexpr float total = sum (i in I)sum (t in T)NW[i][t]+ sum (i in I)sum (r in R)sum (t in T)n[i][r][t]  +sum (i in I)sum (r in R)sum (t in T)c[i][r][t];
dexpr float total = sum (t in T)sum (i in I)NW[t][i];
 minimize total;
 subject to{
// forall ( i in I){
// forall (t in T) {
// forall (r in R){
// kb[i][r][t]==ke [i][r][t-1]; 
// }
// 
//
// }
// }
//  forall ( i in I){
// forall (t in T) {
// forall (r in R){
// ke[i][r][t]==kb [i][r][t-1] + n[i][r][t]-x[i][k][t]; 
// }
// 
//
// }
// }
// forall ( i in I){
// forall (r in R) {
// forall (t in T){
//n[i][r][t]==0; 
// }
// 
//
// }
// }
//  forall ( i in I){
// forall (r in R) {
// forall (t in T){
//v[i][r][t]==c[i][r][t]; 
// }
// 
//
// }
// }
  forall ( i in I){
 //forall (r in R) {
 forall (t in T2){
 
 
//x[i][k][t]+w[i][t]-sum (i in I)p [i][i2][k][t]==D[i][k][t]+sum (i in I)p [i2][i][k][t]+NW[i][t]; 
x[t][i]+w[t][i]-sum(j in I: j!=i) p [t][i][j]==D[t][i]+sum(j in I: j!=i)p [t][j][i]+NW[t-1][i]; 

 //}
 

 }
 x[1][i]+w[1][i]-sum(j in I: j!=i) p [1][i][j]==D[1][i]+sum(j in I: j!=i)p [1][j][i];
 }
 
  forall ( t in T){
 //forall (r in R) {
 forall (i in I){
//NW[i][t]>=w[i][t]-sum (i in I)p [i][i2][k][t]; 
NW[t][i]>=w[t][i]-sum(j in I: j!=i)p [t][i][j]; 
 //}
 

 }
 }
 forall ( t in T){
 //forall (r in R) {
 forall (i in I){
//w[i][t]<=max{D[i][k][t]}*0.5;
w[t][i]<=D[t][i]*1;
 //}
 

 }
 }
 
  forall ( t in  T){

 forall (i in I ){
//sum (i in I)p [i2][i][k][t]<=max{D[i][k][t]}*0.5;
//sum (i in I)p [t][i]<=D[t][i]*0.25;
{

sum (j in I: j!=i)p [t][j][i]<=D[t][i]*1;

}

 }
 }
// 
 //
// forall (i in T){
//forall (j in R){
//if (D[i][j]- C[i][j]<0) {
//w [i][j]==0;
//}
//else{
//w [i][j] == D[i][j]- C[i][j];
//
//}
//if (C[i][j]- D[i][j]>0){
//ReC[i][j]==C[i][j]- D[i][j];
//
// }
// else{
// ReC[i][j]==0;
// }
//}
//}
//forall ( i in T ) {
//  forall ( j in R ) {
//    ReC[i][j] == C[i][j] - D[i][j];
//    //w [i][j] == D[i][j]- C[i][j];
//    w[i][j] >= 0;
//  }
//} 

}  