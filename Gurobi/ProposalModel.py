import gurobipy as gp
import openpyxl

from gurobipy import GRB

def periodic_problem(NPatients, Ndays,sheet):
    workbook=openpyxl.load_workbook(sheet)
    data_Sheet=workbook["Data"]
    capacity_range=data_Sheet["B26:K26"]
    capacity = [row.value  for row in capacity_range[0]]
    print(capacity)
    x_range=data_Sheet["B3:K22"]
    x=[[cell.value  if cell.value is not None else 0 for cell in row]for row in x_range]
    print (x)
    model=gp.Model("OptimsationModel")
    patients=range(NPatients)
    days=range(Ndays)

    #Variables
    y= model.addVars(patients,vtype=GRB.BINARY,name='y')
    z=model.addVars(patients,days,vtype=GRB.INTEGER,lb=0,name="z")

    #Objective
    totalPatiens=y.sum()
    model.setObjective(totalPatiens,GRB.MAXIMIZE)

    #Contranints
    for i in days:
        model.addConstr(gp.quicksum(z[m,i] for m in patients)<= capacity[i],f"capcity_{i}")
    for i in days:
        for m in patients:
            model.addConstr(z[m,i]==x[m][i]*y[m], f"z_{m}_{i}")
    model.optimize()

    # Save the workbook


periodic_problem(20,10,"multi-period-resourceV2.xlsx")