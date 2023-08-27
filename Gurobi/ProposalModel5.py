import gurobipy as gp
import openpyxl
from gurobipy import GRB
import pandas as pa


workbook2 = openpyxl.load_workbook("DataFile3.xlsx")
sheets=["Region1","Region2","Region3"]
def getData():
    capacity=[]
    PatientData=[]
    resreq =[]
    for sheet in sheets:
        sheet_read= workbook2[sheet]
        capacity_range = sheet_read["B13:G13"]
        for cell in capacity_range:
            capacity.append([i.value for i in cell])
        x_range = sheet_read["B3:G12"]
        PatientData.append( [[cell.value if cell.value is not None else 0 for cell in row] for row in x_range])
        resreq.append([[cell.value if cell.value is not None else 0 for cell in row] for row in sheet_read["M3:N22"]])
    #  Ndays = len(capacity[0])
    #capacity = [capacity[i:i + Ndays] for i in range(0, len(capacity), Ndays)]
    print("X")
    print(PatientData)
    print("R`equested Resources")
    print(resreq)
    print("Capacity")
    print(capacity)
    return capacity, PatientData,resreq

def periodic_problem(NPatients, Ndays, NResources, Nregions):
    #sets
    model = gp.Model("OptimsationModel")
    patients = range(NPatients)
    days = range(Ndays)
    resources=range(NResources)
    regions=range (Nregions)
    #parameters
    capacity,x, resreq= getData()
    regionsSet= {
        0: [1, 2],
        1: [0,2],
        2: [0,1]
    }

    # Decision Variables
    y=model.addVars(regions,patients,lb=0,vtype=GRB.BINARY,name= "OwnRegionPatientTaken" )

    z=model.addVars(regions,regions,patients,lb=0,vtype=GRB.BINARY,name="OtherRegionPatientTaken" )




    # Objective

    totalPatiens = gp.quicksum(y)+gp.quicksum(z)
    model.setObjective(totalPatiens, GRB.MAXIMIZE)

    # Contranints
    #for every region, patient taken and transffered patient taken should be less than its capacity
    for r in regionsSet:
        for i in days:

            model.addConstr(gp.quicksum(x[r][m][i]*y[r,m] for m in patients)+gp.quicksum(gp.quicksum((x[r2][m][i]*(1-y[r2,m]))*z[r2,r,m] for m in patients) for r2 in regionsSet[r])<=capacity[r][i])

    # transferred patient of region can only go to one region
    for r in regionsSet:
        for i in days:
            for m in patients:
                model.addConstr(gp.quicksum(z[r2,r,m] for r2 in regionsSet[r] )<=1)
        #ownpatients taken should not be less than the transferredpatient taken
    for r in regionsSet:

        model.addConstr(gp.quicksum(y[r,m] for m in patients)>=gp.quicksum( gp.quicksum(z[r2,r,m]for m in patients)for r2 in regionsSet[r] ))


    # print(remaingCap)

    model.update()
    model.optimize()

    print("\nPatient Assignments:")
    for var in y.values():
        print(f"{var.VarName} = {var.x}")
    for var in z.values():
        print(f"{var.VarName} = {var.x}")
periodic_problem(10, 5, 2, 3)

