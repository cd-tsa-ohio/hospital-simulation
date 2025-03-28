import pandas as pd
import openpyxl
from openpyxl.styles import PatternFill
from decouple import config
import tkinter as tk
from tkinter import filedialog
#import xlsxwriter

def writeResults(y:dict, z:dict,capacityEveryDay:dict, file_path):
    df1 = pd.read_excel(file_path, sheet_name=None)
    counterA = 0
    counterT=0
    newFileName = "answers.xlsx"
    resNumber=0
    with pd.ExcelWriter(file_path, engine='openpyxl',mode='a',if_sheet_exists='overlay') as writer:
        for sheet_name, values in df1.items():
            df2 = pd.DataFrame(values)
            df2["PatientAccepted"] = None
            #df2["CapacityAllocated"]=None
            countA = 0
            countT=0
            CountCA=1
            for key, var in y.items():
                if key[0]==counterA:
                    df2.at[countA,'PatientAccepted'] = var.X
                    countA=countA+1
            for key, var in capacityEveryDay.items():        
                if key[0]==counterA:                  
                       #df2.at[f'capacity{key[1]}',key[2]]=var.X  
                       df2.loc[df2['Patient'] == f'capacity{key[1]+1}', key[2]] =var.X          
           # if (len(capacityEveryDay)>0):  
            counterA = counterA + 1                             
            for key, var in z.items():
                if key[1] == counterT:
                    if var.X==1:
                        df2.at[countT, 'SuccesfulTransferto'] = key[0]+1
                        df2.at[countT,'SuccefulTansferPatientNumber']=key[2]+1
                        countT = countT + 1
            counterT = counterT + 1          
            df2.to_excel(writer, sheet_name=sheet_name, index=False)
#if __name__== "__main__":
   #with pd.ExcelWriter("test.xlsx", engine='openpyxl',mode='a',if_sheet_exists='overlay') as writer:
    #df=pd.read_excel("test.xlsx")
    #df.loc[df['B'] == 'cap', 5]=900
    #df.loc[df['cap'],7]=900
    #df.to_excel("test.xlsx")
    #df.to_excel(writer,  index=False) 