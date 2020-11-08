package repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import repository.entity.Contact;
import repository.entity.Customer;

@Stateless
public class ContactRepositoryImpl implements ContactRepository{
	@PersistenceContext(unitName = "Assignment-ejbPU")
    private EntityManager entityManager;
	

	@Override
	public void addContact(Contact contact) {
		List<Contact> contacts = findAllContacts(); 
		if (contacts.isEmpty()) {
			contact.setContactId(1);
		} else {
			contact.setContactId(contacts.get(0).getContactId() + 1);
		}
        
        entityManager.persist(contact);
		
	}

	@Override
	public List<Contact> findAllContacts() {
		return entityManager.createNamedQuery(Contact.GET_ALL_QUERY_NAME).getResultList();
	}
	
	@Override
	public List<Contact> findContactsByCustomer(Customer customer) {
		Query query = entityManager.createNamedQuery(Contact.GET_BY_CUSTOMER);
		query.setParameter("customerId", customer.getCustomerId());
		return query.getResultList();
	}

	@Override
	public Contact findContactByContactId(long contactId) {
		Contact contact = entityManager.find(Contact.class, contactId);
		return contact;
	}

	@Override
	public void updateContact(Contact contact) {
		try {
			entityManager.merge(contact);
		} catch (Exception ex) {
			
		}
	}

	@Override
	public void removeContact(long contactId) {
		Contact contact = this.findContactByContactId(contactId);
		if (contact != null) {
			entityManager.remove(contact);
		}
		
	}

	@Override
	public void deleteContactsByCustomer(Customer customer) {
		// TODO Auto-generated method stub
		
	}

}
