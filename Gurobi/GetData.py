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
    capacitylist=[]
    xlist = []
    #sheetname = input("write sheet name: ")
    # while sheetname!="Done":
    #     sheets.append(sheetname)
    #     sheetname = input("write another sheet name: ")
    for sheet_name,values in df1.items():
        x=[]
        df2 = pd.DataFrame(values)
        capacityrow = df2[df2['patient'] == 'capacity'].index[0]
        capacity = df2.iloc[capacityrow, 1:-3]
        capacitylist.append(capacity)
        blank_line_index = df2[df2['patient'].isna()].index[0]
        #started with 1 because there is no patient with 0 number
        for items in range(1, blank_line_index + 1):
            patientDataRow = df2[df2['patient'] == items].index[0]
            patientData = df2.iloc[patientDataRow, 1:-3]
            x.append(patientData)
        xlist.append(x)
    #print(xlist)
    if len(xlist)==1:
     return xlist[0],capacitylist[0]
    else:
        return xlist,capacitylist
    #print (capacitylist)
    #print("x List \n",xlist)
    #return xlist,capacitylist

if __name__ == "__main__":
    getData()
