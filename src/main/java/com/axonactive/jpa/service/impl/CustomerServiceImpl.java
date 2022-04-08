package com.axonactive.jpa.service.impl;

import com.axonactive.jpa.entities.ContactInfo;
import com.axonactive.jpa.entities.Customer;
import com.axonactive.jpa.service.CustomerService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @PersistenceContext(unitName = "jpa")
    EntityManager em;
    @Inject
    ContactInfo contactInfo;
    @Override
    public List<Customer> getAllCustomer() {
        return em.createQuery("from Customer",Customer.class).getResultList();
    }

    @Override
    public Customer getCustomerById(int customerId) {
        TypedQuery<Customer> typedQuery = em.createNamedQuery(Customer.GET_ALL,Customer.class);
        typedQuery.setParameter("customerId",customerId);
        return typedQuery.getSingleResult();
    }

    @Override
    public Customer addCustomer() {
        Customer customer = new Customer();
        customer.setName(customer.getName());
        customer.setAge(customer.getAge());
        customer.setNationality(customer.getNationality());
        customer.setContactInfo(contactInfo);
        em.persist(customer);
        return customer;
    }
}
