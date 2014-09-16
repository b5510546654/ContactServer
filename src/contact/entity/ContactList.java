package contact.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * ContactList has list of contact in site.
 * Use for show many contact in xml.
 * @author Rungroj Maipradit 5510546654
 */
@XmlRootElement
public class ContactList {
	/** List of contact. */
	@XmlElement(name="contact")
	private List<Contact> contactlist = new ArrayList<>();
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
		this.contactlist = contactlist;
	}
	
	/**
	 * Add contact to contactList.
	 * @param contact added to contactList.
	 */
	public void addContact(Contact contact) {
		contactlist.add(contact);
	}
}
