import gurobipy as gp
import openpyxl
from gurobipy import GRB
from decouple import config
import GetData

#what this model does:
#This is a multiple region model, where patients are shared among regions, if a region does not have sufficient capacity it transfer to other region
#keeping in mind that other regions patients have priorty
#To Do:
#Model with model multiple resources
#this is patient sharing model and is latest until date 10/28/2023
#config solution (DATA_FOLDER) used from
#https://able.bio/rhett/how-to-set-and-get-environment-variables-in-python--274rgt5

#9/7/2024 ,modifying the model to read from GetData file

# DATA_FOLDER = config('GUR_DATA_FOLDER')
# file_name=input("Choose file name")

# workbook2 = openpyxl.load_workbook(DATA_FOLDER+"DataFileTest.xlsx")
# sheets=["Region1","Region2","Region3"]
# def getData():
#     capacity=[]
#     PatientData=[]
#     resreq =[]
#     for sheet in sheets:
#         sheet_read= workbook2[sheet]
#         #make them named ranges
#         #ask user to give file
#
#         capacity_range = sheet_read["B6:G6"]
#         for cell in capacity_range:
#             capacity.append([i.value for i in cell])
#         x_range = sheet_read["B3:G5"]
#         PatientData.append( [[cell.value if cell.value is not None else 0 for cell in row] for row in x_range])
#         resreq.append([[cell.value if cell.value is not None else 0 for cell in row] for row in sheet_read["I3:J6"]])
#     #  Ndays = len(capacity[0])
#     #capacity = [capacity[i:i + Ndays] for i in range(0, len(capacity), Ndays)]
#     print("X")
#     print(PatientData)
#     print("R`equested Resources")
#     print(resreq)
#     print("Capacity")
#     print(capacity)
#     return capacity, PatientData,resreq

def periodic_problem(NPatients, Ndays, NResources, Nregions):
    #sets
    model = gp.Model("OptimsationModel")
    patients = range(NPatients)
    days = range(Ndays)
    resources=range(NResources)
    regions=range (Nregions)
    #parameters
    x,capacity= GetData.getData()
    regionsSet= {
        0: [1, 2],
        1: [0,2],
        2: [0,1]
    }

    # Decision Variables
    y=model.addVars(regions,patients,lb=0,vtype=GRB.BINARY,name= "OwnRegionPatientTaken" )
    z=model.addVars(regions,regions,patients,lb=0,vtype=GRB.BINARY,name="OtherRegionPatientTaken" )
    # Objective
    totalPatients = gp.quicksum(y)+gp.quicksum(z)
    model.setObjective(totalPatients, GRB.MAXIMIZE)

    # Contranints
    for r in regionsSet:
        for i in days:
            model.addConstr(
                gp.quicksum(y[r, m]*x[r][m][i] for m in patients) +
                gp.quicksum(gp.quicksum(z[r, r2, m] *x[r2][m][i] for r2 in regionsSet [r]) for m in patients)
                <= capacity[r][i])
    for r in regionsSet:
        for m in patients:
            model.addConstr(gp.quicksum(z[r2,r,m] for r2 in regionsSet[r] )<=1)
    for r in regionsSet:
        for m in patients:
            model.addConstr(z[r,r,m]==0)
    for r in regionsSet:
        for m in patients:
            model.addConstr(y[r, m] <= gp.quicksum(x[r][m][i] for i in days))
            model.addConstr(gp.quicksum(z[r2, r, m] for r2 in regionsSet[r])+y[r,m]<=1)
            model.addConstr(gp.quicksum(z[r2, r, m] for r2 in regionsSet[r])<=gp.quicksum(x[r][m][i] for i in days))
#either you can transfer or accept the same patient you cant transfer the accepted patient

    # print(remaingCap)

    model.update()
    model.optimize()

    print("\nPatient Assignments:")
    for var in y.values():
        print(f"{var.VarName} = {var.x}")
    for var in z.values():
        print(f"{var.VarName} = {var.x}")


    print(totalPatients)
periodic_problem(3, 6, 2, 3)

