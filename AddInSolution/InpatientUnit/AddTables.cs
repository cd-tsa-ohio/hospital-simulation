using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using SimioAPI;
using SimioAPI.Extensions;

namespace InpatientUnit
{
    class AddHospitalTables : IDesignAddIn, IDesignAddInGuiDetails
    {
        #region IDesignAddIn Members

        /// <summary>
        /// Property returning the name of the add-in. This name may contain any characters and is used as the display name for the add-in in the UI.
        /// </summary>
        public string Name
        {
            get { return "This is Hospital Resource Scheduling"; }
        }

        /// <summary>
        /// Property returning a short description of what the add-in does.
        /// </summary>
        public string Description
        {
            get { return "This is resource allocation and scheduling model for hospitals"; }
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
                return ("Table name");
            }
        }

        public string TabName
        {
            get
            {
                return ("Add tables");
            }
        }

        public string GroupName
        {
            get
            {
                return ("Inpatient Unit");
            }
        }


        /// <summary>
        /// Method called when the add-in is run.
        /// </summary>
        public void Execute(IDesignContext context)
        {
            var warnings = new List<String>();
            // This example code places some new objects from the Standard Library into the active model of the project.
            if (context.ActiveModel != null)
            {

                IIntelligentObjects intelligentObjects = context.ActiveModel.Facility.IntelligentObjects;

               

                ITable entitydatatable = context.ActiveModel.Tables.Create("HospitalData");
                var i = entitydatatable.Columns.AddEntityReferenceColumn("PatientTypes");
                var mix = entitydatatable.Columns.AddRealColumn("Mix", 0.0);
                var admisiontime = entitydatatable.Columns.AddExpressionColumn("AdmissionTime", "0.0");
                (admisiontime as IUnitizedTableColumn).UnitType = SimioUnitType.Time;
                var rnrounding = entitydatatable.Columns.AddExpressionColumn("RNRounding", "0.0");
                var nurserounding = entitydatatable.Columns.AddExpressionColumn("ANRounding", "0.0");
                var therapistrounding = entitydatatable.Columns.AddExpressionColumn("TherapistRounding", "0.0");
                var bedtime = entitydatatable.Columns.AddExpressionColumn("BedStayTime", "0.0");
                var RVisitTime = entitydatatable.Columns.AddExpressionColumn("RegNurseVisitTime", "0.0");
                var TVisitTime = entitydatatable.Columns.AddExpressionColumn("TherapistVisitTime", "0.0");
                var AVisitTime = entitydatatable.Columns.AddExpressionColumn("AssistantNurseNextVisit", "0.0");


                //var resourceType = context.ActiveModel.NamedLists["ResourceType"];
                //if (resourceType != null)
                //{
                //    warnings.Add(string.Format(FormatListMessage(resourceType.Name)));

                //}
                //else
                //{
                //    resourceType = context.ActiveModel.NamedLists.AddObjectList("ResourceType");

                //    var firstRow = resourceType.Rows.Create();
                //    firstRow.Properties[0].Value = "Source";
                //    var secondRow = resourceType.Rows.Create();
                //    secondRow.Properties[0].Value = "Server";
                //    var thirdRow = resourceType.Rows.Create();
                //    thirdRow.Properties[0].Value = "Patient";
                //    var fourthRow = resourceType.Rows.Create();
                //    fourthRow.Properties[0].Value = "RegNurse";
                //    var fifthRow = resourceType.Rows.Create();
                //    fifthRow.Properties[0].Value = "Therapist";
                //    var sixthrow = resourceType.Rows.Create();
                //    sixthrow.Properties[0].Value = "Ass.Nurse";
                //    var seventhRow = resourceType.Rows.Create();
                //    seventhRow.Properties[0].Value = "Sink";
                //    var eigthRow = resourceType.Rows.Create();
                //    eigthRow.Properties[0].Value = "Bed";
                //    var ninthRow = resourceType.Rows.Create();
                //    ninthRow.Properties[0].Value = "Worker";


                //}
               
                //Adding Resource Table
                ITable resourceTable = context.ActiveModel.Tables["Resources"];

                if (resourceTable != null)
                {
                    warnings.Add(string.Format(FormatListMessage(resourceTable.Name)));

                }

                else
                {

                    resourceTable = context.ActiveModel.Tables.Create("Resources");
                    var j = resourceTable.Columns.AddObjectReferenceColumn("ResourceName");
                    j.FilterToResources = false;
                    j.IsKey = true;

                  //  j.DefaultValueInstantiation = DefaultValueInstantiation.AutoCreateInstance;
                    j.AutoCreatedOffsetX = "XLocation";
                    j.AutoCreatedOffsetY = "0.0";
                    j.AutoCreatedOffsetZ = "ZLocation";
                    var resourceTypes = resourceTable.Columns.AddObjectTypeReferenceColumn  ("ResourceType");
                    
                   // resourceTypes. = ("ResourceType");
                    resourceTypes.DefaultString = ("Bed");
                    var xl = resourceTable.Columns.AddRealColumn("XLocation", 0.0);
                    (xl as IUnitizedTableColumn).UnitType = SimioUnitType.Length;
                    var zl = resourceTable.Columns.AddRealColumn("ZLocation", 0.0);
                    (zl as IUnitizedTableColumn).UnitType = SimioUnitType.Length;
                }

                ITable linkTable = context.ActiveModel.Tables["LinkTable"];
                if (linkTable != null)
                {
                    warnings.Add(string.Format(FormatListMessage(resourceTable.Name)));

                }

                else
                {

                    linkTable = context.ActiveModel.Tables.Create("LinkTable");
                    var nodeLists1 = linkTable.Columns.AddNodeReferenceColumn("Node1");
                    var nodeLists2 = linkTable.Columns.AddNodeReferenceColumn("Node2");
                   


                }
            }
           

            #endregion
        }
        string FormatListMessage(string listname)
        {
            return string.Format("The \"{0}\"   list could not be added because it already exists.", listname);
        }
        string FormatLinkMessage(string likname)
        {
            return string.Format("The \"{0}\"   list could not be added because it already exists.", likname);
        }
    }

}
