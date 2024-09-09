from decouple import config
import pandas as pd
def getData():
    DATA_FOLDER = config('GUR_DATA_FOLDER')
    FileName = input("write file name: ")
    sheets=[]
    capacitylist=[]
    xlist = []
    sheetname = input("write sheet name: ")
    while sheetname!="Done":
        sheets.append(sheetname)
        sheetname = input("write another sheet name: ")

    for sheet in sheets:
        df1 = pd.read_excel(DATA_FOLDER + FileName,sheet_name=sheet)
        x=[]
        capacityrow = df1[df1['patient'] == 'capacity'].index[0]
    # input number of days
        numDays = int(input("Enter number of days"))
        capacity = df1.iloc[capacityrow, 0:numDays+1]
        capacitylist.append(capacity)
        print(capacity)
        blank_line_index = df1[df1['patient'].isna()].index[0]
        for values in range(1, blank_line_index + 1):
            patientDataRow = df1[df1['patient'] == values].index[0]
            patientData = df1.iloc[patientDataRow, 0:numDays + 1]
            x.append(patientData)
        xlist.append(x)
        print(xlist)
    if len(sheets)==1:
        return xlist[0],capacitylist[0]
    else:
        return xlist,capacitylist
