using System;

using System.Collections.Generic;

using System.Linq;

using System.Text;

using SimioAPI;
using System.IO;
using SimioAPI.Extensions;
using System.Collections;
using System.Windows.Forms;

namespace ORCustomisedAddINProject 
{
    class ORTableAddTest : IDesignAddIn, IDesignAddInGuiDetails, IGridDataRecords
    {
         #region IDesignAddIn Members
 


        /// <summary> 

        /// Property returning the name of the add-in. This name may contain any characters and is used as the display name for the add-in in the UI. 

        /// </summary> 

        public string Name
        {
            get { return "OrReadFromFile"; }
        }
        public string Description
        {
            get { return "Description text for the 'UserAddIn' add-in."; }
        }
        /// <summary> 

        /// Property returning an icon to display for the add-in in the UI. 

        /// </summary> 

        public System.Drawing.Image Icon
        {
            get { return null; }

        }

        public enum ColumnProp

        {
            AddObjectReferenceColumn, AddExpressionColumn, AddEntityReferenceColumn
        }
        /// <summary> 

        /// Method called when the add-in is run. 

        /// </summary> 
        /// 
        
        public  List<string> tableList = new List<string>();
        public void Execute(IDesignContext context)

        {
            if (context.ActiveModel != null)// if there are tables in the facility
            {
                string filename = @"C:\Users\Simiotest\Resources.csv";
                // Displays an OpenFileDialog so the user can select a Cursor.  
                OpenFileDialog openFileDialog1 = new OpenFileDialog();
                openFileDialog1.Filter = "CSV Files|*.csv";
                openFileDialog1.Title = "Select a CSV File";

                // Show the Dialog.  
                // If the user clicked OK in the dialog and  
                // a .CUR file was selected, open it.  
                if (openFileDialog1.ShowDialog() == System.Windows.Forms.DialogResult.OK)
                {
                    // Assign the cursor in the Stream to the Form's Cursor property. 
                    filename = openFileDialog1.FileName;


                }
                else
                {
                    
                }
                using (CsvFileReader reader = new CsvFileReader(filename))
                {
                    CsvRow row = new CsvRow();
                   
                    List<string> propertiesList = new List<string>();
                    Dictionary<String, String> columnnames = new Dictionary<string, string>();
                    Dictionary<String, String> propertyname = new Dictionary<string, string>();
                    List<GridDataColumnInfo> colmninfo = new List<GridDataColumnInfo>();
                    List<String> columnData = new List<string>();
                    IGridDataRecord gd;
                    ITableColumn columnns = null;                                  
                    ITable t = null;
                    String[] colNames;
                                     
                   var tableName = context.ActiveModel.PropertyDefinitions.AddStringProperty("TableName", "null");
                    tableName.CategoryName = "OUPrpperties";
                    var ObjectRefColName = context.ActiveModel.PropertyDefinitions.AddStringProperty("ObjectsRefColumnName", "null");
                    ObjectRefColName.CategoryName = "OUPrpperties";
                    var ObjectName = context.ActiveModel.PropertyDefinitions.AddStringProperty("ObjectsColumnName", "null");
                    ObjectName.CategoryName = "OUPrpperties";
                    var XLocation = context.ActiveModel.PropertyDefinitions.AddStringProperty("XLocation", "null");
                    ObjectName.CategoryName = "OUPrpperties";
                    var ZLocation = context.ActiveModel.PropertyDefinitions.AddStringProperty("ZLocation", "null");
                    ObjectName.CategoryName = "OUPrpperties";
                    
                    if (context.ActiveModel.Properties["TableName"].Value == "null")
                    {                                                                                                         
                            while (true)
                            {
                                String tableNames = reader.ReadLine();
                                String[] tableNamesArray = tableNames.Split(',');

                                foreach (String tables in tableNamesArray)
                                {
                                    
                                if (tables=="")
                                {
                                    continue;
                                }
                                tableList.Add((tables));
                                t = context.ActiveModel.Tables.Create(tables);                             
                                } 
                                
                                String columnNames = reader.ReadLine();
                                String[] colNameTypeStr = columnNames.Split(',');
                                foreach (String properties in colNameTypeStr)
                                {   if (properties == "")
                                {
                                    continue;
                                }
                                    String[] colNameTypeArr = properties.Split(';');
                                    columnnames.Add(colNameTypeArr[0], colNameTypeArr[1]);
                                }

                          
                                foreach (var item in columnnames)

                                {   
                                    var columnName = item.Key;
                                    var propertyType = item.Value;
                                   
                                    if(propertyType == "AddActivityReferenceColumn")
                                { 
                                    columnns = t.Columns.AddActivityReferenceColumn(columnName);
                                    
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddBatchLogicReferenceColumn")
                                {
                                    columnns = t.Columns.AddBatchLogicReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                //else if (propertyType == "AddBatchLogicReferenceColumn")
                                //{
                                //    columnns = t.Columns.AddBooleanColumn(columnName);
                                //    columnData.Add(columnName);
                                //}
                                else if (propertyType == "AddChangeoverLogicReferenceColumn")
                                {
                                    columnns = t.Columns.AddChangeoverLogicReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddChangeoverMatrixReferenceColumn")
                                {
                                    columnns = t.Columns.AddChangeoverMatrixReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddColorColumn")
                                {
                                    columnns = t.Columns.AddColorColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddContainerReferenceColumn")
                                {
                                    columnns = t.Columns.AddContainerReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddCostCenterReferenceColumn")
                                {
                                    columnns = t.Columns.AddCostCenterReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddDateTimeColum")
                                {
                                    DateTime date1 = new DateTime(2008, 5, 1, 8, 30, 52);
                                    columnns = t.Columns.AddDateTimeColumn(columnName, (date1));
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddDayPatternReferenceColumn")
                                {
                                    columnns = t.Columns.AddDayPatternReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddEntityReferenceColumn")
                                {
                                    columnns = t.Columns.AddEntityReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddEnumColumn")
                                {
                                    columnns = t.Columns.AddEnumColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddEventReferenceColumn")
                                {
                                    columnns = t.Columns.AddEventReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddExpressionColumn")
                                {
                                    columnns = t.Columns.AddExpressionColumn(columnName,"0.0");
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddFailureReferenceColumn")
                                {
                                    columnns = t.Columns.AddFailureReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddForeignKeyColumn")
                                {
                                    columnns = t.Columns.AddForeignKeyColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddIntegerColumn")
                                {
                                    columnns = t.Columns.AddIntegerColumn(columnName,0);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddListReferenceColumn")
                                {
                                    columnns = t.Columns.AddListReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddMaterialReferenceColumn")
                                {
                                    columnns = t.Columns.AddMaterialReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddMonitorReferenceColumn")
                                {
                                    columnns = t.Columns.AddMonitorReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddNetworkReferenceColumn")
                                {
                                    columnns = t.Columns.AddNetworkReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddNodeListReferenceColumn")
                                {
                                    columnns = t.Columns.AddNodeListReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddNodeReferenceColumn")
                                {
                                    columnns = t.Columns.AddNodeReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddObjectListReferenceColumn")
                                {
                                    columnns = t.Columns.AddObjectListReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddObjectReferenceColumn")
                                {
                                    columnns = t.Columns.AddObjectReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddObjectTypeReferenceColumn")
                                {
                                    columnns = t.Columns.AddObjectTypeReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddOperationReferenceColumn")
                                {
                                    columnns = t.Columns.AddOperationReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddOutputStatisticReferenceColumn")
                                {
                                    columnns = t.Columns.AddOutputStatisticReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddProcessReferenceColumn")
                                {
                                    columnns = t.Columns.AddProcessReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddRateTableReferenceColumn")
                                {
                                    columnns = t.Columns.AddRateTableReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddProcessReferenceColumn")
                                {
                                    columnns = t.Columns.AddProcessReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddRealColumn")
                                {
                                    columnns = t.Columns.AddRealColumn(columnName,0);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddRegulatorReferenceColumn")
                                {
                                    columnns = t.Columns.AddRegulatorReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddRoutingGroupReferenceColumn")
                                {
                                    columnns = t.Columns.AddRoutingGroupReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddScheduleReferenceColumn")
                                {
                                    columnns = t.Columns.AddScheduleReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddSelectionRuleReferenceColumn")
                                {
                                    columnns = t.Columns.AddSelectionRuleReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddSequenceDestinationColumn")
                                {
                                    columnns = t.Columns.AddSequenceDestinationColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddSequenceNumberColumn")
                                {
                                    columnns = t.Columns.AddSequenceNumberColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddStateReferenceColumn")
                                {
                                    columnns = t.Columns.AddStateReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddStateStatisticReferenceColumn")
                                {
                                    columnns = t.Columns.AddStateStatisticReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddStationReferenceColumn")
                                {
                                    columnns = t.Columns.AddStationReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddSteeringBehaviorReferenceColumn")
                                {
                                    columnns = t.Columns.AddSteeringBehaviorReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddStorageReferenceColumn")
                                {
                                    columnns = t.Columns.AddStorageReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddStringColumn")
                                {
                                    columnns = t.Columns.AddStringColumn(columnName,"null");
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddTableReferenceColumn")
                                {
                                    columnns = t.Columns.AddTableReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddTallyStatisticReferenceColumn")
                                {
                                    columnns = t.Columns.AddTallyStatisticReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddTaskDependencyColumn")
                                {
                                    columnns = t.Columns.AddTaskDependencyColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddTaskSequenceReferenceColumn")
                                {
                                    columnns = t.Columns.AddTaskSequenceReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddTimerReferenceColumn")
                                {
                                    columnns = t.Columns.AddTimerReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddTransporterListReferenceColumn")
                                {
                                    columnns = t.Columns.AddTransporterListReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                                else if (propertyType == "AddTransporterReferenceColumn")
                                {
                                    columnns = t.Columns.AddTransporterReferenceColumn(columnName);
                                    columnData.Add(columnName);
                                }
                               
                            }
                            columnnames.Clear();
                           
                        }


                    }                  
                }
            }
        }

        public IEnumerator<IGridDataRecord> GetEnumerator()
        {
            throw new NotImplementedException();
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            throw new NotImplementedException();
        }

        public void Dispose()
        {
            throw new NotImplementedException();
        }















        // Example of how to place some new fixed objects into the active model. 

        // This example code places three new fixed objects: a Source, a Server, and a Sink. 








        #endregion



        public string CategoryName

        {

            get { return "OrModel"; }

        }



        public string GroupName

        {

            get { throw new NotImplementedException(); }

        }



        public string TabName

        {

            get { return "ReadFromFile"; }

        }

        public IEnumerable<GridDataColumnInfo> Columns => throw new NotImplementedException();
    }
    // This example code places some new objects from the Standard Library into 
}
