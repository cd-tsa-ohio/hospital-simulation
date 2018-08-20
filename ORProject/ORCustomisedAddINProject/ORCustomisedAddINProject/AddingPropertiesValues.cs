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
    class AddingPropertiesValues : IDesignAddIn
    {
        public string Name
        {
            get { return "Adding Properties"; }
        }

        /// <summary>
        /// Property returning a short description of what the add-in does.
        /// </summary>
        public string Description
        {
            get { return "Read Properties From a file and add them"; }
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

                    while (true)
                    {
                        String columnNames = reader.ReadLine();
                        String[] colNameTypeStr = columnNames.Split(',');
                        foreach (String properties in colNameTypeStr)
                        {
                            if (properties == "")
                            {
                                continue;
                            }
                            String[] colNameTypeArr = properties.Split(';');
                            Tuple<String, String, String> objectProperties = new Tuple<String, String, String>(colNameTypeArr[0], colNameTypeArr[1], colNameTypeArr[2]);
                            var objectname = context.ActiveModel.Facility.IntelligentObjects[objectProperties.Item1];
                            objectname.Properties[objectProperties.Item2].Value = objectProperties.Item3;

                        }
                    }
                }
            }
        }
    }
}
