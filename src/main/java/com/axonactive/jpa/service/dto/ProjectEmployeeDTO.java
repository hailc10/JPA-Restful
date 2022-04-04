package com.axonactive.jpa.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProjectEmployeeDTO {
    String name;
    String area;
    List<EmployeeDTO> employees;
    Integer numberOfEmployee;
    Integer numberOfHour;
    Double totalSalary;

}
