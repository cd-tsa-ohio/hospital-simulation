import gurobipy as gp
from gurobipy import GRB
import GetData
import math
from Results import readresults

#Adding multiple resource Constraint Latest after 9/20
#Parameters
model = gp.Model("OptimsationModel")
x, capacity,capacity2, resource1list,resource2list,file_path= GetData.getData(GetData.getFile())
totalCaplist=[]
totalCaplist.extend([capacity,capacity2])
totalResourcelist=[]
totalResourcelist.extend([resource1list,resource2list])
totalCapacity=3
periods=3
periodlength=3
#Decision Variables
y = {}
cap={}
capacityEveryDay={}
for region in range(len(x)):  # Iterate over regions
    for patient in range(len(x[region])):
        y[region, patient] = model.addVar(vtype=GRB.BINARY, name=f"Region_{region} Patient_{patient}")
totalPatients =gp.quicksum (var for var in y.values())
for p in range(1,periods+1):
    for region in range(len(x)):
        for re in range(0, len(totalCaplist)):
            cap[p,region,re]= model.addVar(vtype=GRB.INTEGER,name=f"cap_Period_{p} Region_ {region} resource_{re}")
for region in range(len(x)):
    for re in range(0, len(totalCaplist)):
        for i in range(1, len(capacity[region]) + 1):
            capacityEveryDay[region,re,i]= model.addVar(vtype=GRB.INTEGER,name=f"capacityEveryDay_ {region} resource_{re} day_ {i}")
#Model Objective
model.setObjective(totalPatients, GRB.MAXIMIZE)

#Model Constraints
for p in range(1,periods+1):
    for re in range(0, len(totalCaplist)):
        model.addConstr(gp.quicksum(cap[p,r,re] for r in range(len(x)))<=totalCapacity)
for r in range(len(x)):  # Iterate over regions
        for i in range(1, len(capacity[r])+1):#Iterate over days
            for re in range (0,len(totalCaplist)):
               model.addConstr(capacityEveryDay[r, re, i] == cap[math.ceil(i / periodlength), r, re])
               model.addConstr(gp.quicksum(y[r, m] * x[r][m][i]*totalResourcelist[re][r][m]for m in range(len(x[r])))<=capacityEveryDay[r,re,i])
model.update()
model.optimize()

print("\nPatient Assignments:")
for var in y.values():
    print(f"{var.VarName} = {var.x}")
for var in cap.values():
    print(f"{var.VarName} = {var.x}")
for var in capacityEveryDay.values():
    print(f"{var.VarName} = {var.x}")