import gurobipy as gp
from gurobipy import GRB
import GetData
import math
from Results import writeResults
import ou_file_utils as ofu
#Adding multiple resource Constraint Latest after 9/20
#Parameters
def model3( totalCapacity, periodlength):
    model = gp.Model("OptimsationModel")
    x, totalCaplist, totalResourcelist,file_path= GetData.getData(ofu.getFile())

    #input parameter
    #totalCapacity=int(input("Enter global capacity: "))
    #periodlength=int(input("enter length of each period:"))
    periods=int(len(totalCaplist[0][0])/int(periodlength))
    #input parameter

    #Decision Variables
    y = {}
    cap={}
    z={}
    capacityEveryDay={}
    for region in range(len(x)):  # Iterate over regions
        for patient in range(len(x[region])):
            y[region, patient] = model.addVar(vtype=GRB.BINARY, name=f"Region_{region} Patient_{patient}")
    totalPatients =gp.quicksum (var for var in y.values())
    for p in range(1,periods+1):
        for region in range(len(x)):
            for re in range (0,len(totalResourcelist[region])):
                cap[p,region,re]= model.addVar(vtype=GRB.INTEGER,name=f"cap_Period_{p} Region_ {region} resource_{re}")
    for r in range(len(x)):
        for re in range (0,len(totalResourcelist[r])):
            for res in range( len(totalCaplist[r])):
                for days in range (1,len(totalCaplist[r][res])+1):
                    capacityEveryDay[r,re,days]= model.addVar(vtype=GRB.INTEGER,name=f"capacityEveryDay_ {r} resource_{re} day_ {days}")
    #Model Objective
    model.setObjective(totalPatients, GRB.MAXIMIZE)

    #Model Constraints
    for p in range(1,periods+1):
        for re in range (0,len(totalResourcelist[r])):
            model.addConstr(gp.quicksum(cap[p,r,re] for r in range(len(x)))<=totalCapacity)
    for r in range(len(x)):  # Iterate over regions
        for res in range( len(totalCaplist[r])):
            for days in range (1,len(totalCaplist[r][res])+1):#Iterate over days
                for re in range (0,len(totalCaplist[r])):  
                    model.addConstr(capacityEveryDay[r, re, days] == cap[math.ceil(days / periodlength), r, re])
                    model.addConstr(gp.quicksum(y[r, m] * x[r][m][days]*totalResourcelist[r][re][m]for m in range(len(x[r])))<=capacityEveryDay[r,re,days])
    model.update()
    model.optimize()

    print("\nPatient Assignments:")
    for var in y.values():
        print(f"{var.VarName} = {var.x}")
    #for var in cap.values():
     #   print(f"{var.VarName} = {var.x}")
    for var in capacityEveryDay.values():
        print(f"{var.VarName} = {var.x}")
    writeResults(y, z,capacityEveryDay, file_path)
if __name__== "__main__":
    model3(3,1)