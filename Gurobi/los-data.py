stats = {0 : { 1 : {'mean': 3.6, 'stdev' : 4.1},
               10 : {'mean': 13.2, 'stdev' : 14.0},
               11 : {'mean': 17.5, 'stdev' : 11.8}
                },
         1 : { 1 : {'mean': 5.9, 'stdev' : 5.9},
               10 : {'mean': 13.2, 'stdev' : 14.0},
               11 : {'mean': 15.4, 'stdev' : 14.2}
                }
}
#         2 :
#         3 : }

params = {0 : {1 : {'k' : 5, 'lambda' : 3}}}

for i in range(4):
    for k in (1,10,11):
        print (f'for {i} and {k}, mean is {stats[i][k]["mean"]}')
        print (f'for {i} and {k}, stdev is {stats[i][k]["stdev"]}')