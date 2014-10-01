package contact.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * ContactList has list of contact in site.
 * Use for show many contact in xml then put it in file.
 * @author Rungroj Maipradit 5510546654
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactList {
	/** List of contact. */
	@XmlElement(name="contact")
	private List<Contact> contactList = new ArrayList<>();
	/**
	 * Default Constructor.
	 */
	public ContactList() {
		this(new ArrayList<Contact>());
	}
	/**
	 * Constructor that set contactList
	 * @param contactList use for set contactList
	 */
	public ContactList(List<Contact> contactList){
		this.contactList = contactList;
	}
	
	/**
	 * Add contact to contactList.
	 * @param contact added to contactList.
	 */
	public void addContact(Contact contact) {
		contactList.add(contact);
	}
	
	public List<Contact> getContactList(){
		return contactList;
	}
}
