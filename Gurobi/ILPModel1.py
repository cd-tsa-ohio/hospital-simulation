import gurobipy as gp
import openpyxl
from gurobipy import GRB
from decouple import config
import pandas as pd

#this is model 1
#this is one resource one region model
#this model is modified to read from data folder (orginal model was ProposalModel)
#the model is also further modified to read from dataframe
DATA_FOLDER = config('GUR_DATA_FOLDER')
sheetname=input("write file name: ")
sheet_name=input("write sheet name: ")
df1=pd.read_excel(DATA_FOLDER+sheetname,sheet_name=sheet_name)
capacityrow= df1[df1['patient']=='capacity'].index[0]
#input number of days
numDays=int(input("Enter number of days"))
capacity = df1.iloc[capacityrow, 1:numDays+2]
print(capacity)
blank_line_index = df1[df1['patient'].isna()].index[0]
x=[]
for values in range (1,blank_line_index+1):
    patientDataRow= df1[df1['patient']==values].index[0]
    patientData = df1.iloc[patientDataRow, 1:numDays+2]
    x.append(patientData)
print (x)
model=gp.Model("OptimsationModel")
patients=range(0,len(x))
days=range(1,numDays)

#Variables
y= model.addVars(patients,vtype=GRB.BINARY,name='y')

z=model.addVars(patients,days,vtype=GRB.INTEGER,lb=0,name="z")

#Objective
totalPatiens=y.sum()
model.setObjective(totalPatiens,GRB.MAXIMIZE)

#Contranints
for i in days:
     model.addConstr(gp.quicksum(z[m,i] for m in patients)<= capacity[i],f"capacity_{i}")
for i in days:
    for m in patients:
        model.addConstr(z[m,i]==x[m][i]*y[m], f"z_{m}_{i}")
model.optimize()


