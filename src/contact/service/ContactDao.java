package contact.service;

import java.util.List;

import contact.entity.Contact;

/**
 * Interface of Dao use for contract with ContactResource.
 * Can get Dao concrete class from DaoFactory.getContact();.
 * @author Rungroj Maipradit 5510546654
 */
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
	public abstract List<Contact> findByTitle(String str);

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
	 * Check map has key or not.
	 * @param id that use to check
	 * @return if has return true if not return false
	 */
	public abstract boolean containID(long id);

	
	/**
	 * If contact id is 0 (unassigned) generate new id for its.
	 * @param contact generate id for this contact
	 */
	public abstract void generateID(Contact contact);

}