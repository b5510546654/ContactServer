package contact.service;

import java.util.List;

import contact.entity.Contact;

public interface ContactDao {

	/**
	 * Find contact by id
	 * @param id key match with contact
	 * @return contact that key match with.
	 */
	public abstract Contact find(long id);

	/**
	 * Return all of contact in map.
	 * @return all of contact in list
	 */
	public abstract List<Contact> findAll();

	/**
	 * Return list of contact that title contain str.
	 * @param str substring of title
	 * @return list of contact that title contain str
	 */
	public abstract List<Contact> findByStr(String str);

	/**
	 * Remove contact from map by id. 
	 * @param id key that use for remove
	 * @return return delete success or fail
	 */
	public abstract boolean delete(long id);

	/**
	 * Update contact from id.
	 * @param contact that use for update
	 * @return return update success or fail
	 */
	public abstract boolean update(Contact contact);

	/**
	 * Save new contact in map.
	 * @param contact that use to save
	 */
	public abstract void save(Contact contact);

	/**
	 * Create contact with generate id and other value is null.
	 * @return contact that created.
	 */
	public abstract Contact createContact();

	/**
	 * Check map has key or not.
	 * @param id that use to check
	 * @return if has return true if not return false
	 */
	public abstract boolean containID(long id);

	/**
	 * Create contact from parameter that given.
	 * @param l of contact if id = 0 then use id from generateID.
	 * @param title of contact.
	 * @param email of contact.
	 * @param name of contact.
	 * @param phoneNumber of contact.
	 * @return contact that created.
	 */
	public abstract Contact createContact(long l, String title, String email,
			String name,String photoURL, int phoneNumber);
	
	
	/**
	 * If contact id is 0 (unassigned) generate new id for its.
	 * @param contact generate id for this contact
	 */
	public abstract void generateID(Contact contact);

}