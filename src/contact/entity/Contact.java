package contact.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * Contact represent structure of contact XML.
 * Contact include title , name ,email ,phoneNumber ,photoURL
 * Each contact has difference id.
 * @author Rungroj Maipradit 5510546654
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Contact implements Serializable{
	/** Each contact has difference id.	*/
	@Id
	@XmlAttribute
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	/** Contact title. */
	@XmlElement(required=true)
	private String title;
	/** Contact name. */
	private String name;
	/** Contact email. */
	private String email;
	/** Contact number. */
	private int phoneNumber;
	/** URL of photo */
	private String photoUrl;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public Contact(){
		
	}
	public Contact(String title, String name, String email) {
		this.title = title;
		this.name = name;
		this.email = email;
	}
	public Contact(long id) {
		this.id = id;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	
	
}
