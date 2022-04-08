package com.axonactive.jpa.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ContactInfo {
    private String email;
    @Column(name = "phone_number",nullable = false)
    private String phoneNumber;
    private String skype;
}
