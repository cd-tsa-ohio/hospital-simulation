import random

import pandas as pd
import numpy as np
import openpyxl
import decouple
from decouple import config

# config solution (DATA_FOLDER) used from
# https://able.bio/rhett/how-to-set-and-get-environment-variables-in-python--274rgt5

DATA_FOLDER = config('GUR_DATA_FOLDER')


file_path=(DATA_FOLDER+"DailyLeftJoinDataFile3.xlsx")
df1=pd.read_excel(file_path,sheet_name="OctoberData")
#creating new DataFrame with coulmnn 1 contianing data of patients
df2 = pd.DataFrame(columns=['Patient'])
date_data=df1["Date"]

# Add each date from date_data as a new column in df2
for date in date_data:
    df2[date] = None
#inserting columns age group, acuity type and resource information

df2["AgeGroup"]=None
df2["AcuityLevel"]=None
df2["Resources"]=None
#Adding patient
count_data=df1["DailyCounts"]
#let assume los =4
los=4
#getting daily counts of patients that needs to be admitted

#first iterate over dates:
count=0
for i in range(0,30):
    percent_data = int(round(count_data[i] * 0.10))
    #create patients
    for j in range (1,percent_data+1):

            #putting the value of date coulmn 1/10/2021 at location 0=1 and so on ..
                #df2.at[count, d] = 1
        count=count+1
        df2.at[count,'Patient']=count
        df2.at[count,'AgeGroup']=random.randint(0, 3)
        df2.at[count,'AcuityLevel']=random.randint(1, 4)
        df2.at[count, 'Resources']=random.randint(1, 2)

    #get date
        date=date_data[i]
        df2.at[count, date] = 1
        for days in range (1,los):
            if date+pd.Timedelta(los)<=date_data[30]:
                date=date+pd.Timedelta(days=1)
                df2.at[count,date]=1

#putting los for each patient

        #new_rows = pd.DataFrame({'Patient': range(1, percent_data + 1)})
        #df2 = pd.concat([df2, new_rows], ignore_index=True)
print (count_data)
df2.to_excel(DATA_FOLDER+"DataCreation.xlsx")
print (df2["Patient"].values)


