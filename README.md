# Scripbox-NoSQL-DB-Java-Implementation

To execute this tool, follow below steps :

. Download the 'NoSQLDB.jar' file
. Open command prompt and change the working directory to the location of the downloaded jar file ex: C:\Users\Sai\Downloads
. Execute the command 'java -jar NoSQLDB.jar'

Various actions provided by this tool:

> Setting up DB
  When you execute the tool for the first time or when the DB has been reset, you will be prompted to setup a DB collection.
  Where you can enter number of fields in your collection followed by the field names.

> Add Records
  You can add records one at a time in to db by entering the values of each fields one by one.
  
> Delete Records
  You can delete the records from db by matching a value with values of one of the fields.
  for ex: collection schema has Name, City fields
  you can delete City 'Bangalore' by matching the value 'Bangalore' with city field
  
> Find Records (filtering records)
  You can filter the records by a value matching with any fields in the schema.(casing is ignored)
  You can also select the field with which you want to match the filter value.
  Projecting the fields is also possible where the filtered value is matched in only projected fields.
  for ex: collection schema has Name, City fields
          if you filter by 'bangalore' with out projecting any fields, you will get all records with matching City.
            ex res: [{Name:Sharan,City:Bangalore},{Name:Sai,City:Bangalore}]
          if you project Name and filter by 'Bangalore' you will get all the records where Name contains 'bangalore'.
            ex res: [{Name:Bangalore Sharan}]
            
> Display all records
 
> Reset DB            
  
