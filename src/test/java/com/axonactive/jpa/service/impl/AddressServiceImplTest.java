package com.axonactive.jpa.service.impl;

import com.axonactive.jpa.entities.Address;
import com.axonactive.jpa.entities.Department;
import com.axonactive.jpa.entities.Employee;
import com.axonactive.jpa.enumerate.AddressType;
import com.axonactive.jpa.enumerate.Gender;
import com.axonactive.jpa.service.mapper.AddressMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {
    @InjectMocks
    AddressServiceImpl addressService;

    @Mock
    EntityManager entityManager;

    @Mock
    AddressMapper addressMapper;

    @Mock
    TypedQuery<Address> typedQuery;

    Address addressA;
    Address addressB;

    @BeforeEach
    public void init(){
        addressA = Address.builder()
                .id(1)
                .districtLevel("A")
                .addressType(AddressType.TEMPORARY)
                .createdDate(LocalDate.of(2020,1,1))
                .modifiedDate(LocalDate.of(2020,2,2))
                .build();

        addressB = Address.builder()
                .id(2)
                .districtLevel("B")
                .addressType(AddressType.PERMANENT)
                .createdDate(LocalDate.of(2021,1,1))
                .modifiedDate(LocalDate.of(2021,2,2))
                .build();
    }


}
