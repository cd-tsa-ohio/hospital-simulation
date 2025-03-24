

from sqlite3 import Time
import pandas as pd
from tkinter import filedialog
from decouple import config
import ou_file_utils as ofu
import time
def filterData(state,county,file_name):
    df=pd.read_excel(file_name)
    new_df=df[(df['Province_State']==state)& (df['Admin2']==county)]
    transposeddf=new_df.T
    newFile=f"{county[0:4]}{time.time()}.xlsx"
    try:
        transposeddf.iloc[12:].to_excel(newFile,sheet_name=newFile)
    except Exception as e:
        print(e)
if __name__=="__main__":
   file_name=ofu.getFile()
   #filterData("Ohio","Morgan",file_name)
   #filterData("Ohio","Perry",file_name)
   #filterData("Ohio","Hocking",file_name)
   #filterData("Ohio","Vinton",file_name)
   #filterData("Ohio","Meigs",file_name)
   filterData("Ohio","Washington",file_name)
