import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class NoSQLDB {
	
	public static void main(String[] args) {
		while(true){
			try{
				//reading db configurations
				FileReader dbConfigReader = new FileReader("dbconfig.json");
				JSONParser configParser = new JSONParser();
				Object configobject = configParser.parse(dbConfigReader);
				dbConfigReader.close();
				JSONArray dbConfigArray = (JSONArray) configobject;
				JSONObject dbConfigObject = (JSONObject) dbConfigArray.get(0);
				//reading data
				FileReader dbDataReader = new FileReader("dbdata.json");
				JSONParser dataparser = new JSONParser();
				Object dataobject = dataparser.parse(dbDataReader);
				JSONArray dbData = (JSONArray) dataobject;
				//db operations
					System.out.println("Choose one of the following database operations");
					System.out.println("Enter 1 to Add a Record");//key value
					System.out.println("Enter 2 to Delete Records");//key
					System.out.println("Enter 3 to Find Records");//filter value //selection list
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
									newDataObject.put(dbConfigObject.get("field1"), in.next());
								}else{
									while(i < numOfFields){
										System.out.println("Enter "+dbConfigObject.get("field"+(i+1)));
										newDataObject.put(dbConfigObject.get("field"+(i+1)), in.next());
										i++;
									}
								}
								
								dbData.add(newDataObject);
								addRecordWriter.write(dbData.toJSONString());
								addRecordWriter.flush();
								addRecordWriter.close();
								System.out.println("One record has been added to the database");
								System.out.println();
								Thread.currentThread().sleep(500);
							break;
						case 2://delete records
							int j = 0;
							int numRecords = dbData.size();
								if(numOfFields == 1){
										System.out.println("Enter a value to be deleted");
									String deletValue = in.next();
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
									String delValue = in.next();
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
								System.out.println(""+(numRecords - dbData.size())+" records have been deleted");
								System.out.println();
								Thread.currentThread().sleep(500);
							break;
						case 3:
							JSONArray data =  (JSONArray) dbData.clone();
//								System.out.println("Choose one of the fields by which you want to filter the records");
							int k = 0;
								if(numOfFields == 1){
									System.out.println("Enter a value to filter the records");
									String filtVal = in.next();
									data.removeIf((d) -> {
										JSONObject jo = (JSONObject) d;
										if(((String)jo.get(dbConfigObject.get("field1"))).contains(filtVal)){
											return false;
										}
										return true;
									});
								}
								else{
//									while(k < numOfFields){
//										System.out.println("Enter "+(k+1)+" to choose " + dbConfigObject.get("field"+(k+1)));
//										k++;
//									}
									System.out.println("Enter a value to filter the records");
									String filterValue = in.next();
									System.out.println("Do you want to restrict selection fields ?\nEnter y (yes) or n (no)");
									String fieldSelecn = in.next();
									if(fieldSelecn.equals("y")){
										System.out.println("Select one or more of the following fields separated by ','");
										dbConfigObject.forEach((key, val) -> {
											System.out.print(val+"\t");
										});
										System.out.println();
										String selecnFields = in.next();
										data.removeIf((d) -> {
											JSONObject jo = (JSONObject) d;
											int t = 0;
											while(t<numOfFields){
												if((selecnFields.contains((String)dbConfigObject.get("field"+(t+1)))) && ((String)jo.get(dbConfigObject.get("field"+(t+1)))).contains(filterValue)){
													return false;
												}
												t++;
											}
											return true;
										});
										if(data.size() > 0){
											data.forEach((d) -> {
												JSONObject jo = (JSONObject) d;
												jo.forEach((key, val) -> {
													if(selecnFields.contains((String)key)){
														System.out.print(val+"\t");
													}
												});
												System.out.println();
											});
										}else{
											System.out.println("There are no records matching your filter conditions");
										}
									}else{
										data.removeIf((d) -> {
											JSONObject jo = (JSONObject) d;
											int t = 0;
											while(t<numOfFields){
												if(((String)jo.get(dbConfigObject.get("field"+(t+1)))).contains(filterValue)){
													return false;
												}
												t++;
											}
											return true;
										});
										if(data.size() > 0){
											data.forEach((d) -> {
												JSONObject jo = (JSONObject) d;
												jo.forEach((key, val) -> {
													System.out.print(val+"\t");
												});
												System.out.println();
											});
										}else{
											System.out.println("There are no records matching your filter conditions");
										}
									}
								}
//								System.out.println();
								System.out.println();
								Thread.currentThread().sleep(500);
							break;
						default:
							break;
					}
			}catch(FileNotFoundException fnfe){
				//db setup
				createDB();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void createDB(){
		try{
				System.out.println("Please setup your database");
			FileWriter dbConfigWriter = new FileWriter("dbconfig.json");
				System.out.println("Enter number of fields in your collection");
			Scanner inp = new Scanner(System.in);
			int numOfFields = inp.nextInt();
			JSONObject collection = new JSONObject();
			int i=0;
				while(i < numOfFields){
						System.out.println("Enter field "+(i+1)+" name");
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
