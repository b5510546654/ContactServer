package contact.service.mem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import contact.entity.Contact;
import contact.entity.ContactList;
import contact.service.*;
import contact.service.mem.MemContactDao;

/**
 * MemDaoFactory is a factory for getting instances of entity DAO object
 * that use memory-based persistence, which isn't really persistence at all!
 * 
 * @see contact.service.DaoFactory
 * @version 2014.09.19
 * @author jim
 */
public class MemDaoFactory extends DaoFactory {
	/** instance of the entity DAO */
	private ContactDao daoInstance;

	public MemDaoFactory() {
		daoInstance = new MemContactDao();
		loadFile();
	}

	@Override
	public ContactDao getContactDao() {
		return daoInstance;
	}

	@Override
	public void shutdown() throws FileNotFoundException  {
		try {
			write(daoInstance.findAll());
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void write(List<Contact> contacts) throws JAXBException, IOException{
		File file = new File("newfile2.xml");
		System.out.println("WRITE");
		JAXBContext jaxbContext = JAXBContext.newInstance(ContactList.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(new ContactList(contacts), file);
	}
	
	public void loadFile() {
		File infile = new File("newfile2.xml");
		try {
			JAXBContext context = JAXBContext.newInstance( ContactList.class );
			Unmarshaller um = context.createUnmarshaller();
			ContactList list = (ContactList)um.unmarshal(infile);
			if(list!=null) {
				List<Contact> contacts = list.getContactList();
				if(contacts!=null) {
					for(Contact c: contacts) {
						daoInstance.save(c);
					}
				}
			}
		} catch (JAXBException je) {
			je.printStackTrace();
		}
	}
}