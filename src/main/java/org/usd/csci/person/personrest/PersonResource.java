/*
 * $Id$
 * $Name$
 */

package org.usd.csci.person.personrest;

import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ejb.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.DELETE;
import static javax.ws.rs.HttpMethod.POST;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.usd.csci.person.data.Person;
import static org.usd.csci.person.data.Person.BIRTH_DATE_FORMAT;
import static org.usd.csci.person.data.Person.BIRTH_DATE_KEY;
import static org.usd.csci.person.data.Person.FIRST_NAME_KEY;
import static org.usd.csci.person.data.Person.LAST_NAME_KEY;

/**
 * REST Web Service
 *
 * @author Mike Benton CSC470
 */
@Singleton
@Path("/persons")
public class PersonResource {
    
    Map<Integer, Person> persons = new ConcurrentHashMap<Integer, Person>();
    AtomicInteger id = new AtomicInteger();
    
    //--------------------------------------------------------------------------
    @Context
    private UriInfo context;
    //--------------------------------------------------------------------------
    
    public PersonResource() {
        loadPersons();
    }
    
    /**
     * readPerson       receives a JSON string and returns a Person object
     * 
     * @param json      String representing a JSON object
     * 
     * @return          Person with all fields set from JSON String
     * 
     * @throws JSONException    if problem with JSON processing
     * @throws ParseException   if Date object in wrong format
     */
     public Person readPerson(String json)throws JSONException, ParseException{
        
        Person personToReturn = new Person();
        JSONObject obj = new JSONObject(json);
        
        id.incrementAndGet();
        personToReturn.setId(id.get());
        personToReturn.setFirstname(obj.getString(FIRST_NAME_KEY));
        personToReturn.setLastname(obj.getString(LAST_NAME_KEY));
        
        String birthdateString = obj.getString(BIRTH_DATE_KEY);
        Date dateObject = new SimpleDateFormat("MM/dd/yyyy").parse(birthdateString); 
        personToReturn.setBirthdate(dateObject);  
        
        return personToReturn;
    }
    
    
    private void loadPersons(){
        
        //THIS WAS ONLY TO LOAD IN A PERSON FOR TESTING
        id.incrementAndGet();
        Person aPerson = new Person();
      
        aPerson.setFirstname("Carol");
        aPerson.setLastname("Smith");
        aPerson.setBirthdate(new Date());
        aPerson.setId(id.get());
        persons.put(id.get(), aPerson);
        
        id.incrementAndGet();
        
        aPerson = new Person();
        aPerson.setFirstname("Jack");
        aPerson.setLastname("Jackson");
        aPerson.setBirthdate(new Date());
        aPerson.setId(id.get());
        persons.put(id.get(), aPerson);
        
    }
    
    /**
     * persons      receives parameter(s) from the user and returns a String with
     *              the desired query results
     * 
     * @param start the starting position ID
     * @param size  the amount of results(Persons) to return
     * @param name  the last name specified as a search query
     * 
     * @return      String representing the JSON Person objects that were searched
     *              by the user
     */
    
