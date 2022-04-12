package com.axonactive.jpa.service.impl;

import com.axonactive.jpa.entities.*;
import com.axonactive.jpa.service.CompanyService;
import com.axonactive.jpa.service.EmployeeService;
import com.axonactive.jpa.service.dto.*;
import com.axonactive.jpa.service.mapper.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Transactional
public class CompanyServiceImpl implements CompanyService {
    @PersistenceContext(unitName = "jpa")
    EntityManager em;
    @Inject
    EmployeeService employeeService;

    @Inject
    AddressMapper addressMapper;

    @Inject
    EmployeeMapper employeeMapper;

    @Inject
    HealthInsuranceMapper healthInsuranceMapper;

    @Inject
    DepartmentMapper departmentMapper;

    @Inject
    ProjectMapper projectMapper;

    //lấy danh sách address theo emp
    public List<EmployeeAddressDTO> getAddressOfEmployees(){
        List<Address> addresses = em.createQuery("from Address", Address.class).getResultList();
        return addresses.stream()
                .collect(Collectors.groupingBy(Address::getEmployee))
                .entrySet()
                .stream()
                .map(e->{
                    EmployeeDTO employeeDTO = employeeMapper.EmployeeToEmployeeDto(e.getKey());
                    List<AddressDTO> addressDTOS = addressMapper.AddressToAddressDtos(e.getValue());
                    return new EmployeeAddressDTO(employeeDTO,addressDTOS);
                }).collect(Collectors.toList());
    }

    //lấy danh sách các emp chưa làm project nào - java 8
    public List<EmployeeDTO> getEmpNotInProject() {
        List<Assignment> assignments = em.createQuery("from Assignment", Assignment.class).getResultList();
        List<Employee> employeeList = assignments
                .stream()
                .map(assignment -> assignment.getEmployee())
                .distinct()
                .collect(Collectors.toList());
        return employeeMapper.EmployeesToEmployeeDtos(em.createQuery("from Employee", Employee.class).getResultList().stream().filter(employee -> !employeeList.contains(employee)).collect(Collectors.toList()));
    }

    //lấy danh sách các emp chưa làm project nào - jpa query
    public List<EmployeeDTO> getEmpNotInProjectJPQL() {
        return employeeMapper.EmployeesToEmployeeDtos(
                em.createQuery("from Employee e where e.id NOT IN (SELECT a.employee.id FROM Assignment a)").getResultList());
    }

    //lấy danh sách các emp chưa có thông tin bảo hiểm
    public List<EmployeeDTO> getEmpDontHaveHealthInsurance() {
        List<HealthInsurance> healthInsurances = em.createQuery("from HealthInsurance", HealthInsurance.class).getResultList();
        List<Employee> employeeList = healthInsurances
                .stream()
                .map(healthInsurance -> healthInsurance.getEmployee())
                .distinct()
                .collect(Collectors.toList());
        return employeeMapper.EmployeesToEmployeeDtos(em.createQuery("from Employee", Employee.class).getResultList().stream().filter(employee -> !employeeList.contains(employee)).collect(Collectors.toList()));
    }

    //lấy danh sách các emp chưa có thông tin bảo hiểm - jpa query
    @Override
    public List<EmployeeDTO> getEmployeeHasNoHealthInsurance() {
        return em.createQuery("SELECT e from Employee e WHERE e.id NOT IN (SELECT h.employee.id FROM HealthInsurance h )").getResultList();
    }

    //lấy danh sách các emp chưa có thông tin hộ khẩu
    public List<EmployeeDTO> getEmpDontHaveAddress() {
        List<Address> addresses = em.createQuery("from Address", Address.class).getResultList();
        List<Employee> employeeList = addresses
                .stream()
                .map(address -> address.getEmployee())
                .distinct()
                .collect(Collectors.toList());
        return employeeMapper.EmployeesToEmployeeDtos(em.createQuery("from Employee", Employee.class).getResultList().stream().filter(employee -> !employeeList.contains(employee)).collect(Collectors.toList()));
    }

    //lấy danh sách emp làm việc trong nhiều hơn 1 project
    public List<EmployeeDTO> getEmployeesWorkOnMoreThanProject() {
        List<Assignment> assignments = em.createQuery("from Assignment", Assignment.class).getResultList();
        return employeeMapper.EmployeesToEmployeeDtos(assignments.stream()
                .collect(Collectors.groupingBy(Assignment::getEmployee))
                .entrySet()
                .stream()
                .filter(employeeListEntry ->
                        employeeListEntry.getValue()
                                .stream()
                                .map(assignment -> assignment.getProject().getDepartment().getId())
                                .distinct().count() > 1
                )
                .map(employeeListEntry -> employeeListEntry.getKey())
                .collect(Collectors.toList()));
    }

    //lấy danh sách emp làm việc trong project của dept khác - jpa query
    @Override
    public List<EmployeeDTO> getEmployeeWorkOnOtherDepartmentProject() {
        return employeeMapper.EmployeesToEmployeeDtos(
                em.createQuery("SELECT em FROM Employee em WHERE em.department.id IN " +
                        "(SELECT DISTINCT e.department.id " +
                        "FROM Employee e, Assignment a, Project p " +
                        "WHERE e.id = a.employee.id and a.project.id = p.id and " +
                        "e.id = em.id and p.department.id != em.department.id)").getResultList());
    }

    //lấy danh sách health insurance theo emp
    public List<EmployeeHealthInsuranceDTO> getHealthInsuranceOfEmployee(){
        List<HealthInsurance> healthInsurances = em.createQuery("from HealthInsurance", HealthInsurance.class).getResultList();
        return healthInsurances.stream()
                .collect(Collectors.groupingBy(HealthInsurance::getEmployee))
                .entrySet()
                .stream()
                .map(e->{
                    EmployeeDTO employeeDTO = employeeMapper.EmployeeToEmployeeDto(e.getKey());
                    List<HealthInsuranceDTO> healthInsuranceDTOS = healthInsuranceMapper.HealthInsurancesToHealthInsuranceDTOs(e.getValue());
                    return new EmployeeHealthInsuranceDTO(employeeDTO, healthInsuranceDTOS);
                })
                .collect(Collectors.toList());
    }

    //lấy danh sách các project theo department
    public List<DepartmentProjectDTO> getProjectOfDepartment(){
        return em.createQuery("from Project", Project.class).getResultList()
                .stream()
                .collect(Collectors.groupingBy(Project::getDepartment))
                .entrySet()
                .stream()
                .map(e->{
                    DepartmentDTO departmentDTO = departmentMapper.DepartmentToDepartmentDTO(e.getKey());
                    List<ProjectDTO> projectDTOS = projectMapper.ProjectsToProjectDtos(e.getValue());
                    return new DepartmentProjectDTO(departmentDTO,projectDTOS);
                }).collect(Collectors.toList());
    }
}
