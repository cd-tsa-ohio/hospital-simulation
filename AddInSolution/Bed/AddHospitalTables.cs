using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using SimioAPI;
using SimioAPI.Extensions;

namespace Bed
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
                return ("table name");
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
            var warnings = new List<String>();
            // This example code places some new objects from the Standard Library into the active model of the project.
            if (context.ActiveModel != null)
            {
              
                IIntelligentObjects intelligentObjects = context.ActiveModel.Facility.IntelligentObjects;
                IFixedObject bed = intelligentObjects.CreateObject("Bed", new FacilityLocation(0, 0, 0)) as IFixedObject;
                IFixedObject sourceObject = intelligentObjects.CreateObject("Source", new FacilityLocation(-10, 0, -10)) as IFixedObject;
                IFixedObject serverObject = intelligentObjects.CreateObject("Server", new FacilityLocation(0, 0, 0)) as IFixedObject;
                IFixedObject sinkObject = intelligentObjects.CreateObject("Sink", new FacilityLocation(10, 0, 10)) as IFixedObject;
                IEntityInstanceReferencePropertyDefinition modelentitiy = intelligentObjects.CreateObject("Patient", new FacilityLocation(-20, 0, -10))
                                                                             as IEntityInstanceReferencePropertyDefinition;
                ITransporterInstanceReferencePropertyDefinition regnurse = intelligentObjects.CreateObject("Worker", new FacilityLocation(-30, 0, -30))
                                                                            as ITransporterInstanceReferencePropertyDefinition;
                //var RNurseName = context.ActiveModel.Facility.IntelligentObjects["Worker1"];
                //RNurseName.Properties["Name"].Value = "RegularNurse";
                //var modelentitiy = context.ActiveModel.Facility.IntelligentObjects["Patient"];
                // Example of how to place some new link objects into the active model (to add network paths between nodes).
                // This example code places two new link objects: a Path connecting the Source 'output' node to the Server 'input' node,
                // and a Path connecting the Server 'output' node to the Sink 'input' node.
                INodeObject sourceOutputNode = sourceObject.Nodes[0];
                INodeObject serverInputNode = serverObject.Nodes[0];
                INodeObject serverOutputNode = serverObject.Nodes[1];
                INodeObject sinkInputNode = sinkObject.Nodes[0];
                INodeObject bedinputnode = bed.Nodes[0];
                INodeObject bedoutputnode = bed.Nodes[1];
                ILinkObject pathObject1 = intelligentObjects.CreateLink("Path", sourceOutputNode, serverInputNode, null) as ILinkObject;
                ILinkObject pathObject2 = intelligentObjects.CreateLink("Path", serverOutputNode, bedinputnode, null) as ILinkObject;
                ILinkObject pathObject3 = intelligentObjects.CreateLink("Path", bedoutputnode, sinkInputNode, null) as ILinkObject;
                
                // Example of how to edit the property of an object.
                // This example code edits the 'ProcessingTime' property of the added Server object.
                serverObject.Properties["ProcessingTime"].Value = "0";
                sourceObject.Properties["EntityType"].Value = "Patient1";
                var defaultEntity = context.ActiveModel.Facility.IntelligentObjects["Patient1"];

                defaultEntity.Properties["RegNurseCheckTime"].Value = "HospitalData.RNRounding";
                defaultEntity.Properties["TherapistCheckTime"].Value = "HospitalData.TherapistRounding";
                defaultEntity.Properties["AssistantNurseCheckTime"].Value = "HospitalData.ANRounding";
                defaultEntity.Properties["BedStayTime"].Value = "HospitalData.BedStayTime";
                defaultEntity.Properties["TherapistVisitTime"].Value = "HospitalData.TherapistVisitTime";
                defaultEntity.Properties["RegNurseVisitTime"].Value = "HospitalData.RegNurseVisitTime";
                defaultEntity.Properties["AssistantNurseNextVisit"].Value = "HospitalData.AssistantNurseNextVisit";
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
                //Add Resource Type List

                var resourceType = context.ActiveModel.NamedLists["ResourceType"];
                if (resourceType != null)
                {
                    warnings.Add(string.Format(FormatListMessage(resourceType.Name)));

                }
                else
                {
                    resourceType = context.ActiveModel.NamedLists.AddObjectList("ResourceType");
                    var firstRow = resourceType.Rows.Create();
                    firstRow.Properties[0].Value = "Source";
                    var secondRow = resourceType.Rows.Create();
                    secondRow.Properties[0].Value = "Server";
                    var thirdRow = resourceType.Rows.Create();
                    thirdRow.Properties[0].Value = "Patient";
                    var fourthRow = resourceType.Rows.Create();
                    fourthRow.Properties[0].Value = "RegNurse";
                    var fifthRow = resourceType.Rows.Create();
                    fifthRow.Properties[0].Value = "Therapist";
                    var sixthrow = resourceType.Rows.Create();
                    sixthrow.Properties[0].Value = "Ass.Nurse";
                    var seventhRow = resourceType.Rows.Create();
                    seventhRow.Properties[0].Value = "Sink";
                    var eigthRow = resourceType.Rows.Create();
                    eigthRow.Properties[0].Value = "Bed";
                    var ninthRow = resourceType.Rows.Create();
                    ninthRow.Properties[0].Value = "Worker";


                }
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
                    var resourceTypes = resourceTable.Columns.AddListReferenceColumn("ResourceType");
                    resourceTypes.ListName = ("ResourceType");
                    resourceTypes.DefaultString = ("Bed"); 
                }
                ITable link = context.ActiveModel.Tables["LinkBetweenResources"];
                if (link != null)
                {
                    warnings.Add(string.Format(FormatLinkMessage(link.Name)));

                }
                else
                {

                }
                //var modelentity1 = context.ActiveModel.Facility.IntelligentObjects["Patient"];

                //modelentity1.Properties["BedStayTime"].Value = "5";


            }

        #endregion
        }
     string FormatListMessage(string listname)
        {
           return string. Format ("The \"{0}\"   list could not be added because it already exists.", listname);
        }
        string FormatLinkMessage(string likname)
        {
            return string.Format("The \"{0}\"   list could not be added because it already exists.", likname);
        }
    }

}
