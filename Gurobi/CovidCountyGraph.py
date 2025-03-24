from sqlite3 import Time
import pandas as pd
from tkinter import filedialog
from decouple import config
import ou_file_utils as ofu
import time
from plotly.graph_objs import Bar, Layout
from plotly import offline

def createGraph(file_name):
    df=pd.read_excel(file_name,MaxDate,MinDate)

    
    
    #y_axis_config = {'title': 'Number of Cases'}
    #x_axis_config = {'title': 'Date'}
    #x_values = Count
    #y_values = Date

    new_df=df[(df['Date'] >=MaxDate ) & (df['Date'] <=MinDate )]
    Date=new_df["Date"]
    Count=new_df["DailyCount"]
    data = [Bar (x=Date, y=Count)]
    my_layout = Layout(title=f'County Coronavirus Cases per day')
    offline.plot({'data':data, 'layout':my_layout}, filename=f'{file_name} Countycasesperday.html')

if __name__=="__main__":
   file_name=ofu.getFile()
   MaxDate='2020-09-01'
   MinDate='2021-01-31'
   createGraph(file_name,MaxDate,MinDate)


