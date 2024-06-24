#from plotly.graph_objs import Scattergeo, Layout
import decouple
import pandas as pd
import numpy as np
import openpyxl
import decouple
from decouple import config


# config solution (DATA_FOLDER) used from
# https://able.bio/rhett/how-to-set-and-get-environment-variables-in-python--274rgt5

DATA_FOLDER = config('GUR_DATA_FOLDER')


file_path=(DATA_FOLDER+"AthesDataAnalysis.xlsx")

file_path2=(DATA_FOLDER+"time_series_covid19_confirmed_US.xlsx")


df=pd.read_excel(file_path,sheet_name="AthensFile", usecols=["Date","total_adult_patients_hospitalized_confirmed_covid_7_day_sum"])
#pd.set_option('display.max_columns',100)
new_df=df.nlargest(5,"total_adult_patients_hospitalized_confirmed_covid_7_day_sum")

df2=pd.read_excel(file_path2,sheet_name="AthensDailyConfirmed",usecols=["Date","DailyCounts"])
new_df2=pd.merge(left=new_df,right=df2, on="Date", how="left")



newFile=("C:/Users/HP/Ohio University/Sormaz, Dusan - malik-shared/PhD - Research/DataFiles/DailyLeftJoinDataFile3.xlsx")
try:
    new_df2.to_excel(newFile)
except Exception as e:
    print("file could not be saved")

