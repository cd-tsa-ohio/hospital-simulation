using System;
using System.Collections.Generic;
using System.Text;
using SimioAPI.Extensions;
using LumenWorks.Framework.IO.Csv;

namespace CSVGridDataProvider
{
    public class CSVGridDataProviderImpl 
    {
        #region IGridDataProvider Members

        public string Name
        {
            get { return "CSV File"; }
        }

        public string Description
        {
            get { return "Reads data from a CSV file"; }
        }

        
        public Guid UniqueID
        {
            get { return MY_ID; }
        }
        static readonly Guid MY_ID = new Guid("974CEBFD-B200-4500-9303-CD49A7C9701B");

        public byte[] GetDataSettings(byte[] existingSettings)
        {
            CSVGridDataSettings thesettings = CSVGridDataSettings.FromBytes(existingSettings);
            if (thesettings == null)
                thesettings = new CSVGridDataSettings();

            SettingsDialog dlg = new SettingsDialog();
            dlg.SetSettings(thesettings);

            if (dlg.ShowDialog() == System.Windows.Forms.DialogResult.OK)
                return thesettings.ToBytes();

            return existingSettings;
        }

        public IGridDataRecords OpenData(byte[] dataSettings, IGridDataOpenContext openContext)
        {
            CSVGridDataSettings thesettings = CSVGridDataSettings.FromBytes(dataSettings);
            if (thesettings == null || thesettings.FileName == null) // and maybe check that file exists and can be opened, etc?
                return null;

            thesettings.TryFindFile(openContext);
            return new CSVGridDataRecords(thesettings);
        }

        public string GetDataSummary(byte[] dataSettings)
        {
            CSVGridDataSettings thesettings = CSVGridDataSettings.FromBytes(dataSettings);
            if (thesettings == null || thesettings.FileName == null) // and maybe check that file exists and can be opened, etc?
                return null;

            if (String.IsNullOrWhiteSpace(thesettings.ImportCulture) == false)
                return String.Format("Bound to CSV: {0}, Use headers = {1}, Separator = '{2}', Culture='{3}'", thesettings.FileName, thesettings.UseHeaders, thesettings.Separator, thesettings.ImportCulture);

            return String.Format("Bound to CSV: {0}, Use headers = {1}, Separator = '{2}'", thesettings.FileName, thesettings.UseHeaders, thesettings.Separator);
        }

        #endregion
    }

    [Serializable]
    public class CSVGridDataSettings
    {
        string _fileName;
        public string FileName
        {
            get { return _fileName; }
            set { _fileName = value; }
        }

        public void TryFindFile(IGridDataOpenContext openContext)
        {
            if (System.IO.File.Exists(_fileName) == false)
            {
                if (String.IsNullOrEmpty(openContext.ProjectFileName) == false)
                {
                    var newFile = System.IO.Path.Combine(System.IO.Path.GetDirectoryName(openContext.ProjectFileName), System.IO.Path.GetFileName(_fileName));
                    if (System.IO.File.Exists(newFile))
                    {
                        _fileName = newFile;
                    }
                }
            }
        }

        bool _useHeaders = true;
        public bool UseHeaders
        {
            get { return _useHeaders; }
            set { _useHeaders = value; }
        }

        public const char DEFAULT_SEPARATOR = ',';
        char _separator = DEFAULT_SEPARATOR;
        public char Separator
        {
            get { return _separator; }
            set { _separator = value; }
        }

        [System.Runtime.Serialization.OptionalField]
        public string _importCulture;
        public string ImportCulture
        {
            get { return _importCulture; }
            set
            {
                _importCulture = value;
                _importCultureInfo = null;
            }
        }

        [NonSerialized]
        System.Globalization.CultureInfo _importCultureInfo;
        public System.Globalization.CultureInfo ImportCultureInfo
        {
            get
            {
                if (_importCultureInfo == null && String.IsNullOrWhiteSpace(_importCulture) == false)
                {
                    try
                    {
                        _importCultureInfo = System.Globalization.CultureInfo.GetCultureInfo(_importCulture);
                    }
                    catch
                    {
                        _importCultureInfo = null;
                    }
                }

                return _importCultureInfo;
            }
        }

        public static CSVGridDataSettings FromBytes(byte[] settings)
        {
            if (settings == null)
                return null;

            System.IO.MemoryStream memstream = new System.IO.MemoryStream(settings);
            System.Runtime.Serialization.Formatters.Binary.BinaryFormatter fmt = new System.Runtime.Serialization.Formatters.Binary.BinaryFormatter();

            CSVGridDataSettings csvsettings = (CSVGridDataSettings)fmt.Deserialize(memstream);

            return csvsettings;
        }
        public byte[] ToBytes()
        {
            System.IO.MemoryStream memstream = new System.IO.MemoryStream();
            System.Runtime.Serialization.Formatters.Binary.BinaryFormatter fmt = new System.Runtime.Serialization.Formatters.Binary.BinaryFormatter();

            fmt.Serialize(memstream, this);

            return memstream.ToArray();
        }
    }

    class CSVGridDataRecords : IGridDataRecords
    {
        CSVGridDataSettings _settings;
        public CSVGridDataRecords(CSVGridDataSettings settings)
        {
            _settings = settings;
        }

        #region IGridDataRecords Members

