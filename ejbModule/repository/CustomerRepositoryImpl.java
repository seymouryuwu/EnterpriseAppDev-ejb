package repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import repository.entity.Contact;
import repository.entity.Customer;
import repository.entity.IndustryType;
import repository.entity.User;

@Stateless
public class CustomerRepositoryImpl implements CustomerRepository{
	@PersistenceContext(unitName = "Assignment-ejbPU")
    private EntityManager entityManager;
	
	@Override
	public void createCustomer(Customer customer) {
		entityManager.persist(customer);
	}

	@Override
	public List<Customer> findCustomersByConnectUser(User connectUser) {
		Query query = entityManager.createNamedQuery(Customer.GET_BY_USER);
		query.setParameter("username", connectUser.getUsername());
		return query.getResultList();
	}

	@Override
	public Customer findCustomerByCustomerId(long customerId) {
		Customer customer = entityManager.find(Customer.class, customerId);
		return customer;
	}

	@Override
	public List<Customer> findCustomerByIndustryType(IndustryType industryType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer findCustomerByContact(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Customer> findAllCustomers() {
		return entityManager.createNamedQuery(Customer.GET_ALL_QUERY_NAME).getResultList();
	}

	@Override
	public void updateCustomer(Customer customer) {
		try {
			entityManager.merge(customer);
		} catch (Exception ex) {
			
		}
	}

	@Override
	public void removeCustomer(long customerId) {
		Customer customer = this.findCustomerByCustomerId(customerId);
		if (customer != null) {
			entityManager.remove(customer);
		}
	}

	@Override
	public void addCustomer(Customer customer) {
		List<Customer> customers = findAllCustomers(); 
		if (customers.isEmpty()) {
			customer.setCustomerId(1);
		} else {
			customer.setCustomerId(customers.get(0).getCustomerId() + 1);
		}
        
        entityManager.persist(customer);
	}
	

}
