package com.axonactive.jpa.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "health_insurance")
@NamedQuery(name = HealthInsurance.GET_ALL_HEALTH_INSURANCE_BY_EMPLOYEE_ID, query = "from HealthInsurance h where h.employee.id =:employeeId")
@NamedQuery(name = HealthInsurance.GET_HEALTH_INSURANCE_BY_EMPLOYEE_ID_AND_HEALTH_INSURANCE_ID, query = "from HealthInsurance h where h.employee.id=:employeeId and h.id=:healthInsuranceId")
public class HealthInsurance {
    private static final String QUALIFIER = "com.axonactive.jpa.entities";
    public static final String GET_ALL_HEALTH_INSURANCE_BY_EMPLOYEE_ID = QUALIFIER + "getAllHealthInsuranceByEmployeeId";
    public static final String GET_HEALTH_INSURANCE_BY_EMPLOYEE_ID_AND_HEALTH_INSURANCE_ID = QUALIFIER + "getHealthInsuranceByEmployeeIdAndHealthInsuranceId";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String address;

    @Column(name = "register_hospital", nullable = false)
    private String registerHospital;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

}
