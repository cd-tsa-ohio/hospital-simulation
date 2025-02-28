import numpy as np
from scipy.optimize import fsolve
import pandas as pd
import openpyxl
 #got the equation of mean using mean=lam*gamaa(1+1/k) source https://en.wikipedia.org/wiki/Weibull_distribution
    #we need initial guess of shape and scale paramenter which is k and lam, k is chosen as 1.5 as k=1 would correspond to exponential, and k>1 will
    #will generate extreme values and k<1.5 respresent the failure rate decreases over time, or, equivalently, we have a high rate of early failures (“infant mortality”),
    #source: https://brandewinder.com/2022/08/28/mle-of-weibull-process/
    #lambda the shape paramteter initial guess is chosen closer to the mean
from scipy.special import gamma
def weibullparameters (estimatedparams, mean, std):
    k,lam=estimatedparams
    equation1= lam * gamma(1 + 1/k) - mean
    equation2= lam**2 * (gamma(1 + 2/k) - (gamma(1 + 1/k))**2) - std**2
    return equation1,equation2

# def predictlos(ageGroup,resource1,resource2):
#    #we need to  change this
#     if ageGroup==0:
#         if resource1==1 & resource2==0:
#             initialguess = (1.5, 4)
#             mean_icu = 3.6
#             std_icu = 4.1
#             k_icu, k_lam = fsolve(weibullparameters, initialguess, args=(mean_icu, std_icu))
#             value = np.random.weibull(k_icu) * k_lam
#         if resource1==1&resource2==1:
#             initialguess = (1.5, 18)
#             mean_icu = 17.5
#             std_icu = 11.8
#             k_icu, k_lam = fsolve(weibullparameters, initialguess, args=(mean_icu, std_icu))
#             value = np.random.weibull(k_icu) * k_lam
#         if resource1 == 0 & resource2 == 1:
#             initialguess = (1.5, 13)
#             mean_icu = 13.2
#             std_icu = 14.0
#             k_icu, k_lam = fsolve(weibullparameters, initialguess, args=(mean_icu, std_icu))
#             value = np.random.weibull(k_icu) * k_lam

