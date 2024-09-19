import gurobipy as gp
from gurobipy import GRB
import GetData
#Latest Model as of Sep 19 2021
#Included model reads data from GetDATA File
#Included dynamic demands over different region, earlier demand was fixed for each region
#removed z[r,r,m] cpnstant and added if condition to limit the for loop, this will save space, and ensure patients are transferred to other region and not to own region
model = gp.Model("OptimsationModel")
x, capacity= GetData.getData()
# Decision Variables
y = {}
z={}
for region in range(len(x)):  # Iterate over regions (outer list)
    for patient in range(len(x[region])):
        y[region, patient] = model.addVar(vtype=GRB.BINARY, name=f"Region_{region} Patient_{patient}")
    for Otherregion in range(len(x)) :
        if Otherregion !=region:
            for OtherRegionpatient in range(len(x[Otherregion])):
                z[region,Otherregion,OtherRegionpatient]=model.addVar(vtype=GRB.BINARY, name=f"otherRegionPatientTaken To {region} from_{Otherregion} patientnumber_{OtherRegionpatient}")

# Objective
totalPatients = gp.quicksum(var for var in y.values())+gp.quicksum(var for var in z.values())
model.setObjective(totalPatients, GRB.MAXIMIZE)
#Constraints
for r in range(len(x)):  # Iterate over regions
      # Iterate over patients
        for i in range(1, len(capacity[r]) + 1): #Iterate over days
            model.addConstr(gp.quicksum(y[r, m] * x[r][m][i]for m in range(len(x[r])))
             +gp.quicksum(gp.quicksum(z[r, r2, m2] * x[r2][m2][i] for m2 in range(len(x[r2])) ) for r2 in range(len(x))if r2!=r)
                <= capacity[r][i])

#ensuring patient transferred go to one region
for r in range(len(x)):
    for r2 in range(len(x)):
        for i in range(1, len(capacity[r]) + 1):
            if r2!=r:
                model.addConstr(gp.quicksum(z[r,r2,m2]  for m2 in range(len(x[r2])))<=1)
                model.addConstr(gp.quicksum(z[r, r2, m2] for m2 in range(len(x[r2]))) <= gp.quicksum(
                x[r2][m2][i] for m2 in range(len(x[r2]))))

# for r in range(len(x)):
#     for m in range(len(x[r])):
#          model.addConstr(z[r,r,m]==0)
for r in range(len(x)):
        for m in range(len(x[r])):
            #doing quicksum here because there are days the patient willl come, if i dont do it it will make the days the patient comealso zero
            model.addConstr(y[r, m] <= gp.quicksum(x[r][m][i] for i in range(1,len(capacity[r])+1)))
            model.addConstr(gp.quicksum(z[r2, r, m] for r2 in range(len(x)) if r2!=r)+y[r,m]<=1)

#either you can transfer or accept the same patient you cant transfer the accepted patient
model.update()
model.optimize()

print("\nPatient Assignments:")
for var in y.values():
    print(f"{var.VarName} = {var.x}")
for var in z.values():
    print(f"{var.VarName} = {var.x}")
