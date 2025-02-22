import WeibullParameters as wb
import numpy as np
from scipy.optimize import fsolve

initialguess = (2,500)
mean = (505, 495, 480, 450, 320)
std = (200, 190, 180, 175, 150)

for i in range (5):
    k, lam = fsolve(wb.weibullparameters, initialguess, args=(mean[i], std[i]))
    print (f'mean={mean[i]}, std={std[i]}, k={k}, lam={lam}')



for i in range(100):
    value = np.random.weibull(k) * lam
    print(value)