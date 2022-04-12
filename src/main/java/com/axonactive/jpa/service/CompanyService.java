package com.axonactive.jpa.service;

import com.axonactive.jpa.service.dto.*;

import java.util.List;

public interface CompanyService {
    List<EmployeeAddressDTO> getAddressOfEmployees();
    List<EmployeeDTO> getEmpNotInProject();
    List<EmployeeDTO> getEmpNotInProjectJPQL();
    List<EmployeeDTO> getEmpDontHaveHealthInsurance();
    List<EmployeeDTO> getEmployeeHasNoHealthInsurance();
    List<EmployeeDTO> getEmpDontHaveAddress();
    List<EmployeeDTO> getEmployeesWorkOnMoreThanProject();
    List<EmployeeDTO> getEmployeeWorkOnOtherDepartmentProject();
    List<EmployeeHealthInsuranceDTO> getHealthInsuranceOfEmployee();
    List<DepartmentProjectDTO> getProjectOfDepartment();
    List<ProjectEmployeeDTO> getEmployeeInProject();
    List<EmployeeRelativeDTO> getRelativeOfEmployee();
    List<EmployeeRelativeDTO> getEmployeeEmergencyRelative();
}