#     if ageGroup==1:
#         if resource1 == 1 & resource2 == 0:
#             initialguess = (1.5, 6)
#             mean_icu = 5.9
#             std_icu = 5.9
#             k_icu, k_lam = fsolve(weibullparameters, initialguess, args=(mean_icu, std_icu))
#             value = np.random.weibull(k_icu) * k_lam
#         if resource1 == 1 & resource2 == 1:
#             initialguess = (1.5, 20)
#             mean_icu = 19.5
#             std_icu = 14.3
#             k_icu, k_lam = fsolve(weibullparameters, initialguess, args=(mean_icu, std_icu))
#             value = np.random.weibull(k_icu) * k_lam
#         if resource1 == 0 & resource2 == 1:
#             initialguess = (1.5, 15)
#             mean_icu = 15.4
#             std_icu = 14.2
#             k_icu, k_lam = fsolve(weibullparameters, initialguess, args=(mean_icu, std_icu))
#             value = np.random.weibull(k_icu) * k_lam
#     if ageGroup == 2:
#         if resource1 == 1 & resource2 == 0:
#             initialguess = (1.5, 8)
#             mean_icu = 8.3
#             std_icu = 7.7
#             k_icu, k_lam = fsolve(weibullparameters, initialguess, args=(mean_icu, std_icu))
#             value = np.random.weibull(k_icu) * k_lam
#         if resource1 == 1 & resource2 == 1:
#             initialguess = (1.5, 17)
#             mean_icu = 17.1
#             std_icu = 13.5
#             k_icu, k_lam = fsolve(weibullparameters, initialguess, args=(mean_icu, std_icu))
#             value = np.random.weibull(k_icu) * k_lam
#         if resource1 == 0 & resource2 == 1:
#             initialguess = (1.5, 14)
#             mean_icu = 13.6
#             std_icu = 13.8
#             k_icu, k_lam = fsolve(weibullparameters, initialguess, args=(mean_icu, std_icu))
#             value = np.random.weibull(k_icu) * k_lam
#     if ageGroup == 3:
#         if resource1 == 1 & resource2 == 0:
#             initialguess = (1.5, 10)
#             mean_icu = 10
#             std_icu = 9.4
#             k_icu, k_lam = fsolve(weibullparameters, initialguess, args=(mean_icu, std_icu))
#             value = np.random.weibull(k_icu) * k_lam
#         if resource1 == 1 & resource2 == 1:
#             initialguess = (1.5, 13)
#             mean_icu = 12.6
#             std_icu = 10.8
#             k_icu, k_lam = fsolve(weibullparameters, initialguess, args=(mean_icu, std_icu))
#             value = np.random.weibull(k_icu) * k_lam
#         if resource1 == 0 & resource2 == 1:
#             initialguess = (1.5, 11)
#             mean_icu = 11.4
#             std_icu = 11.4
#             k_icu, k_lam = fsolve(weibullparameters, initialguess, args=(mean_icu, std_icu))
#             value = np.random.weibull(k_icu) * k_lam
#     return np.round(value).astype(int)
stats = {0 : { 10 : {'initialguess' : (1.5,4),'mean': 3.6, 'stdev' : 4.1},
               1 : {'initialguess' : (1.5,13),'mean': 13.2, 'stdev' : 14.0},
               11 : {'initialguess' : (1.5,18),'mean': 17.5, 'stdev' : 11.8}
                },
         1 : { 10 : {'initialguess' : (1.5,6),'mean': 5.9, 'stdev' : 5.9},
               1 : {'initialguess' : (1.5,15),'mean': 15.4, 'stdev' : 14.2},
               11 : {'initialguess' : (1.5,20),'mean': 19.5, 'stdev' : 14.3}
                },
         2 :{10 : {'initialguess' : (1.5,8),'mean': 8.3, 'stdev' : 7.7},
              1 : {'initialguess' : (1.5,14),'mean': 13.6, 'stdev' : 13.8},
               11 : {'initialguess' : (1.5,17),'mean': 17.1, 'stdev' : 13.5}},
         3: {10 : {'initialguess' : (1.5,10),'mean': 10, 'stdev' : 9.4},
               1 : {'initialguess' : (1.5,11),'mean': 11.4, 'stdev' : 11.4},
               11 : {'initialguess' : (1.5,13),'mean': 12.6, 'stdev' : 10.8}},
         
        4: {10 : {'initialguess' : (1.5,9),'mean': 10, 'stdev' : 9.5},
               1 : {'initialguess' : (1.5,13),'mean': 13.4, 'stdev' : 13.8},
               11 : {'initialguess' : (1.5,17),'mean': 17.3, 'stdev' : 13.1}}
        }


def predParams():
 #param=[]
 param={}
 for outkey, invalue in stats.items():
    param[outkey]=[]
    for inkey, values in invalue.items():
        initialguess = values['initialguess']
        mean = values['mean']
        stdev = values['stdev']
        k, lam = fsolve(weibullparameters, initialguess, args=(mean, stdev))

        
        param[outkey].append({inkey:[k,lam]})
 return param      
 

def predLoss(ageGroup,resnum,resources):
    params=predParams()   
    k = params[ageGroup][resnum][resources][0]
    lam = params[ageGroup][resnum][resources][1]
    los_data=[]
    value =( np.random.weibull(k) * lam)
    for i in range (1000):
        value =( np.random.weibull(k) * lam)
        los_data.append(value)
    df=pd.DataFrame(los_data)
    file_name = "results.xlsx"
    df.to_excel(file_name, index=False)
    return int(value)
if __name__== "__main__":
    
    predLoss(0,0,10)
   
    


          
# for i in range(4):
#     for k in (1,10,11):
#         print (f'for {i} and {k}, mean is {stats[i][k]["mean"]}')
#         print (f'for {i} and {k}, stdev is {stats[i][k]["stdev"]}')

