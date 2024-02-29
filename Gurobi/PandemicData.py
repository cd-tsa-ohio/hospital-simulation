#from plotly.graph_objs import Scattergeo, Layout
import pandas as pd
import numpy as np
import openpyxl

file_path=("C:/Users/HP/Ohio University/Sormaz, Dusan - malik-shared/PhD - Research/DataFiles/COVID-19_Reported_Patient_Impact_and_Hospital_Capacity_by_Facility_--_RAW_20240226.csv")
df=pd.read_csv(file_path)
pd.set_option('display.max_columns',100)
new_df=df[(df['state']=='OH')& (df['zip']==45701)]
newFile=("C:/Users/HP/Ohio University/Sormaz, Dusan - malik-shared/PhD - Research/DataFiles/Athens.csv")
try:
    new_df.to_csv(newFile)
except Exception as e:
    print("file could not be saved")

print (df['state'].head(3), df ['zip'].head(4), sep="#")