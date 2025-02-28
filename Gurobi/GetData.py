# add header here
# 
from decouple import config
import pandas as pd
import tkinter as tk
import ou_file_utils as ofu
from tkinter import filedialog


def getData(file_path):
    df1 = pd.read_excel(file_path, sheet_name=None)
    patientData2 = []
    resourceList=[]
    capacityList=[] 
    for sheet_name,values in df1.items():
        countResources=0
        x=[]
        df2 = pd.DataFrame(values)
        for col in df2.columns:
           if isinstance(col, str) and col.startswith("R"):
               countResources+=1
        resourceListforRegion=([df2[col].dropna().tolist() for col in df2.columns if isinstance(col, str) and col.startswith("R")])
        resourceList.append(resourceListforRegion)
        capacityListRegion=([row[1:-(5+countResources)] for row in df2.values if isinstance(row[0], str) and row[0].startswith("c")])
        capacityList.append(capacityListRegion)
        
        
        blank_line_index = df2[df2['Patient'].isna()].index[0]
        for items in range(1, blank_line_index + 1):
            patientDataRow = df2[df2['Patient'] == items].index[0]
            patientData = df2.iloc[patientDataRow, 1:-(5+countResources)]
            x.append(patientData)
        patientData2.append(x)
    return patientData2,capacityList,resourceList,file_path
if __name__ == "__main__":
    gd = getData(ofu.getFile())
    print (type(gd))
    print(gd[2])
    rl_df = pd.DataFrame(gd[2])
    transp = rl_df.transpose()
    print('original')
    print (rl_df)
    print('transposed')
    print(transp)