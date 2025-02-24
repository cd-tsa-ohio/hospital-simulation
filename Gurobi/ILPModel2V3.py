import gurobipy as gp
from gurobipy import GRB
import GetData
from Results import writeResults
import ou_file_utils as ofu
model = gp.Model("OptimsationModel")
x, totalCaplist, totalResourcelist,file_path= GetData.getData(ofu.getFile())
# Decision Variables
#need to put capcity of each resource together in a list, and same with resource list
y = {}
z={}
capacityEveryDay={}
for region in range(len(x)):  # Iterate over regions (outer list)
    for patient in range(len(x[region])):
        y[region, patient] = model.addVar(vtype=GRB.BINARY, name=f"Region_{region} Patient_{patient}")
    for Otherregion in range(len(x)) :
        if Otherregion !=region:
            for OtherRegionpatient in range(len(x[Otherregion])):
                z[region,Otherregion,OtherRegionpatient]=model.addVar(vtype=GRB.BINARY, name=f" from {Otherregion} to {region} patientnumber_{OtherRegionpatient}")

# Objective
weightY=1.0001
totalPatients = (gp.quicksum((weightY * var) for var in y.values())) + gp.quicksum(1 * var for var in z.values())
model.setObjective(totalPatients, GRB.MAXIMIZE)
#Constraints
#Constraint 1 : Patient coming from own region and patient taken from other region should be less than the capacity
for r in range(len(x)):  # Iterate over regions
        for res in range( len(totalCaplist[r])):
            for days in range (1,len(totalCaplist[r][res])+1):
                for re in range (0,len(totalCaplist[r])):                  
                    ownregion=gp.quicksum(y[r, m] * x[r][m][days]*totalResourcelist[r][re][m]for m in range(len(x[r])))
                    otherregion=gp.quicksum(gp.quicksum(z[r, r2, m2] * x[r2][m2][days] *totalResourcelist[r2][re][m2]for m2 in range(len(x[r2])) ) for r2 in range(len(x))if r2!=r)
                    model.addConstr(ownregion+otherregion <= totalCaplist[r][res][days-1])

#constraint 2:
#ensuring patient transferred go to one region so we iterating over from region and from patients , and then in the end summinng over to regions
for r2 in range(len(x)):
    for m2 in range(len(x[r2])):
        #for i in range(1, len(capacity[r2])+1):#gettting days
        for res in range( len(totalCaplist[r])):
            for days in range (1,len(totalCaplist[r][res])+1):
                model.addConstr(gp.quicksum(z[r,r2,m2]  for r in range(len(x))if r2!=r) <=1)
for r2 in range(len(x)):
        for m in range(len(x[r2])):
            model.addConstr(gp.quicksum(z[r, r2, m] for r in range(len(x)) if r!=r2)<=1-y[r2,m])

model.update()
model.optimize()

print("\nPatient Assignments:")

for var in y.values():
    print(f"{var.VarName} = {var.x}")
for var in z.values():
    print(f"{var.VarName} = {var.x}")
writeResults(y, z,capacityEveryDay, file_path)