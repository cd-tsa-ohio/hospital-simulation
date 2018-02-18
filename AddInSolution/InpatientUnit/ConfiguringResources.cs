using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using SimioAPI;
using SimioAPI.Extensions;

namespace InpatientUnit
{
    public class ConfigureSchedulingHospitalResources : IDesignAddIn, IDesignAddInGuiDetails
    {
        //private readonly INodeObject sourceOutputNode;
        //private readonly INodeObject serverInputNode;
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
                return "Configure Tables";
            }
        }

        public string GroupName
        {
            get
            {
                return "Inpatient Unit";
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
                var table2 = context.ActiveModel.Tables["LinkTable"];
                context.ActiveModel.BulkUpdate(model =>
                {
                    foreach (IRow row in table.Rows)
                    {
                        var io = context.ActiveModel.Facility.IntelligentObjects[row.Properties["ResourceName"].Value];
                     
                        io = context.ActiveModel.Facility.IntelligentObjects.CreateObject(row.Properties["ResourceType"].Value, new FacilityLocation(Double.Parse(row.Properties["XLocation"].Value), 0, Double.Parse(row.Properties["ZLocation"].Value))) ;

                        

                    
                        io.ObjectName = row.Properties["ResourceName"].Value;

                    }
                    foreach (IRow row in table2.Rows)
                    {
                        INodeObject j = context.ActiveModel.Facility.IntelligentObjects[row.Properties["Node1"].Value] as INodeObject;
                        INodeObject j2 = context.ActiveModel.Facility.IntelligentObjects[row.Properties["Node2"].Value] as INodeObject;

                        var j4 = context.ActiveModel.Facility.IntelligentObjects.CreateLink("Path", j, j2, null) as ILinkObject;
                    }

                });
            }


        }

        #endregion
    }
}
