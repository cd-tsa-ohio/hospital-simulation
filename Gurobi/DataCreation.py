#let assume los =4
#creating function to get los
#los= (WeibullParameters.predictlos(WeibullParameters.k_icu,WeibullParameters.k_lam))
#getting daily counts of patients that needs to be admitted

#first iterate over dates:
import random
import pandas as pd
from decouple import config
import WeibullParameters
import tkinter as tk
from tkinter import filedialog
# config solution (DATA_FOLDER) used from
# https://able.bio/rhett/how-to-set-and-get-environment-variables-in-python--274rgt5
root = tk.Tk()
root.withdraw()
DATA_FOLDER = config('GUR_DATA_FOLDER')
file_path = filedialog.askopenfilename(initialdir=DATA_FOLDER)
df1 = pd.read_excel(file_path, sheet_name=None)
df2=pd.DataFrame()
newFileName=""
for sheet_name,values in df1.items():
    sheetDataframe=pd.DataFrame(values)
    date_data=sheetDataframe["Date"]
    df2 = pd.DataFrame(columns=['Patient'])
    for date in date_data:
        df2[date] = None
    df2["AgeGroup"]=None
    df2["AcuityLevel"]=None
    df2["Resource1"]=None
    df2["Resource2"]=None
    count_data=sheetDataframe["DailyCounts"]
    count=0
    for i in range(0,len(date_data)):
        percent_data = int(round(count_data[i] * 0.10))
        for j in range (1,percent_data+1):
            count=count+1
            df2.at[count,'Patient']=count
            df2.at[count,'AgeGroup']=random.randint(0, 3)
            df2.at[count, 'Resource1']=random.randint(0,1)
            df2.at[count, 'Resource2'] = random.randint(0, 1)
            date=date_data[i]
            df2.at[count, date] = 1
            los = (WeibullParameters.predictlos(df2.at[count,'AgeGroup','Resource1','Resource2']))
            if los<=7:
                df2.at[count, 'AcuityLevel'] = 1
            if los>7&los<=10:
                df2.at[count, 'AcuityLevel'] = 2
            if los>10:
                df2.at[count, 'AcuityLevel'] = 3
            for days in range (1,los):
                if date + pd.Timedelta(los) > date_data.iloc[-1]:
                    break
                date=date+pd.Timedelta(days=1)
                df2.at[count,date]=1
    df2.at[count+3, 'Patient'] = "capacity1"
    df2.at[count + 4, 'Patient'] = "capacity2"
    newFileName=f"{sheet_name}.xlsx"
print (count_data)
df2.to_excel(DATA_FOLDER+newFileName,index=False)
print (df2["Patient"].values)



