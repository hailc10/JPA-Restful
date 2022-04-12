package com.axonactive.jpa.service.impl;

import com.axonactive.jpa.controller.request.CustomerRequest;
import com.axonactive.jpa.entities.ContactInfo;
import com.axonactive.jpa.entities.Customer;
import com.axonactive.jpa.service.CustomerService;
import javassist.NotFoundException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

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
    public Customer addCustomer(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setName(customerRequest.getName());
        customer.setContactInfo(customerRequest.getContactInfo());
        customer.setAge(customerRequest.getAge());
        customer.setNationality(customerRequest.getNationality());
        em.persist(customer);
        return customer;
    }

    @Override
    public void deleteCustomer(int customerId) {
        Customer customer = getCustomerById(customerId);
        if (Objects.isNull(customer)){
            System.out.println("null customer");
        }else {
            em.remove(customer);
        }
    }

    @Override
    public Customer updateCustomer(int customerId, CustomerRequest customerRequest) {
        Customer customer = getCustomerById(customerId);
        customer.setName(customerRequest.getName());
        customer.setAge(customerRequest.getAge());
        customer.setContactInfo(customerRequest.getContactInfo());
        customer.setNationality(customerRequest.getNationality());
        em.merge(customer);
        return customer;
    }


}
