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
#Constraint 1 : Patient coming from own region and patient taken from other region should be less than the capacity
for r in range(len(x)):  # Iterate over regions
      # Iterate over patients,
        for i in range(1, len(capacity[r]) + 1): #Iterate over days
            model.addConstr(gp.quicksum(y[r, m] * x[r][m][i]for m in range(len(x[r])))
             +gp.quicksum(gp.quicksum(z[r, r2, m2] * x[r2][m2][i] for m2 in range(len(x[r2])) ) for r2 in range(len(x))if r2!=r)
                <= capacity[r][i])

#constraint 2:
#ensuring patient transferred go to one region so we iterating over from region and from patients , and then in the end summinng over to regions
for r2 in range(len(x)):
    for m2 in range(len(x[r2])):
        for i in range(1, len(capacity[r2]) + 1):#gettting days
                model.addConstr(gp.quicksum(z[r,r2,m2]  for r in range(len(x))if r2!=r) <=1)
                #model.addConstr(gp.quicksum(z[r, r2, m2] for m2 in range(len(x[r2]))) <= gp.quicksum(
               # x[r2][m2][i] for m2 in range(len(x[r2]))))

# for r in range(len(x)):
#     for m in range(len(x[r])):
#          model.addConstr(z[r,r,m]==0)
#Constraint patient transffered from 1 region to other region or patient accepted should not be more than 1 , ensuring patient can either be transferred or accetpted

for r2 in range(len(x)):
        for m in range(len(x[r2])):
            #doing quicksum here because there are days the patient willl come, if i dont do it it will make the days the patient comealso zero
            #model.addConstr(y[r, m] <= gp.quicksum(x[r][m][i] for i in range(1,len(capacity[r])+1)))
            model.addConstr(gp.quicksum(z[r, r2, m] for r in range(len(x)) if r!=r2)<=1-y[r2,m])

#either you can transfer or accept the same patient you cant transfer the accepted patient
model.update()
model.optimize()

print("\nPatient Assignments:")
for var in y.values():
    print(f"{var.VarName} = {var.x}")
for var in z.values():
    print(f"{var.VarName} = {var.x}")
