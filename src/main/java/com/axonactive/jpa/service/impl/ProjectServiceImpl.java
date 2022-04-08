package com.axonactive.jpa.service.impl;

import com.axonactive.jpa.controller.request.ProjectRequest;
import com.axonactive.jpa.entities.Assignment;
import com.axonactive.jpa.entities.Project;
import com.axonactive.jpa.service.DepartmentService;
import com.axonactive.jpa.service.ProjectService;
import com.axonactive.jpa.service.dto.*;
import com.axonactive.jpa.service.mapper.DepartmentMapper;
import com.axonactive.jpa.service.mapper.EmployeeMapper;
import com.axonactive.jpa.service.mapper.ProjectMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RequestScoped
@Transactional
public class ProjectServiceImpl implements ProjectService {

    @PersistenceContext(unitName = "jpa")
    private EntityManager em;

    @Inject
    private DepartmentService departmentService;

    @Inject
    DepartmentMapper departmentMapper;

    @Inject
    ProjectMapper projectMapper;

    @Inject
    EmployeeMapper employeeMapper;

    private Project getProjectByIdDepartmentIdAndProjectIdHelper(int departmentId, int projectId){
        TypedQuery<Project> namedQuery = em.createNamedQuery(Project.GET_PROJECT_BY_ID, Project.class);
        namedQuery.setParameter("departmentId",departmentId);
        namedQuery.setParameter("projectId",projectId);
        return namedQuery.getSingleResult();

    }

    @Override
    public Project getProjectByIdHepler(int projectId){
        return em.createQuery("from Project p where p.id ="+projectId,Project.class).getSingleResult();
    }


    @Override
    public ProjectDTO getProjectById(int departmentId, int projectId) {
        return ProjectMapper.INSTANCE.ProjectToProjectDto(getProjectByIdDepartmentIdAndProjectIdHelper(departmentId,projectId));
    }

    @Override
    public List<ProjectDTO> getAllProjectByDepartment(int departmentId) {
        TypedQuery<Project> namedQuery = em.createNamedQuery(Project.GET_ALL, Project.class);
        namedQuery.setParameter("departmentId",departmentId);
        List<Project> projectList = namedQuery.getResultList();

        return ProjectMapper.INSTANCE.ProjectsToProjectDtos(projectList);
    }


    @Override
    public ProjectDTO addProject(int departmentId, ProjectRequest projectRequest) {
        Project project = new Project();
        project.setArea(projectRequest.getArea());
        project.setName(projectRequest.getName());
        project.setDepartment(departmentService.getDepartmentById(departmentId));
        em.persist(project);
        return ProjectMapper.INSTANCE.ProjectToProjectDto(project);
    }

    @Override
    public void deleteProject(int departmentId, int projectId) {
        Project project = getProjectByIdDepartmentIdAndProjectIdHelper(departmentId, projectId);
        if(Objects.nonNull(project)){
            em.remove(project);
        }

    }

    @Override
    public ProjectDTO updateProject(int departmentId, int projectId, ProjectRequest projectRequest) {
        Project project = getProjectByIdDepartmentIdAndProjectIdHelper(departmentId, projectId);
        project.setArea(projectRequest.getArea());
        project.setName(projectRequest.getName());
        em.merge(project);

        return ProjectMapper.INSTANCE.ProjectToProjectDto(project);
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


    //lấy danh sách nhân viên làm việc trong project, tổng số lượng nhân viên, tổng số lượng tgian, tổng lương phải trả
    public List<ProjectEmployeeDTO> getEmployeeInProject(){
        List<Assignment> assignments = em.createQuery("from Assignment", Assignment.class).getResultList();
        return assignments.stream()
                .collect(Collectors.groupingBy(Assignment::getProject))
                .entrySet()
                .stream()
                .filter(e->e.getKey().getArea().equals("Vietnam"))
                .map((e)->{
                    Project project = e.getKey();
                    List<Assignment> assignmentList = e.getValue();
                    List<EmployeeDTO> employeeDTOS = employeeMapper.EmployeesToEmployeeDtos(assignmentList
                            .stream()
                            .map(Assignment::getEmployee)
                            .distinct()
                            .collect(Collectors.toList()));
                    Double totalSalary = assignmentList.stream().reduce(0.0, (acc, cur) ->
                            acc + (cur.getEmployee().getSalary()/160) * cur.getNumofhour()
                    ,Double::sum);
                    int totalNumberOfHour = assignmentList.stream().mapToInt(Assignment::getNumofhour).sum();
                    return new ProjectEmployeeDTO(project.getName(),project.getArea(),employeeDTOS,employeeDTOS.size(),totalNumberOfHour,totalSalary);
                })
                .collect(Collectors.toList());
    }

}
