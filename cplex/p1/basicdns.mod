/*********************************************
 * OPL 12.8.0.0 Model
 * Author: sormaz
 * Creation Date: Jan 15, 2023 at 8:29:49 PM
 *********************************************/

float maxOfx =...;
dvar float x;

maximize x;
subject to {
  x<=maxOfx;
}

main
{
  var f = new IloOplInputFile("datafiles.txt");
   if (f.exists) {
     writeln("the file datafiles.txt exists");
     var s;
     var cplex = new IloCplex();
     var source = new IloOplModelSource("basicdns.mod");
  	var def = new IloOplModelDefinition(source);
     while (!f.eof) {
      s=f.readline();
      writeln(s); 
      var opl = new IloOplModel(def,cplex);
  var data = new IloOplDataSource(s);
  opl.addDataSource(data);
  opl.generate();
  if (cplex.solve()) {
     writeln("OBJ = " + cplex.getObjValue());
  }  else {
     writeln("No solution");
  }
      
     }
     writeln("finished all data files");
     f.close();
   }   
   else { 
     writeln("the file output.txt doesn't exist");
   }  
}
