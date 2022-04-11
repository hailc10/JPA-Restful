package com.axonactive.jpa.controller.request;

import com.axonactive.jpa.entities.ContactInfo;
import com.axonactive.jpa.enumerate.Nationality;
import com.axonactive.jpa.enumerate.NationalityAttributeConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Transient;

@Getter
@Setter
public class CustomerRequest {
    private String name;
    @Transient
    private int age;
    @Embedded
    private ContactInfo contactInfo;
    @Convert(converter = NationalityAttributeConverter.class)
    private Nationality nationality;


}
