using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
namespace ORCustomisedAddINProject
{
    class TestClassForRead
    { 
       public  static void Main() { 
        String text = @"C:\Users\Simiotest\Book1.xlsx";
                
                foreach (string objects in File.ReadAllLines(text))
                {

                    string value = objects;
                    if(value== "AddObjectReferenceColumn")

                    {
                    Console.WriteLine(value);
                    Console.ReadLine();
                    }
            }
    }}
}
