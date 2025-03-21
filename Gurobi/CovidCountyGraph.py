from sqlite3 import Time
import pandas as pd
from tkinter import filedialog
from decouple import config
import ou_file_utils as ofu
import time
from plotly.graph_objs import Bar, Layout
from plotly import offline


df=pd.read_excel(ofu.getFile())

Date=df["Date"]
Count=df["Count"]
print(Count)
#y_axis_config = {'title': 'Number of Cases'}
#x_axis_config = {'title': 'Date'}
#x_values = Count
#y_values = Date

data = [Bar (x=Date, y=Count)]
my_layout = Layout(title='County Coronavirus Cases per day')
offline.plot({'data':data, 'layout':my_layout}, filename='Countycasesperday.html')




