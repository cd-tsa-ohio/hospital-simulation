

from sqlite3 import Time
import pandas as pd
from tkinter import filedialog
from decouple import config
import ou_file_utils as ofu
import time
df=pd.read_excel(ofu.getFile())
pd.set_option('display.max_columns',100)
county=input("County name: ")
state=input ("state: ")
#zip=input ("zip number")
new_df=df[(df['Province_State']==state)& (df['Admin2']==county)]
transposeddf=new_df.T
newFile=f"{county}{time.time()}.xlsx"
try:
    transposeddf.iloc[12:].to_excel(newFile,sheet_name=newFile)
except Exception as e:
    print("file count  could not be saved")