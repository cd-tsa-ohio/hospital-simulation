import gurobipy as gp
import openpyxl

from gurobipy import GRB

workbook2 = openpyxl.load_workbook("DataFile2.xlsx")
sheets=["Data1","Data2"]
def getData():
    capacity=[]
    x=[]
    resreq =[]
    for sheet in sheets:
        sheet_read= workbook2[sheet]
        capacity_range = sheet_read["B27:K28"]
        capacity.append([[cell.value for cell in row] for row in capacity_range])
        x_range = sheet_read["B3:K22"]
        x.append(( [[cell.value if cell.value is not None else 0 for cell in row] for row in x_range]))
        resreq.append([[cell.value if cell.value is not None else 0 for cell in row] for row in sheet_read["M3:N22"]])
  #  Ndays = len(capacity[0])
    #capacity = [capacity[i:i + Ndays] for i in range(0, len(capacity), Ndays)]
    print("X")
    print(x)
    print("R`equested Resources")
    print(resreq)
    print("Capacity")
    print(capacity)
    return capacity, x,resreq

def periodic_problem(NPatients, Ndays,NResources, Nregions):
    model = gp.Model("OptimsationModel")
    patients = range(NPatients)
    days = range(Ndays)
    resources=range(NResources)
    regions=range (Nregions)

    capacity,x, resreq= getData()

    # Decision Variables
    y = model.addVars(regions,patients, vtype=GRB.BINARY, name='y')
    actualCap= model.addVars(regions,days,patients,lb=0,name="acceptedPatient")
    acceptedPatient=model.addVars(regions,days,patients,lb=0,name="acceptedPatient")
   # patientTransferred=model.addVars(regions,days,patients,lb=0,name="patientTransffered")
    TransfferredCap=model.addVars(regions,days,resources,lb=0,name="transferredCap")
    AcceptedCap= model.addVars(regions,days,resources,lb=0,name="acceptedCap")

    # Objective
    totalPatiens = y.sum()
    model.setObjective(totalPatiens, GRB.MAXIMIZE)

    # Contranints

    # Contranints
    for r in regions:
        for i in days:
          for j in resources:
              model.addConstr(
                  gp.quicksum(x[r][m][i] * resreq[r][m][j] * y[r, m] for m in patients) <= capacity[r][j][i],
                  f"capacity_{r}_{i}_{j}")



    print ("Patient Transfered")
   # print (patientTransferred)
        # Optimize the model
    model.optimize()
    print('\nObjective Value: %g' % model.objVal)

    # Print the optimal y values (patient assignments)
    print("\nPatient Assignments:")


periodic_problem(20, 10, 2, 2)

