# RESTful_API_Service-PersonsProject-
CSC 470 Software Engineering // A RESTful API allowing basic ADD|UPDATE|DELETE|GET functionality

## Assignment Overview:
Extension of JSFClient_RESTful-PersonProject 
https://github.com/mikecrbenton/JSFClient_RESTful-PersonProject-/blob/master/README.md

### Code written for assignment:
https://github.com/mikecrbenton/RESTful_API_Service-PersonsProject-/blob/master/src/main/java/org/usd/csci/person/data/Person.java
* Add fields for attributes: id, firstname,  lastname, and birthdate (Line 25)
* Create constants to represent the JSON keys for these attributes (Line 30)
* Create a method in the Person class toJSON() that returns a string representing a person in JSON format.
* {"id":1,"lastName":"Smith","birthDate":"09/17/2014","firstName":"Carol"} (Line 68)

https://github.com/mikecrbenton/RESTful_API_Service-PersonsProject-/blob/master/src/main/java/org/usd/csci/person/personrest/PersonResource.java
* Modify the @GET persons() method, so that according to user input it will:
  * Retrieve all records (Line 134)
  * Retrieve a record by name (Line 154)
  * Retrive a record by name in a range of records (Line 177)
  * Retrieve a range of records (Line 210)
  
 
  