        List<GridDataColumnInfo> _columnInfo;
        List<GridDataColumnInfo> ColumnInfo
        {
            get
            {
                if (_columnInfo == null)
                {
                    if (System.IO.File.Exists(_settings.FileName) == false)
                        return null;

                    _columnInfo = new List<GridDataColumnInfo>();

                    try
                    {
                        using (System.IO.FileStream fstream = System.IO.File.OpenRead(_settings.FileName))
                        {
                            using (System.IO.StreamReader streamreader = new System.IO.StreamReader(fstream))
                            {
                                using (CsvReader reader = new CsvReader(streamreader, false, _settings.Separator))
                                {
                                    reader.DefaultParseErrorAction = ParseErrorAction.AdvanceToNextLine;
                                    reader.MissingFieldAction = MissingFieldAction.ReplaceByEmpty;
                                    reader.SkipEmptyLines = true;

                                    int fieldCount = reader.FieldCount;

                                    // Read in or create names for all columns
                                    if (_settings.UseHeaders)
                                    {
                                        if (reader.ReadNextRecord())
                                        {
                                            for (int i = 0; i < fieldCount; i++)
                                                _columnInfo.Add(new GridDataColumnInfo { Name = reader[i], Type = typeof(string) });
                                        }
                                    }
                                    else
                                    {
                                        for (int i = 0; i < fieldCount; i++)
                                            _columnInfo.Add(new GridDataColumnInfo { Name = String.Format("Col{0}", i), Type = typeof(string) });
                                    }

                                    // Read in types from first data row
                                    if (reader.ReadNextRecord())
                                    {
                                        for (int i = 0; i < fieldCount; i++)
                                        {
                                            string value = reader[i];

                                            double dValue;
                                            DateTime dtValue;

                                            if (Double.TryParse(value, out dValue))
                                                _columnInfo[i] = new GridDataColumnInfo { Name = _columnInfo[i].Name, Type = typeof(double) };
                                            else if (DateTime.TryParse(value, System.Globalization.CultureInfo.InvariantCulture, System.Globalization.DateTimeStyles.None, out dtValue))
                                                _columnInfo[i] = new GridDataColumnInfo { Name = _columnInfo[i].Name, Type = typeof(DateTime) };
                                        }
                                    }
                                }
                            }
                        }
                    }
                    catch (System.IO.IOException)
                    {
                        return null;
                    }
                }

                return _columnInfo;
            }
        }

        public IEnumerable<GridDataColumnInfo> Columns
        {
            get { return ColumnInfo; }
        }

        #endregion

        #region IEnumerable<IGridDataRecord> Members

        class CSVEnumerator : IEnumerator<IGridDataRecord>
        {
            System.IO.FileStream _fstream;
            System.IO.StreamReader _streamReader;
            CsvReader _reader;
            System.Globalization.CultureInfo _importCultureInfo;

            public CSVEnumerator(string fileName, bool hasHeaders, char separator, System.Globalization.CultureInfo cultureInfo)
            {
                try
                {
                    _importCultureInfo = cultureInfo;
                    _fstream = System.IO.File.OpenRead(fileName);
                    _streamReader = new System.IO.StreamReader(_fstream);
                    _reader = new CsvReader(_streamReader, hasHeaders, separator);

                    _reader.DefaultParseErrorAction = ParseErrorAction.AdvanceToNextLine;
                    _reader.MissingFieldAction = MissingFieldAction.ReplaceByEmpty;
                }
                catch (System.IO.IOException)
                {
                    _fstream = null;
                    _streamReader = null;
                    _reader = null;
                }
            }

            #region IEnumerator<IGridDataRecord> Members

            IGridDataRecord _current;
            public IGridDataRecord Current
            {
                get { return _current; }
            }

            #endregion

            #region IDisposable Members

            public void Dispose()
            {
                if (_reader != null)
                {
                    _reader.Dispose();
                    _reader = null;
                }
                if (_streamReader != null)
                {
                    _streamReader.Dispose();
                    _streamReader = null;
                }
                if (_fstream != null)
                {
                    _fstream.Dispose();
                    _fstream = null;
                }
            }

            #endregion

            #region IEnumerator Members

            object System.Collections.IEnumerator.Current
            {
                get { return _current; }
            }

            public bool MoveNext()
            {
                if (_reader != null && _reader.ReadNextRecord())
                {
                    _current = new CSVGridDataRecord(_reader, _importCultureInfo);
                    return true;
                }

                return false;
            }

            public void Reset()
            {
                if (_reader != null)
                    _reader.MoveTo(0);
            }

            #endregion
        }

        public IEnumerator<IGridDataRecord> GetEnumerator()
        {
            if (System.IO.File.Exists(_settings.FileName) == false)
                return null;

            return new CSVEnumerator(_settings.FileName, _settings.UseHeaders, _settings.Separator, _settings.ImportCultureInfo);
        }

        #endregion

        #region IEnumerable Members

        System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
        {
            if (System.IO.File.Exists(_settings.FileName) == false)
                return null;

            return new CSVEnumerator(_settings.FileName, _settings.UseHeaders, _settings.Separator, _settings.ImportCultureInfo);
        }

        #endregion

        #region IDisposable Members

        public void Dispose()
        {
        }

        #endregion
    }

    class CSVGridDataRecord : IGridDataRecord
    {
        readonly string[] _data;
        readonly System.Globalization.CultureInfo _importCultureInfo;

        public CSVGridDataRecord(CsvReader reader, System.Globalization.CultureInfo cultureInfo)
        {
            _importCultureInfo = cultureInfo;
            _data = new string[reader.FieldCount];
            reader.CopyCurrentRecordTo(_data);
        }

        #region IGridDataRecord Members

        public string this[int index]
        {
            get
            {
                if (_importCultureInfo != null && String.IsNullOrWhiteSpace(_data[index]) == false)
                {
                    double d = 0.0;
                    if (Double.TryParse(_data[index], System.Globalization.NumberStyles.Any, _importCultureInfo, out d))
                    {
                        // This parsed as a double in the import culture, but the reader expects things in the invariant culture
                        return d.ToString(System.Globalization.CultureInfo.InvariantCulture);
                    }
                }
                return _data[index];
            }
        }

        #endregion
    }
}