    @GET
    @Produces("application/json")
    public String persons(@QueryParam("start") int start,
            @QueryParam("size") int size, @QueryParam("name") String name){
        
        if(size == 0){
            //BLOCK FOR NO PARAMETERS ENTERED BY USER IN ANY OF THE SEARCH FIELDS (RETRIEVE ALL RECORDS)
            if(name==null || name.isEmpty()){

                try{
                    JSONObject jPersons = new JSONObject();
                    JSONArray jlist = new JSONArray();
                    Collection<Person> list = persons.values();
                    
                    for(Person aPerson : list){
                        JSONObject obj = new JSONObject(aPerson.toJSON());
                        jlist.put(obj);
                    }
                    
                    jPersons.put("persons", jlist);
                    return jPersons.toString();
                    
                } catch(JSONException e){
                    throw new WebApplicationException(Response.Status.BAD_REQUEST);
                }
            }
            //BLOCK FOR FINDING THE NAME PARAMETER ONLY (RETRIEVE BY NAME)
            else if(!name.isEmpty()){
                
                try{
                    JSONObject jPersons = new JSONObject();
                    JSONArray jlist = new JSONArray();
                    Collection<Person> list = persons.values();
                    
                    for(Person aPerson : list){
                        if( aPerson.getLastname().equals(name) ){
                            JSONObject obj = new JSONObject(aPerson.toJSON());
                            jlist.put(obj);
                        }
                    }
                    
                    jPersons.put("persons", jlist);
                    return jPersons.toString();
                    
                } catch(JSONException e){
                    throw new WebApplicationException(Response.Status.BAD_REQUEST);
                }
            }
        }
        //BLOCK FOR FINDING ALL PARAMETERS ENTERED BY USER (NAME, START, SIZE)
        else if (size != 0 && !name.isEmpty()){
            try{
                JSONObject jPersons = new JSONObject();      
                JSONArray jlist = new JSONArray();          
                Collection<Person> list = persons.values();   
                
                Person[] personArray;
                personArray =(Person[])persons.values().toArray(new Person[persons.size()]);
                int listSize = persons.size();
                
                if( (size+start-1) > (listSize) ){
                        throw new ArrayIndexOutOfBoundsException();
                } 
                
                for(int i = (start-1) ; i < (size+(start-1)) ; i++){       
                        
                     Person aPerson = personArray[i];
                     
                     if( aPerson.getLastname().equals(name) ){
                     JSONObject obj = new JSONObject(aPerson.toJSON());
                     jlist.put(obj);  
                     }
                }
                        
                jPersons.put("persons", jlist);
                return jPersons.toString();

            }catch(JSONException e){
                        throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
        }
        
        //BLOCK FOR FINDING THE SIZE & START PARAMETER ONLY - NOTHING ENTERED IN NAME PARAMETER(RETRIEVE RANGE OF RECORDS)
        //else (size != 0) ELSE STATEMENT COMMENTED OUT SO METHOD RECOGNIZED A RETURN STATEMENT
            try{
                JSONObject jPersons = new JSONObject();      
                JSONArray jlist = new JSONArray();           
                Collection<Person> list = persons.values();  
                
                Person[] personArray = (Person[])persons.values().toArray(new Person[persons.size()]);
                int listSize = persons.size();
                
                if( (size+start-1) > (listSize) ){
                    throw new ArrayIndexOutOfBoundsException();
                } 
                
                //REMEMBER int i IS ALWAYS CHANGING IN THE LOOP, YOU NEEDED A CONSISTENT VARIABLE (start-1) not (i-1)
                for(int i = (start-1) ; i < (size+(start-1)) ; i++){       
                        
                     Person aPerson = personArray[i];
                     JSONObject obj = new JSONObject(aPerson.toJSON());
                     jlist.put(obj);   
                }
                jPersons.put("persons", jlist);
                return jPersons.toString();

            }catch(JSONException e){
                        throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
            //}
            
    }//END OF PERSONS @QUERYPARAM
    
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public String persons(@PathParam("id") int id){
        try{
            
            Person aPerson = persons.get(id);
            if(aPerson != null){
                return aPerson.toJSON();
            }else{
                throw new NotFoundException("not located");
            }
        }catch(Exception e){ //DIRECTIONS SAY JSONEXCEPTION
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }
    
    @POST
    @Consumes("application/json")
    public Response createPerson(String is){
        try{
            
            Person person = readPerson(is);
            persons.put(person.getId(), person);
            
            return Response.created(URI.create("/persons/" + person.getId())).build();
        }catch(JSONException e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }catch(ParseException e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public Response updatePerson(@PathParam("id") int id, String is){
        try{
            Person person = readPerson(is);
            person.setId(id);
            Person locPerson = persons.get(id);
            if(locPerson == null){
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            persons.put(person.getId(), person);
            
            return Response.status(Response.Status.OK).build();
            
        }catch(JSONException e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }catch(ParseException e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response deletePerson(@PathParam("id") int id, String is){
        
        try{
            persons.remove(id);
        }catch(Exception e){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
            return Response.status(Response.Status.OK).build();     
    }
    
}//END CLASS
