package com.axonactive.jpa.entities;


import com.axonactive.jpa.enumerate.AddressType;
import com.axonactive.jpa.enumerate.Province;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.Api;
import lombok.*;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "address")
@ToString
@JsonDeserialize(using = LocalDateDeserializer.class)
@JsonSerialize(using = LocalDateSerializer.class)
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
@Api(value = "address")


@NamedQuery(name = Address.GET_ALL_ADDRESS_BY_EMPLOYEE_ID, query = "from Address a where a.employee.id=: employeeId")
@NamedQuery(name = Address.GET_ADDRESS_BY_EMPLOYEE_ID_AND_ADDRESS_ID, query = "from Address a where a.employee.id=: employeeId and a.id=:addressId")
public class Address {
    private static final String QUALIFIER = "com.axonactive.jpa.entities";
    public static final String GET_ALL_ADDRESS_BY_EMPLOYEE_ID = QUALIFIER + "getAllAddressByEmployeeId";
    public static final String GET_ADDRESS_BY_EMPLOYEE_ID_AND_ADDRESS_ID = QUALIFIER + "getAddressByEmployeeIdAndAddressId";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "district_level", nullable = false)
    @NotNull
    private String districtLevel;

    @NotNull
    @Column(name = "province_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private Province provinceLevel;

    @NotNull
    @Column(name = "address_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @CreationTimestamp
    @Column(name = "created_date",nullable = false)
    private LocalDate createdDate;

    @UpdateTimestamp
    @Column(name = "modified_date",nullable = false)
    private LocalDate modifiedDate;


}
