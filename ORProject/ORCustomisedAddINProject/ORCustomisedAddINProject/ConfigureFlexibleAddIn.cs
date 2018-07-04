using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using SimioAPI;
using SimioAPI.Extensions;

namespace ORCustomisedAddINProject
{
    class ConfigureFlexibleAddIn : IDesignAddIn
    {
        #region IDesignAddIn Members

        /// <summary>
        /// Property returning the name of the add-in. This name may contain any characters and is used as the display name for the add-in in the UI.
        /// </summary>
        public string Name
        {
            get { return "ConfigureORData"; }
        }

        /// <summary>
        /// Property returning a short description of what the add-in does.
        /// </summary>
        public string Description
        {
            get { return "Reads data from file and create objects"; }
        }

        /// <summary>
        /// Property returning an icon to display for the add-in in the UI.
        /// </summary>
        public System.Drawing.Image Icon
        {
            get { return null; }
        }
        ORTableAddTest varCall = new ORTableAddTest();
        /// <summary>
        /// Method called when the add-in is run.
        /// </summary>
        public void Execute(IDesignContext context)
        {
            // This example code places some new objects from the Standard Library into the active model of the project.
            var TableNameUserSpecified = context.ActiveModel.Properties["TableName"].Value;
            var columnNameObject = context.ActiveModel.Properties["ObjectsRefColumnName"].Value;
            var columnObjectCreator = context.ActiveModel.Properties["ObjectsColumnName"].Value;
            var Xlocation = context.ActiveModel.Properties["XLocation"].Value;
            var ZLocation = context.ActiveModel.Properties["ZLocation"].Value;

            if (context.ActiveModel != null)
            {
                if (context.ActiveModel.Properties["TableName"].Value != "null")
                {   
                    IIntelligentObjects intelligentObjects = context.ActiveModel.Facility.IntelligentObjects;
                    
                     
                    var table = context.ActiveModel.Tables[TableNameUserSpecified];
                            context.ActiveModel.BulkUpdate(model =>
                            {
                                foreach (IRow row in table.Rows)
                                {
                                    //var Xlocationdefn = context.ActiveModel.Facility.IntelligentObjects[row.Properties[Xlocation].Value];
                                    //(Xlocationdefn as IUnitizedTableColumn).UnitType = SimioUnitType.Length;
                                    //var Zlocationdefn = context.ActiveModel.Facility.IntelligentObjects[row.Properties[ZLocation].Value];
                                    //(Zlocationdefn as IUnitizedTableColumn).UnitType = SimioUnitType.Length;
                                    var io = context.ActiveModel.Facility.IntelligentObjects[row.Properties[columnNameObject].Value];
                                
                                    io = context.ActiveModel.Facility.IntelligentObjects.CreateObject(row.Properties[columnObjectCreator].Value, new FacilityLocation(Double.Parse(row.Properties[Xlocation].Value),0, Double.Parse(row.Properties[ZLocation].Value)));
                                    io.ObjectName = row.Properties[columnNameObject].Value;
                                }

                            });                                        
                }

                #endregion
            }
        }
    }
}
