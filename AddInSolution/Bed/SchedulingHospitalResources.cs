using System;
using System.Collections.Generic;
using System.Xml;
using System.Xml.Linq;
using System.Linq;

using SimioAPI;
using SimioAPI.Extensions;

namespace Bed
{
    public class ConfigureSchedulingHospitalResources : IDesignAddIn, IDesignAddInGuiDetails
    {
        #region IDesignAddIn Members

        /// <summary>
        /// Property returning the name of the add-in. This name may contain any characters and is used as the display name for the add-in in the UI.
        /// </summary>
        public string Name
        {
            get { return "SchedulingHospitalResources"; }
        }

        /// <summary>
        /// Property returning a short description of what the add-in does.
        /// </summary>
        public string Description
        {
            get { return "Create and update scheduling hospital resources in Simio"; }
        }

        /// <summary>
        /// Property returning an icon to display for the add-in in the UI.
        /// </summary>
        public System.Drawing.Image Icon
        {
            get { return null; }
        }

        public string CategoryName
        {
            get
            {
                throw new NotImplementedException();
            }
        }

        public string TabName
        {
            get
            {
                throw new NotImplementedException();
            }
        }

        public string GroupName
        {
            get
            {
                throw new NotImplementedException();
            }
        }

        /// <summary>
        /// Method called when the add-in is run.
        /// </summary>
        public void Execute(IDesignContext context)
        {
            // This example code places some new objects from the Standard Library into the active model of the project.
            if (context.ActiveModel != null)
            {

                var table = context.ActiveModel.Tables["Resources"];
                context.ActiveModel.BulkUpdate(model =>
                {
                    foreach (IRow row in table.Rows)
                    {
                        var io = context.ActiveModel.Facility.IntelligentObjects[row.Properties["ResourceName"].Value];
                        io = context.ActiveModel.Facility.IntelligentObjects.CreateObject(row.Properties["ResourceType"].Value, new FacilityLocation(0, 0, 0));


                        // System.Windows.Forms.MessageBox.Show(String.Format("The \"{0}\" object type does not exist in this model.  This object type is referenced on {1} in the Resources table.", row.Properties["ResourceType"].Value, row.Properties["ResourceName"].Value), Name);
                        //return;



                        //   newObject = true;
                        io.ObjectName = row.Properties["ResourceName"].Value;
                    }



                });
            } 

        
        }

        #endregion
    }
}
