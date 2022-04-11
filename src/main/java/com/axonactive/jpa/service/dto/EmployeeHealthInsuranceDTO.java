package com.axonactive.jpa.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeHealthInsuranceDTO {
    private EmployeeDTO employee;
    private List<HealthInsuranceDTO> healthInsurances;
}
