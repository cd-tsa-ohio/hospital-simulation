# module to perform various file operations. Currentlyly implements excel and CSV fiel read and
# generation of pandas dataframe from the file 
# If run as main, prompts user to select a file, reads it, makes dataframe, and shows its head
# 
# Author Dusan Sormaz
# Version 1.0 
# change log
# Date          Author      Content
# 11/22/2024    D. Sormaz   Intial implementation
from decouple import config
import pandas as pd
import tkinter as tk
from tkinter import filedialog

def getFile():
    root = tk.Tk()
    root.withdraw()
    try: 
        DATA_FOLDER = config('DF_DATA_FOLDER')
    except:
        DATA_FOLDER = '.'
    file_path = filedialog.askopenfilename(initialdir=DATA_FOLDER)
    return file_path

def getDataFrameFromFile(file):
    if file.endswith('xlsx'):
        df = pd.read_excel(file)
    elif file.endswith('csv'):
        df = pd.reads_csv(file)
    else:
        ext = file[file.rindex('.'):]
        raise NotImplementedError (f'Not supported file extension: {ext}')
    return df

def getDataFrame():
        return getDataFrameFromFile(getFile())

if __name__ == "__main__":
    df = getDataFrame()
    print(df.head())