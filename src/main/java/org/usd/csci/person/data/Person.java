/*
 * $Id$
 * $Name$
 */

package org.usd.csci.person.data;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @ author  Mike Benton CSC470 
 */
public class Person implements Serializable {
    
    private Integer id;
    private String firstname;
    private String lastname;
    Date birthdate = new Date(); 
    
    public static final String ID_KEY = "id";
    public static final String FIRST_NAME_KEY = "firstname";
    public static final String LAST_NAME_KEY = "lastname";
    public static final String BIRTH_DATE_KEY = "birthdate";
    public static final String BIRTH_DATE_FORMAT = "MM/dd/yyyy";
    
    /**
     * toJSON   takes the fields from a Java Person object, and returns a String
     *          in JSON format representing a Person
     * 
     * @return  String in JSON format
     */
    
    /*public static void main(String[] args)throws Exception{
        String test = "[1,2,3,4,5]";
        ArrayList mainList = returnArrayList(test);
        
        for(int i = 0; i<mainList.size();i++){
            System.out.println(mainList.get(i));
        }
    }*/
    
    public static ArrayList<Integer> returnArrayList(String jsonString)throws Exception{
        
        JSONArray list = new JSONArray(jsonString);
        ArrayList<Integer> arrList = new ArrayList<Integer>();
        
        for(int i = 0; i<list.length(); i++){
            arrList.add(list.getInt(i));
        }
        
        return arrList;
        
        
        
    }
    
    public String toJSON(){
        
        JSONObject obj = new JSONObject();
        DateFormat df = new SimpleDateFormat(BIRTH_DATE_FORMAT);
        String formattedDate;
        
        try {
            obj.put(ID_KEY,this.id);
            obj.put(FIRST_NAME_KEY,this.firstname);
            obj.put(LAST_NAME_KEY,this.lastname);
            
            formattedDate= df.format(birthdate);
            obj.put(BIRTH_DATE_KEY,formattedDate);
             
        } catch (JSONException ex) {
            ex.getMessage();
            Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return obj.toString();
        
    }//END TOJSON METHOD

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirthdate() {
        return birthdate;
    }
    
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    
}
