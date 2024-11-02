import pandas as pd
import openpyxl
from openpyxl.styles import PatternFill
from decouple import config
import tkinter as tk
from tkinter import filedialog

def writeResults(y:dict, z:dict, file_path):
    # root = tk.Tk()
    # root.withdraw()
    # DATA_FOLDER = config('GUR_DATA_FOLDER')
    #remove this filepath and get it from the model
    # file_path = filedialog.askopenfilename(initialdir=DATA_FOLDER)
    df1 = pd.read_excel(file_path, sheet_name=None)
    counterA = 0
    counterT=0
    newFileName = "answers.xlsx"
    with pd.ExcelWriter(file_path, engine='xlsxwriter') as writer:
        for sheet_name, values in df1.items():
            df2 = pd.DataFrame(values)
            df2["PatientAccepted"] = None
            countA = 0
            countT=0
            for key, var in y.items():
                if key[0]==counterA:
                    df2.at[countA,'PatientAccepted'] = var.X
                    countA=countA+1
            counterA = counterA + 1
            for key, var in z.items():
                if key[1] == counterT:
                    if var.X==1:
                        df2.at[countT, 'SuccesfulTransferto'] = key[0]+1
                        df2.at[countT,'SuccefulTansferPatientNumber']=key[2]+1
                        countT = countT + 1
            counterT = counterT + 1
            df2.to_excel(writer, sheet_name=sheet_name, index=False)

#need to copy sheets
#then print sheets
#find out how z can be done