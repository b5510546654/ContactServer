package contact.service.jpa;

import java.util.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;

import java.util.logging.Logger;

import jersey.repackaged.com.google.common.collect.Lists;
import contact.entity.Contact;
import contact.service.ContactDao;

/**
 * Data access object for saving and retrieving contacts,
 * using JPA.
 * To get an instance of this class use:
 * <p>
 * <tt>
 * dao = DaoFactory.getInstance().getContactDao()
 * </tt>
 * 
 * @author jim
 */
public class JpaContactDao implements ContactDao {
	/** the EntityManager for accessing JPA persistence services. */
	private final EntityManager em;
	static int id = 1;
	/**
	 * constructor with injected EntityManager to use.
	 * @param em an EntityManager for accessing JPA services.
	 */
	public JpaContactDao(EntityManager em) {
		this.em = em;
		createTestContact( );
	}

	
	/** add contacts for testing. */
	private void createTestContact( ) {
		int id = 101; // usually we should let JPA set the id
		if (find(id) == null) {
			Contact test = new Contact("Title", "Joe Experimental", "none@testing.com");
			test.setId(id);
			save(test);
		}
		id++;
		if (find(id) == null) {
			Contact test2 = new Contact("Another Test contact", "Testosterone", "testee@foo.com");
			test2.setId(id);
			save(test2);
		}
	}

	@Override
	public Contact find(long id) {
		return em.find(Contact.class, id);
	}

	@Override
	public List<Contact> findAll() {
		Query query = em.createQuery("select c from Contact c");
		List<Contact> list = query.getResultList();
		return query.getResultList();
	}

	@Override
	public List<Contact> findByStr(String str) {
		Query query = em.createQuery("SELECT c FROM Contact c where UPPER(c.title) like :str");
		query.setParameter("str", "%"+str.toUpperCase()+"%");
		List<Contact> list = query.getResultList();
		return query.getResultList();
	}

	@Override
	public boolean delete(long id) {
		EntityTransaction tx  = em.getTransaction();
		tx.begin();
		Contact contact = em.find(Contact.class, id);
		System.out.println(1);
		if(contact == null)
			return false;
		System.out.println(2);
		em.remove(contact);
		System.out.println(3);
		tx.commit();
		return true;
	}

	@Override
	public boolean update(Contact contact) {
		EntityTransaction tx  = em.getTransaction();
		tx.begin();
		if(!containID(contact.getId()))
			return false;
// DAO shouldn't copy contact attributes. Use em.merge()
		Contact contactDB = em.find(Contact.class, contact.getId());
		//		if(contact.getTitle() != null)
		contactDB.setTitle(contact.getTitle());
		//		if(contact.getName() != null)
		contactDB.setName(contact.getName());
		//		if(contact.getEmail() != null)
		contactDB.setEmail(contact.getEmail());
		//		if(contact.getPhoneNumber() != 0)
		contactDB.setPhoneNumber(contact.getPhoneNumber());
		contactDB.setPhotoUrl(contact.getPhotoUrl());
		tx.commit();
// no try - catch or rollback
		return true;
	}

	@Override
	public void save(Contact contact) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		if(contact.getId() == 0){
			generateID(contact);
		}
		em.persist(contact);
		tx.commit();	
// no try - catch or rollback
	}

	@Override
	public Contact createContact() {
		Contact contact = new Contact();
		generateID(contact);
		save(contact);
		return contact;
	}

	@Override
	public boolean containID(long l) {
		Query query = em.createQuery("select c.id from Contact c where c.id = :id");
		query.setParameter("id", l);
		return !query.getResultList().isEmpty();
	}

	@Override
	public Contact createContact(long id, String title, String email,
			String name,String photoURL, int phoneNumber) {
		Contact contact = new Contact();
		contact.setTitle(title);
		contact.setEmail(email);
		contact.setName(name);
		contact.setPhotoUrl(photoURL);
		contact.setPhoneNumber(phoneNumber);
		if(id != 0){
			contact.setId(id);
		}
		else{
			generateID(contact);
		}
		save(contact);
		return contact;
	}

	@Override
	public void generateID(Contact contact) {
		contact.setId(id++);
	}
}