package com.axonactive.jpa.service.impl;

import com.axonactive.jpa.controller.request.RelativeRequest;
import com.axonactive.jpa.entities.Employee;
import com.axonactive.jpa.entities.Project;
import com.axonactive.jpa.entities.Relative;
import com.axonactive.jpa.enumerate.Relationship;
import com.axonactive.jpa.service.EmployeeService;
import com.axonactive.jpa.service.RelativeService;
import com.axonactive.jpa.service.dto.*;
import com.axonactive.jpa.service.mapper.DepartmentMapper;
import com.axonactive.jpa.service.mapper.EmployeeMapper;
import com.axonactive.jpa.service.mapper.ProjectMapper;
import com.axonactive.jpa.service.mapper.RelativeMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static com.axonactive.jpa.constant.Constant.EMPLOYEE_ID_PARAMETER_NAME_SQL;
import static com.axonactive.jpa.constant.Constant.RELATIVE_ID_PARAMETER_NAME_SQL;

@Transactional
@RequestScoped
public class RelativeServiceImpl implements RelativeService {

    @PersistenceContext(unitName = "jpa")
    EntityManager em;

    @Inject
    RelativeMapper relativeMapper;

    @Inject
    EmployeeService employeeService;

    @Inject
    EmployeeMapper employeeMapper;

    @Inject
    DepartmentMapper departmentMapper;

    @Inject
    ProjectMapper projectMapper;

    @Override
    public List<RelativeDTO> getAllRelativeByEmployeeId(int employeeId) {
        TypedQuery<Relative> namedQuery = em.createNamedQuery(Relative.GET_ALL_RELATIVE_BY_EMPLOYEE_ID, Relative.class);
        namedQuery.setParameter(EMPLOYEE_ID_PARAMETER_NAME_SQL, employeeId);
        return relativeMapper.RelativesToRelativeDtos(namedQuery.getResultList());
    }

    @Override
    public RelativeDTO getRelativeByEmployeeIdAndRelativeId(int employeeId, int relativeId) {
        TypedQuery<Relative> namedQuery = em.createNamedQuery(Relative.GET_RELATIVE_BY_EMPLOYEE_ID_AND_RELATIVE_ID, Relative.class);
        namedQuery.setParameter(EMPLOYEE_ID_PARAMETER_NAME_SQL, employeeId);
        namedQuery.setParameter(RELATIVE_ID_PARAMETER_NAME_SQL, relativeId);
        return relativeMapper.RelativeToRelativeDto(namedQuery.getSingleResult());
    }

    @Override
    public RelativeDTO addRelativeByEmployeeId(int employeeId, RelativeRequest relativeRequest) {
        Employee employee = em.createQuery("from Employee e where e.id = " + employeeId, Employee.class).getSingleResult();
        Relative relative = new Relative();
        relative.setEmployee(employee);
        relative.setFullName(relativeRequest.getFullName());
        relative.setGender(relativeRequest.getGender());
        relative.setPhoneNumber(relativeRequest.getPhoneNumber());
        relative.setRelationship(relativeRequest.getRelationship());
        em.persist(relative);
        return relativeMapper.RelativeToRelativeDto(relative);
    }

    @Override
    public void deleteRelativeByEmployeeIdAndRelativeId(int employeeId, int relativeId) {
        Relative relative = getRelativeByEmployeeIdAndRelativeIdHelper(employeeId, relativeId);
        if (Objects.nonNull(relative)) {
            em.remove(relative);
        }
    }

    @Override
    public RelativeDTO updateRelativeByEmployeeIdAndRelativeId(int employeeId, int relativeId, RelativeRequest relativeRequest) {
        Relative relative = getRelativeByEmployeeIdAndRelativeIdHelper(employeeId, relativeId);
        relative.setFullName(relativeRequest.getFullName());
        relative.setGender(relativeRequest.getGender());
        relative.setPhoneNumber(relativeRequest.getPhoneNumber());
        relative.setRelationship(relativeRequest.getRelationship());
        em.merge(relative);
        return relativeMapper.RelativeToRelativeDto(relative);
    }

