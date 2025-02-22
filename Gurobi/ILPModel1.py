import gurobipy as gp
import openpyxl
from gurobipy import GRB
from decouple import config
import ou_file_utils as ofu
import GetData
from Results import writeResults
x, totalCaplist, totalResourcelist,file_path= GetData.getData(ofu.getFile())
model=gp.Model("OptimsationModel")
patients=range(1, len(x))
y = {}
z={}
capacityEveryDay={}
for region in range(len(x)):  # Iterate over regions (outer list)
    for patient in range(0,len(x[region])):
        y[region, patient] = model.addVar(vtype=GRB.BINARY, name=f"Region_{region} Patient_{patient}")
#Objective
totalPatients = gp.quicksum(var for var in y.values())
model.setObjective(totalPatients,GRB.MAXIMIZE)

#Contranints
#for i in days:
for r in range(len(x)):
            for res in range( len(totalCaplist[r])):
                for days in range (1,len(totalCaplist[r][res])+1):#Iterate over days
                    for re in range (0,len(totalResourcelist[r])):
                                    
         #model.addConstr(gp.quicksum(z[m,i] for m in patients)<= capacity[i],f"capacity_{i}")
                        model.addConstr(gp.quicksum(y[r, m] * x[r][m][days]*totalResourcelist[r][re][m] for m in range(len(x[r])))<=totalCaplist[r][res][days-1])


model.update()
model.optimize()
for var in y.values():
   print(f"{var.VarName} = {var.x}")

writeResults(y, z,capacityEveryDay, file_path)