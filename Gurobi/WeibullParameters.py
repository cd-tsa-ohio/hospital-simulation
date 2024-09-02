import numpy as np
from scipy.optimize import fsolve
from scipy.special import gamma



def weibullparameters (estimatedparams, mean, std):
    k,lam=estimatedparams
    #got the equation of mean using mean=lam*gamaa(1+1/k) source https://en.wikipedia.org/wiki/Weibull_distribution
    #we need initial guess of shape and scale paramenter which is k and lam, k is chosen as 1.5 as k=1 would correspond to exponential, and k>1 will
    #will generate extreme values and k<1.5 respresent the failure rate decreases over time, or, equivalently, we have a high rate of early failures (“infant mortality”),
    #source: https://brandewinder.com/2022/08/28/mle-of-weibull-process/
    #lambda the shape paramteter initial guess is chosen closer to the mean

    equation1= lam * gamma(1 + 1/k) - mean
    equation2= lam**2 * (gamma(1 + 2/k) - (gamma(1 + 1/k))**2) - std**2
    return equation1,equation2
#mean and standard deviation
mean_non_icu=9.1 #for population age group 0
std_non_icu=9.5

initialguess=(1.5,10)
#Function to predict los
def predictlos(ageGroup):
    if ageGroup==0:
        mean_icu = 10
        std_icu = 4
        k_icu, k_lam = fsolve(weibullparameters, initialguess, args=(mean_icu, std_icu))
        value=np.random.weibull(k_icu)*k_lam
    if ageGroup==1:
        mean_icu = 15
        std_icu = 4
        k_icu, k_lam = fsolve(weibullparameters, initialguess, args=(mean_icu, std_icu))
        value = np.random.weibull(k_icu) * k_lam
    if ageGroup == 2:
        mean_icu = 14
        std_icu = 4
        k_icu, k_lam = fsolve(weibullparameters, initialguess, args=(mean_icu, std_icu))
        value = np.random.weibull(k_icu) * k_lam
    if ageGroup == 3:
        mean_icu = 17.3
        std_icu = 10
        k_icu, k_lam = fsolve(weibullparameters, initialguess, args=(mean_icu, std_icu))
        value = np.random.weibull(k_icu) * k_lam
    return np.round(value).astype(int)
ageGroup=3
los_predictions_icu=predictlos(ageGroup)
print(f"length of stay predictions : {los_predictions_icu}")

