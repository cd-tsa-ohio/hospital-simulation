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
    class AddingList : IDesignAddIn
    {
        public string Name
        {
            get { return "Adding List"; }
        }

        /// <summary>
        /// Property returning a short description of what the add-in does.
        /// </summary>
        public string Description
        {
            get { return "Read Lists From a file and add them"; }
        }

        /// <summary>
        /// Property returning an icon to display for the add-in in the UI.
        /// </summary>
        public System.Drawing.Image Icon
        {
            get { return null; }
        }
        ORTableAddTest varCall = new ORTableAddTest();
        public void Execute(IDesignContext context)

        {
            if (context.ActiveModel != null)// if there are tables in the facility
            {
                IIntelligentObjects intelligentObjects = context.ActiveModel.Facility.IntelligentObjects;
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
                using (CsvFileReader reader = new CsvFileReader(filename))
                {
                    CsvRow row = new CsvRow();
                    List<string> propertiesList = new List<string>();
                    INamedList newlist = null;
                    Dictionary<String, String> listnamedic = new Dictionary<string, string>();
                    List<String> propertydata = new List<string>();
                    while (true)
                    {
                        String listname = reader.ReadLine();
                        String[] listnameTypeStr = listname.Split(',');
                        foreach (String properties in listnameTypeStr)
                        {
                            if (properties == "")
                            {
                                continue;
                            }
                            String[] listdictionaryType = properties.Split(';');
                            listnamedic.Add(listdictionaryType[0], listdictionaryType[1]);
                        }
                        foreach (var item in listnamedic)

                        {
                            var listNAme = item.Key;
                            var propertyType = item.Value;

                            if (propertyType == "AddNodeList")
                            {
                                newlist = context.ActiveModel.NamedLists.AddNodeList(listNAme);
                               // propertydata.Add(listNAme);
                            }
                            else if (propertyType == "AddObjectList")
                            {
                                newlist = context.ActiveModel.NamedLists.AddObjectList(listNAme);
                                propertydata.Add(listNAme);
                            }
                            else if (propertyType == "AddStringList")
                            {
                                newlist = context.ActiveModel.NamedLists.AddStringList(listNAme);
                               
                            }
                            else if (propertyType == "AddTransporterList")
                            {
                                newlist = context.ActiveModel.NamedLists.AddTransporterList(listNAme);
                              
                            }



                           
                               

                                String data = reader.ReadLine();
                                if (data == null)
                                {
                                    continue;
                                }
                                String[] Rowvalues = data.Split(',');
                                foreach (String properties in Rowvalues)
                                {
                                    if (properties == "")
                                    {
                                        continue;
                                    }
                                   
                                        var rows = newlist.Rows.Create();
                                     propertiesList.Add(properties);
                                        rows.Properties[0].Value = properties;
                                    
                                }
                           

                            }
                        listnamedic.Clear();
                    }
                        }
                    }
                }



            }
        }
    
