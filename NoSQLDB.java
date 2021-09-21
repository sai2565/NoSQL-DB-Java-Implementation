import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class NoSQLDB {
//driver function
public static void main(String[] args) {
while(true){
try{
//reading db configurations
FileReader dbConfigReader = new FileReader("dbconfig.json");
JSONParser configParser = new JSONParser();
Object configobject = configParser.parse(dbConfigReader);
dbConfigReader.close();
JSONArray dbConfigArray = (JSONArray) configobject;
if(dbConfigArray.size() == 0){
 createDB();	
}else{
	JSONObject dbConfigObject = (JSONObject) dbConfigArray.get(0);
	//reading data
	FileReader dbDataReader = new FileReader("dbdata.json");
	JSONParser dataparser = new JSONParser();
	Object dataobject = dataparser.parse(dbDataReader);
	dbDataReader.close();
	JSONArray dbData = (JSONArray) dataobject;
	//db operations
	System.out.println("Choose one of the following database operations");
	System.out.println("Enter 1 to Add a Record");//key value
	System.out.println("Enter 2 to Delete Records");//key
	System.out.println("Enter 3 to Find Records");//filter value //selection list
	System.out.println("Enter 4 to Display all Records");
	System.out.println("Enter 5 to Reset your Database");
	Scanner in = new Scanner(System.in);
	int selection = in.nextInt();
	int numOfFields = dbConfigObject.size();
	switch (selection) {
		case 1://add new record
		 int i = 0;
		 JSONObject newDataObject = new JSONObject();
		 FileWriter addRecordWriter = new FileWriter("dbdata.json");
		 if(numOfFields == 1){
		  System.out.println("Enter a value");
		  in.nextLine();
		  newDataObject.put(dbConfigObject.get("field1"), in.nextLine());
		 }else{
		   in.nextLine();	 
		   while(i < numOfFields){
		    System.out.println("Enter "+dbConfigObject.get("field"+(i+1)));
			newDataObject.put(dbConfigObject.get("field"+(i+1)), in.nextLine());
			i++;
		   }
	     }
		 dbData.add(newDataObject);
		 addRecordWriter.write(dbData.toJSONString());
		 addRecordWriter.flush();
		 addRecordWriter.close();
//		 Thread.currentThread().sleep(500);
		 System.out.println("One record has been added to the database");
		 System.out.println();
//		 Thread.currentThread().sleep(500);
		break;
		case 2://delete records
		  int j = 0;
		  int numRecords = dbData.size();
		  if(numOfFields == 1){
		  System.out.println("Enter a value to be deleted");
		  in.nextLine();
		  String deletValue = in.nextLine();
		  dbData.removeIf((d) -> {
		   JSONObject jo = (JSONObject) d;
		   if(jo.get(dbConfigObject.get("field1")).equals(deletValue)){
		    return true;
		   }
			return false;
		   });
		  }else{
		  System.out.println("Choose one of the fields by which you want to delete the records");
		  while(j < numOfFields){
		   System.out.println("Enter "+(j+1)+" to choose " + dbConfigObject.get("field"+(j+1)));
		   j++;
		  }
		  String delField = (String) dbConfigObject.get("field"+in.nextInt());
		  System.out.println("Enter value of "+delField+" to be deleted");
		  in.nextLine();
		  String delValue = in.nextLine();
		  dbData.removeIf((d) -> {
		   JSONObject jo = (JSONObject) d;
		   if(jo.get(delField).equals(delValue)){
		    return true;
		   }
			return false;
		   });
		  }
		  FileWriter deleteRecordWriter = new FileWriter("dbdata.json");
		  deleteRecordWriter.write(dbData.toJSONString());
		  deleteRecordWriter.flush();
		  deleteRecordWriter.close();
//		  Thread.currentThread().sleep(500);
		  System.out.println(""+(numRecords - dbData.size())+" records have been deleted");
		  System.out.println();
//		  Thread.currentThread().sleep(500);
		break;
		case 3:
		  JSONArray data =  (JSONArray) dbData.clone();
		  if(numOfFields == 1){
		  System.out.println("Enter a value to filter the records");
		  in.nextLine();
		  String filtVal = in.nextLine();
		  data.removeIf((d) -> {
		   JSONObject jo = (JSONObject) d;
		   if(((String)jo.get(dbConfigObject.get("field1"))).toLowerCase().contains(filtVal.toLowerCase())){
		    return false;
		   }
		    return true;
		   });
		  if(data.size() > 0){
			System.out.println(data.toJSONString());
		  }else{
		    System.out.println("There are no records matching your filter conditions");
		  }
		  }
		  else{
		   System.out.println("Enter a value to filter the records");
		   in.nextLine();
		   String filterValue = in.nextLine();
		   System.out.println("Do you want to restrict selection fields ?\nEnter y (yes) or n (no)");
		   String fieldSelecn = in.nextLine();
		   if(fieldSelecn.equals("y") || fieldSelecn.equals("yes")){
			System.out.println("Select one or more of the following fields separated by ','");
			dbConfigObject.forEach((key, val) -> {
			 System.out.print(val+"\t");
			});
			System.out.println();
			String selecnFields = in.nextLine();
			String selectedFields [] = selecnFields.split(",");
			JSONArray res = new JSONArray();
			for(int l = 0; l < data.size(); l++){
			 JSONObject to = new JSONObject();
			 boolean isFiltered = false;
			 for(int m = selectedFields.length-1; m >= 0; m--){
			  to.put(selectedFields[m].trim(), ((JSONObject)data.get(l)).get(selectedFields[m].trim()));
			  if(((String)((JSONObject)data.get(l)).get(selectedFields[m].trim())).toLowerCase().contains(filterValue.toLowerCase())){
		       isFiltered = true;
			  }
			 }
			 if(isFiltered){
			  res.add(to); 	 
			 }
			}
			System.out.println(res.toJSONString());
		   }else{
		    data.removeIf((d) -> {
			 JSONObject jo = (JSONObject) d;
			 int t = 0;
			 while(t<numOfFields){
			  if(((String)jo.get(dbConfigObject.get("field"+(t+1)))).toLowerCase().contains(filterValue.toLowerCase())){
			   return false;
			  }
			  t++;
			 }
			 return true;
			});
//		    Thread.currentThread().sleep(500);
			if(data.size() > 0){
			 System.out.println(data.toJSONString());
			}else{
			 System.out.println("There are no records matching your filter conditions");
			}
		   }
		  }
		  System.out.println();
//		  Thread.currentThread().sleep(500);
		break;
		case 4:
//		 Thread.currentThread().sleep(500);
		 System.out.println(dbData.toJSONString());	
		break;
		case 5:
		 System.out.println("This will delete all the data and collections. Do you really want to proceed?");
		 System.out.println("Enter y to proceed or any other key to exit");
		 if(in.next().equals("y")){
		  FileWriter dbConfigWriter = new FileWriter("dbconfig.json");
		  dbConfigWriter.write(new JSONArray().toJSONString());
		  dbConfigWriter.flush();
		  dbConfigWriter.close();
		  FileWriter dbDataWriter = new FileWriter("dbdata.json");
		  dbDataWriter.write(new JSONArray().toJSONString());
		  dbDataWriter.flush();
		  dbDataWriter.close();
		 }
		break;	
		default:
		break;
	   }	
}
}catch(FileNotFoundException fnfe){//there is no dbconfig file
 //db setup
 createDB();
}catch(Exception e){
 e.printStackTrace();
 }
}
}
//creating db and a collection that can be extended to multiple collections if required
public static void createDB(){
 try{
  System.out.println("Please setup your database to proceed");
  FileWriter dbConfigWriter = new FileWriter("dbconfig.json");
  System.out.println("Enter number of fields in your collection");
  Scanner inp = new Scanner(System.in);
  int numOfFields = inp.nextInt();
  JSONObject collection = new JSONObject();
  int i=0;
  while(i < numOfFields){
   System.out.println("Enter field "+(i+1)+" name with out spaces");
   String field = inp.next();
   collection.put("field"+(i+1), field);
   i++;
  }
  JSONArray collections = new JSONArray();
  collections.add(collection);
  dbConfigWriter.write(collections.toJSONString());
  dbConfigWriter.flush();
  dbConfigWriter.close();
  FileWriter dbDataWriter = new FileWriter("dbdata.json");
  dbDataWriter.write(new JSONArray().toJSONString());
  dbDataWriter.flush();
  dbDataWriter.close();
 }catch(Exception e){
   e.printStackTrace();
 }
}
}