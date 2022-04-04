package com.axonactive.jpa.service.mapper;

import com.axonactive.jpa.entities.Department;
import com.axonactive.jpa.service.dto.DepartmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    DepartmentDTO DepartmentToDepartmentDTO(Department department);
    List<DepartmentDTO> DepartmentToDepartmentDTOs(List<Department> departments);
}
