package hioa.mappe2.s180475;

import java.io.Serializable;

//Class that holds all the information about a person that's saved in the birthday-app.
public class Person implements Serializable{
	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private int hasPersonalSMS;
	private String tlf;
	private String date;
	
	public Person(String fn, String ln, String nr, String bday, int hasMessage){
		firstName = fn;
		lastName = ln;
		tlf = nr;
		date = bday;
		hasPersonalSMS = hasMessage;
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public String getLastName(){
		return lastName;
	}
	
	public int gethasPersonalSMS(){
		return hasPersonalSMS;
	}
	
	public String getTlf(){
		return tlf;
	}
	
	public String getDate(){
		return date;
	}
	
	public void setFirstName(String s){
		firstName = s;
	}
	
	public void setLastName(String s){
		lastName = s;
	}
	
	public void sethasPersonalSMS(int ok){
		hasPersonalSMS = ok;
	}
	
	public void setTlf(String s){
		tlf = s;
	}
	
	public void setDate(String bday){
		date = bday;
	}
}
