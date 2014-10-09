package contact.service.jpa;

import java.util.*;
import java.util.logging.Logger;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

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
		return query.getResultList();
	}

	@Override
	public List<Contact> findByTitle(String str) {
		Query query = em.createQuery("SELECT c FROM Contact c where UPPER(c.title) like :str");
		query.setParameter("str", "%"+str.toUpperCase()+"%");
		return query.getResultList();
	}

	@Override
	public boolean delete(long id) {
		EntityTransaction tx  = em.getTransaction();
		try{
		tx.begin();
		Contact contact = em.find(Contact.class, id);
		em.remove(contact);
		tx.commit();
		return true;
		}catch(EntityExistsException ex){
			Logger.getLogger(this.getClass().getName()).warning(ex.getMessage());
			if(tx.isActive())try{ tx.rollback(); } catch (Exception e) {}
			return false;
		}
	}

	@Override
	public boolean update(Contact contact) {
		EntityTransaction tx  = em.getTransaction();
		try{
			tx.begin();
			if(!containID(contact.getId()))
				return false;
			em.find(Contact.class, contact.getId());
			em.merge(contact);
			tx.commit();
			return true;
		}catch(EntityExistsException ex){
			Logger.getLogger(this.getClass().getName()).warning(ex.getMessage());
			if(tx.isActive())try{ tx.rollback(); } catch (Exception e) {}
			return false;
		}
	}

	@Override
	public void save(Contact contact) {
		EntityTransaction tx = em.getTransaction();
		try{
			tx.begin();
			if(contact.getId() == 0){
				generateID(contact);
			}
			em.persist(contact);
			tx.commit();	
		}catch(EntityExistsException ex){
			Logger.getLogger(this.getClass().getName()).warning(ex.getMessage());
			if(tx.isActive())try{ tx.rollback(); } catch (Exception e) {}
		}
	}

	@Override
	public boolean containID(long l) {
		Query query = em.createQuery("select c.id from Contact c where c.id = :id");
		query.setParameter("id", l);
		return !query.getResultList().isEmpty();
	}

	@Override
	public void generateID(Contact contact) {
		contact.setId(id++);
	}
}