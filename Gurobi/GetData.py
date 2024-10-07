from decouple import config
import pandas as pd
import tkinter as tk
from tkinter import filedialog


def getData():
    root = tk.Tk()
    root.withdraw()
    DATA_FOLDER = config('GUR_DATA_FOLDER')
    file_path = filedialog.askopenfilename(initialdir=DATA_FOLDER)
    df1 = pd.read_excel(file_path, sheet_name=None)


    #sheets=[]
    capacitylist1=[]
    capacitylist2=[]

    xlist = []
    resource1list=[]
    resource2list=[]
    #sheetname = input("write sheet name: ")
    # while sheetname!="Done":
    #     sheets.append(sheetname)
    #     sheetname = input("write another sheet name: ")
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
        resource2 = df2['Resource1'].dropna().tolist()
        blank_line_index = df2[df2['Patient'].isna()].index[0]
        #started with 1 because there is no patient with 0 number
        for items in range(1, blank_line_index + 1):
            patientDataRow = df2[df2['Patient'] == items].index[0]
            patientData = df2.iloc[patientDataRow, 1:-4]
            x.append(patientData)
        xlist.append(x)
        resource1list.append(resource1)
        resource2list.append(resource2)

    #print(xlist)
    if len(xlist)==1:
        return xlist[0],capacitylist1[0]
    else:
        return xlist,capacitylist1,capacitylist2,resource1list,resource2list
    #print (capacitylist)
    #print("x List \n",xlist)
    #r eturn xlist,capacitylist
if __name__ == "__main__":
    getData()
