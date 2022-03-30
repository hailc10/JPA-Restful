package com.axonactive.jpa.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DepartmentProjectDTO {
    private DepartmentDTO department;
    private List<ProjectDTO> projects;
}