    private Relative getRelativeByEmployeeIdAndRelativeIdHelper(int employeeId, int relativeId) {
        return em.createQuery("from Relative r where r.employee.id=:" + EMPLOYEE_ID_PARAMETER_NAME_SQL +
                        " and r.id=:" + RELATIVE_ID_PARAMETER_NAME_SQL, Relative.class)
                .setParameter(EMPLOYEE_ID_PARAMETER_NAME_SQL, employeeId)
                .setParameter(RELATIVE_ID_PARAMETER_NAME_SQL, relativeId)
                .getSingleResult();
    }

    public List<EmployeeRelativeDTO> getRelativeOfEmployee(){
        return getAllRelatives().stream()
                .collect(Collectors.groupingBy(Relative::getEmployee))
                .entrySet()
                .stream()
                .map(employeeRelativesEntry -> {
                    EmployeeDTO employeeDTO = employeeMapper.EmployeeToEmployeeDto(employeeRelativesEntry.getKey());
                    List<RelativeDTO> relativeDTOS = relativeMapper.RelativesToRelativeDtos(employeeRelativesEntry.getValue());
                    return new EmployeeRelativeDTO(employeeDTO, relativeDTOS);
                }).collect(Collectors.toList());
    }

    private List<Relative> getAllRelatives() {
        return em.createQuery("from Relative", Relative.class).getResultList();
    }

    public List<DepartmentProjectDTO> getProjectOfDepartment(){
        return getAllProjects().stream()
                .collect(Collectors.groupingBy(Project::getDepartment))
                .entrySet()
                .stream()
                .map(departmentProjectsEntry ->{
                    DepartmentDTO departmentDTO = departmentMapper.DepartmentToDepartmentDTO(departmentProjectsEntry.getKey());
                    List<ProjectDTO> projectDTOS = projectMapper.ProjectsToProjectDtos(departmentProjectsEntry.getValue());
                    return new DepartmentProjectDTO(departmentDTO, projectDTOS);
                }).collect(Collectors.toList());
    }

    private List<Project> getAllProjects(){
        return em.createQuery("from Project", Project.class).getResultList();
    }



    //employeeDTO +  relative where -> FATHER -> MOTHER -> ANYBODY ELSE
    public Optional<Relative> getEmergencyRelative(List<Relative> relativeList){
        Optional<Relative> emergencyRelative = getRelative(relativeList, Relationship.FATHER);
        if (emergencyRelative.isEmpty()){
            emergencyRelative = getRelative(relativeList, Relationship.MOTHER);
        }
        if (emergencyRelative.isEmpty()){
            emergencyRelative=relativeList.stream().findAny();
        }
        return emergencyRelative;
    }

    private Optional<Relative> getRelative(List<Relative> relativeList, Relationship relationship) {
        return relativeList.stream().filter(r ->
                r.getRelationship().equals(relationship)
        ).findAny();
    }

    public List<EmployeeRelativeDTO> getEmployeeEmergencyRelative(){
        return getAllRelatives().stream()
                .collect(Collectors.groupingBy(Relative::getEmployee))
                .entrySet()
                .stream()
                .map(employeeRelativesEntry -> {
                    EmployeeDTO employeeDTO = employeeMapper.EmployeeToEmployeeDto(employeeRelativesEntry.getKey());
                    List<RelativeDTO> relativeDTOS = new ArrayList<>();
                    Optional<Relative> emergencyRelative = getEmergencyRelative(employeeRelativesEntry.getValue());
                    if (!emergencyRelative.isEmpty()){
                        relativeDTOS.add(relativeMapper.RelativeToRelativeDto(emergencyRelative.get()));
                    }
                    return new EmployeeRelativeDTO(employeeDTO, relativeDTOS);
                }).collect(Collectors.toList());
    }

}
