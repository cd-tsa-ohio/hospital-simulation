# add header here
# 
from decouple import config
import pandas as pd
import tkinter as tk
import ou_file_utils as ofu
from tkinter import filedialog


def getData(file_path):
    df1 = pd.read_excel(file_path, sheet_name=None)
    capacitylist1=[]
    capacitylist2=[]
    patientData2 = []
    resource1list=[]
    resource2list=[]
    for sheet_name,values in df1.items():
        x=[]
        df2 = pd.DataFrame(values)
        capacityrow = df2[df2['Patient'] == 'capacity1'].index[0]
        capacity1 = df2.iloc[capacityrow, 1:-4]
        capacitylist1.append(capacity1)
        capacityrow2 = df2[df2['Patient'] == 'capacity2'].index[0]
        capacity2 = df2.iloc[capacityrow2, 1:-4]
        capacitylist2.append(capacity2)
        resource1 = df2['Resource1'].dropna().tolist()
        resource2 = df2['Resource2'].dropna().tolist()
        resource1list.append(resource1)
        resource2list.append(resource2)
        blank_line_index = df2[df2['Patient'].isna()].index[0]
        for items in range(1, blank_line_index + 1):
            patientDataRow = df2[df2['Patient'] == items].index[0]
            patientData = df2.iloc[patientDataRow, 1:-4]
            x.append(patientData)
        patientData2.append(x)

    #print(xlist)
    #Dr.Sormaz comments: need to remove ifs and then make it flecible to run model 1 and model 2 ..
    if len(patientData2)==1:
        return patientData2[0],capacitylist1[0]
    else:
        return patientData2,capacitylist1,capacitylist2,resource1list,resource2list,file_path
if __name__ == "__main__":
    gd = getData(ofu.getFile())
    print (type(gd))
    print(gd)
