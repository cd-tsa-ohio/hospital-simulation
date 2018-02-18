using System;
using System.Collections.Generic;
using System.Linq;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace CSVGridDataProvider
{
    public partial class SettingsDialog : Form
    {
        public SettingsDialog()
        {
            InitializeComponent();

            var cultures = System.Globalization.CultureInfo.GetCultures(System.Globalization.CultureTypes.SpecificCultures).Select(c => c.Name).ToArray();
            foreach (var c in cultures)
                this.importCultureComboBox.Items.Add(c);
        }

        private void fileNameBrowseButton_Click(object sender, EventArgs e)
        {
            OpenFileDialog dlg = new OpenFileDialog();
            dlg.Filter = "Comma Separated Value Files (*.csv)|*.csv";
            dlg.Title = "Select File";

            if (dlg.ShowDialog() == DialogResult.OK)
            {
                fileNameTextBox.Text = dlg.FileName;
                UpdateFileContents();
            }
        }

        internal void SetSettings(CSVGridDataSettings settings)
        {
            cSVGridDataSettingsBindingSource.DataSource = settings;
        }

        private void separatorTextBox_Validating(object sender, CancelEventArgs e)
        {
            if (separatorTextBox.Text.Length == 0)
                e.Cancel = true;
        }

        private void fileNameTextBox_Validated(object sender, EventArgs e)
        {
            UpdateFileContents();
        }

        void UpdateFileContents()
        {
            List<string> lines = new List<string>();

            if (System.IO.File.Exists(fileNameTextBox.Text))
            {
                try
                {
                    using (System.IO.FileStream fstream = System.IO.File.OpenRead(fileNameTextBox.Text))
                    {
                        if (fstream != null)
                        {
                            using (System.IO.StreamReader reader = new System.IO.StreamReader(fstream))
                            {
                                string line = null;
                                for (int i = 0; i < 10 && (line = reader.ReadLine()) != null; i++)
                                {
                                    lines.Add(line);
                                }
                            }
                        }
                    }
                }
                catch (System.IO.IOException e)
                {
                    lines.Add(String.Format("[Cannot open file, reason: {0}]", e.Message));
                }
            }

            fileContentsTextBox.Lines = lines.ToArray();
        }
    }
}
