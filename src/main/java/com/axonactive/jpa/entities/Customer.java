package com.axonactive.jpa.entities;

import com.axonactive.jpa.enumerate.Nationality;
import com.axonactive.jpa.enumerate.NationalityAttributeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name = Customer.GET_ALL, query = "from Customer c where c.id = :customerId")
public class Customer {
    private static final String QUALIFIER = "com.axonactive.jpa.entities";
    public static final String GET_ALL = "getAllCustomer";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Transient
    private int age;

    @Embedded
    private ContactInfo contactInfo;

    @Convert(converter = NationalityAttributeConverter.class)
    private Nationality nationality;
}
