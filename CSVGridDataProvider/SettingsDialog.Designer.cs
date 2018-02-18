namespace CSVGridDataProvider
{
    partial class SettingsDialog
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.fileNameTextBox = new System.Windows.Forms.TextBox();
            this.cSVGridDataSettingsBindingSource = new System.Windows.Forms.BindingSource(this.components);
            this.fileNameLabel = new System.Windows.Forms.Label();
            this.fileNameBrowseButton = new System.Windows.Forms.Button();
            this.separaterLabel = new System.Windows.Forms.Label();
            this.separatorTextBox = new System.Windows.Forms.TextBox();
            this.checkBox1 = new System.Windows.Forms.CheckBox();
            this.okButton = new System.Windows.Forms.Button();
            this.cancelButton = new System.Windows.Forms.Button();
            this.fileContentsTextBox = new System.Windows.Forms.TextBox();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.importCultureComboBox = new System.Windows.Forms.ComboBox();
            this.label1 = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.cSVGridDataSettingsBindingSource)).BeginInit();
            this.groupBox1.SuspendLayout();
            this.groupBox2.SuspendLayout();
            this.SuspendLayout();
            // 
            // fileNameTextBox
            // 
            this.fileNameTextBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.fileNameTextBox.DataBindings.Add(new System.Windows.Forms.Binding("Text", this.cSVGridDataSettingsBindingSource, "FileName", true, System.Windows.Forms.DataSourceUpdateMode.OnPropertyChanged));
            this.fileNameTextBox.Location = new System.Drawing.Point(75, 6);
            this.fileNameTextBox.Name = "fileNameTextBox";
            this.fileNameTextBox.Size = new System.Drawing.Size(311, 20);
            this.fileNameTextBox.TabIndex = 1;
            this.fileNameTextBox.Validated += new System.EventHandler(this.fileNameTextBox_Validated);
            // 
            // cSVGridDataSettingsBindingSource
            // 
            this.cSVGridDataSettingsBindingSource.DataSource = typeof(CSVGridDataProvider.CSVGridDataSettings);
            // 
            // fileNameLabel
            // 
            this.fileNameLabel.AutoSize = true;
            this.fileNameLabel.Location = new System.Drawing.Point(12, 9);
            this.fileNameLabel.Name = "fileNameLabel";
            this.fileNameLabel.Size = new System.Drawing.Size(57, 13);
            this.fileNameLabel.TabIndex = 0;
            this.fileNameLabel.Text = "&File Name:";
            // 
            // fileNameBrowseButton
            // 
            this.fileNameBrowseButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.fileNameBrowseButton.Location = new System.Drawing.Point(392, 4);
            this.fileNameBrowseButton.Name = "fileNameBrowseButton";
            this.fileNameBrowseButton.Size = new System.Drawing.Size(30, 23);
            this.fileNameBrowseButton.TabIndex = 2;
            this.fileNameBrowseButton.Text = "...";
            this.fileNameBrowseButton.UseVisualStyleBackColor = true;
            this.fileNameBrowseButton.Click += new System.EventHandler(this.fileNameBrowseButton_Click);
            // 
            // separaterLabel
            // 
            this.separaterLabel.AutoSize = true;
            this.separaterLabel.Location = new System.Drawing.Point(6, 23);
            this.separaterLabel.Name = "separaterLabel";
            this.separaterLabel.Size = new System.Drawing.Size(105, 13);
            this.separaterLabel.TabIndex = 3;
            this.separaterLabel.Text = "&Separator Character:";
            // 
            // separatorTextBox
            // 
            this.separatorTextBox.DataBindings.Add(new System.Windows.Forms.Binding("Text", this.cSVGridDataSettingsBindingSource, "Separator", true));
            this.separatorTextBox.Font = new System.Drawing.Font("Courier New", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.separatorTextBox.Location = new System.Drawing.Point(117, 20);
            this.separatorTextBox.MaxLength = 1;
            this.separatorTextBox.Name = "separatorTextBox";
            this.separatorTextBox.Size = new System.Drawing.Size(44, 20);
            this.separatorTextBox.TabIndex = 4;
            this.separatorTextBox.Validating += new System.ComponentModel.CancelEventHandler(this.separatorTextBox_Validating);
            // 
            // checkBox1
            // 
            this.checkBox1.AutoSize = true;
            this.checkBox1.CheckAlign = System.Drawing.ContentAlignment.MiddleRight;
            this.checkBox1.DataBindings.Add(new System.Windows.Forms.Binding("Checked", this.cSVGridDataSettingsBindingSource, "UseHeaders", true));
            this.checkBox1.Location = new System.Drawing.Point(188, 22);
            this.checkBox1.Name = "checkBox1";
            this.checkBox1.Size = new System.Drawing.Size(193, 17);
            this.checkBox1.TabIndex = 5;
            this.checkBox1.Text = "First line contains column &headings:";
            this.checkBox1.UseVisualStyleBackColor = true;
            // 
            // okButton
            // 
            this.okButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.okButton.DialogResult = System.Windows.Forms.DialogResult.OK;
            this.okButton.Location = new System.Drawing.Point(266, 287);
            this.okButton.Name = "okButton";
            this.okButton.Size = new System.Drawing.Size(75, 23);
            this.okButton.TabIndex = 4;
            this.okButton.Text = "&OK";
            this.okButton.UseVisualStyleBackColor = true;
            // 
            // cancelButton
            // 
            this.cancelButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.cancelButton.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this.cancelButton.Location = new System.Drawing.Point(347, 287);
            this.cancelButton.Name = "cancelButton";
            this.cancelButton.Size = new System.Drawing.Size(75, 23);
            this.cancelButton.TabIndex = 5;
            this.cancelButton.Text = "&Cancel";
            this.cancelButton.UseVisualStyleBackColor = true;
            // 
            // fileContentsTextBox
            // 
            this.fileContentsTextBox.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.fileContentsTextBox.Font = new System.Drawing.Font("Courier New", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.fileContentsTextBox.Location = new System.Drawing.Point(6, 19);
            this.fileContentsTextBox.Multiline = true;
            this.fileContentsTextBox.Name = "fileContentsTextBox";
            this.fileContentsTextBox.ReadOnly = true;
            this.fileContentsTextBox.ScrollBars = System.Windows.Forms.ScrollBars.Both;
            this.fileContentsTextBox.Size = new System.Drawing.Size(395, 135);
            this.fileContentsTextBox.TabIndex = 8;
            this.fileContentsTextBox.WordWrap = false;
            // 
            // groupBox1
            // 
            this.groupBox1.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.groupBox1.Controls.Add(this.fileContentsTextBox);
            this.groupBox1.Location = new System.Drawing.Point(15, 121);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(407, 160);
            this.groupBox1.TabIndex = 9;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "File preview";
            // 
            // groupBox2
            // 
            this.groupBox2.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.groupBox2.Controls.Add(this.importCultureComboBox);
            this.groupBox2.Controls.Add(this.label1);
            this.groupBox2.Controls.Add(this.checkBox1);
            this.groupBox2.Controls.Add(this.separaterLabel);
            this.groupBox2.Controls.Add(this.separatorTextBox);
            this.groupBox2.Location = new System.Drawing.Point(15, 33);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(407, 82);
            this.groupBox2.TabIndex = 3;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "Options";
            // 
            // importCultureComboBox
            // 
            this.importCultureComboBox.DataBindings.Add(new System.Windows.Forms.Binding("Text", this.cSVGridDataSettingsBindingSource, "ImportCulture", true));
            this.importCultureComboBox.FormattingEnabled = true;
            this.importCultureComboBox.Location = new System.Drawing.Point(117, 46);
            this.importCultureComboBox.Name = "importCultureComboBox";
            this.importCultureComboBox.Size = new System.Drawing.Size(122, 21);
            this.importCultureComboBox.TabIndex = 7;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(37, 49);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(74, 13);
            this.label1.TabIndex = 6;
            this.label1.Text = "&Import culture:";
            // 
            // SettingsDialog
            // 
            this.AcceptButton = this.okButton;
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CancelButton = this.cancelButton;
            this.ClientSize = new System.Drawing.Size(434, 324);
            this.ControlBox = false;
            this.Controls.Add(this.groupBox2);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.cancelButton);
            this.Controls.Add(this.okButton);
            this.Controls.Add(this.fileNameBrowseButton);
            this.Controls.Add(this.fileNameLabel);
            this.Controls.Add(this.fileNameTextBox);
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.MinimumSize = new System.Drawing.Size(450, 300);
            this.Name = "SettingsDialog";
            this.ShowIcon = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "CSV Settings";
            ((System.ComponentModel.ISupportInitialize)(this.cSVGridDataSettingsBindingSource)).EndInit();
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox fileNameTextBox;
        private System.Windows.Forms.Label fileNameLabel;
        private System.Windows.Forms.Button fileNameBrowseButton;
        private System.Windows.Forms.Label separaterLabel;
        private System.Windows.Forms.TextBox separatorTextBox;
        private System.Windows.Forms.CheckBox checkBox1;
        private System.Windows.Forms.Button okButton;
        private System.Windows.Forms.Button cancelButton;
        private System.Windows.Forms.BindingSource cSVGridDataSettingsBindingSource;
        private System.Windows.Forms.TextBox fileContentsTextBox;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.ComboBox importCultureComboBox;
        private System.Windows.Forms.Label label1;
    }
}