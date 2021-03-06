package contact.service.mem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import contact.entity.Contact;
import contact.service.ContactDao;
/**
 * Has map that contains created contact.
 * Use similar to database.
 * @author Rungroj Maipradit 5510546654
 */
public class MemContactDao implements ContactDao {
	/** Represent a contact every contact has different id. */
	static Long id = (long) 1;
	/** Contains id in key and contact in value.	 */
	private Map<Long, Contact> map;
	/**
	 * Initial map.
	 */
	public MemContactDao() {
		map = new HashMap<Long, Contact>();
	}

	/**
	 * @see contact.service.ContactDao#find(long)
	 */
	@Override
	public Contact find(long id){
		return map.get(id);
	}

	/**
	 * @see contact.service.ContactDao#findAll()
	 */
	@Override
	public List<Contact> findAll(){
		List<Contact> list = new ArrayList<Contact>();
		for (Long in : map.keySet()) {
			list.add(map.get(in));
		}
		return list;
	}

	/**
	 * @see contact.service.ContactDao#findByTitle(String)
	 */
	@Override
	public List<Contact> findByTitle(String str){
		List<Contact> list = new ArrayList<Contact>();
		for (Long in : map.keySet()) {
			if(map.get(in).getTitle() != null && map.get(in).getTitle().toLowerCase().contains(str.toLowerCase()))
				list.add(map.get(in));
		}
		return list;
	}

	/**
	 * @see contact.service.ContactDao#delete(long)
	 */
	@Override
	public boolean delete(long id){
		if(map.containsKey(id)){
			map.remove(id);	
			return true;
		}
		else
			return false;
	}

	/**
	 * @see contact.service.ContactDao#update(Contact)
	 */
	@Override
	public boolean update(Contact contact){
		if(!map.containsKey(contact.getId()))
			return false;
//		long id = contact.getId();
//		if(contact.getTitle() == null)
//			contact.setTitle(map.get(id).getTitle());
//		if(contact.getName() == null)
//			contact.setName(map.get(id).getName());
//		if(contact.getEmail() == null)
//			contact.setEmail(map.get(id).getEmail());
//		if(contact.getPhoneNumber() == 0)
//			contact.setPhoneNumber(map.get(id).getPhoneNumber());
		map.remove(contact.getId());
		map.put(contact.getId(), contact);
		return true;
	}

	/**
	 * @see contact.service.ContactDao#save(Contact)
	 */
	@Override
	public void save(Contact contact){
		if(contact.getId() == 0){
			generateID(contact);
		}
		map.put(contact.getId(), contact);
	}

	/**
	 * @see contact.service.ContactDao#generateID(Contact)
	 */
	@Override
	public void generateID(Contact contact) {
		contact.setId(id);
		map.put(id++, contact);

	}


	/**
	 * @see contact.service.ContactDao#containID(long)
	 */
	@Override
	public boolean containID(long id){
		return map.containsKey(id);
	}

}
