package com.axonactive.jpa.service.impl;

import com.axonactive.jpa.controller.request.RelativeRequest;
import com.axonactive.jpa.entities.Department;
import com.axonactive.jpa.entities.Employee;
import com.axonactive.jpa.entities.HealthInsurance;
import com.axonactive.jpa.entities.Relative;
import com.axonactive.jpa.enumerate.Gender;
import com.axonactive.jpa.enumerate.Relationship;
import com.axonactive.jpa.service.EmployeeService;
import com.axonactive.jpa.service.dto.RelativeDTO;
import com.axonactive.jpa.service.mapper.RelativeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelativeServiceImplTest {
    @InjectMocks
    RelativeServiceImpl relativeService;

    @Mock
    RelativeMapper relativeMapper;

    @Mock
    EmployeeService employeeService;

    @Mock
    EntityManager entityManager;

    @Mock
    TypedQuery<Relative> typedQuery;

    Employee employee;
    Relative relative1;
    Relative relative2;
    List<Relative> relativeList;

    @BeforeEach
    public void init(){
        employee = new Employee(3,"A","B","C",
                LocalDate.of(2020,10,10),
                Gender.FEMALE,1000, new Department(8,"DeptB",LocalDate.of(2018,1,10)));

        relative1 = new Relative(1,employee,"X",Gender.FEMALE,"0909111222", Relationship.MOTHER);
        relative2 = new Relative(2,employee,"Y",Gender.MALE,"0909222333",Relationship.FATHER);
        relativeList = Arrays.asList(relative1,relative2);
    }

    @Test
    void getRelativeByEmployeeId_RightEmployeeId_ShouldReturnRelativeListOfEmployee(){
        int relativeId = 2;
        List<Relative> expectedRelativeList = this.relativeList.stream()
                .filter(relative -> relative.getEmployee().getId() == relativeId).collect(Collectors.toList());
        when(entityManager.createNamedQuery(Relative.GET_ALL_RELATIVE_BY_EMPLOYEE_ID,Relative.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedRelativeList);

        List<RelativeDTO> expectedRelativeDTOList = new ArrayList<>();
        relativeList.forEach(relative -> {
            expectedRelativeDTOList.add(relativeMapper.RelativeToRelativeDto(relative));
        });

        when(relativeMapper.RelativesToRelativeDtos(expectedRelativeList))
                .thenReturn(expectedRelativeDTOList);
        List<RelativeDTO> actualRelativeDTOList = relativeService.getAllRelativeByEmployeeId(relativeId);
        assertEquals(expectedRelativeDTOList,actualRelativeDTOList);
    }

    @Test
    void getRelativeByEmployeeId_WrongEmployeeId_ShouldReturnEmptyList(){
        int employeeId = 3;
        List<Relative> expectedRelativeList = this.relativeList.stream()
                .filter(relative -> relative.getEmployee().getId() == employeeId).collect(Collectors.toList());
        when(entityManager.createNamedQuery(Relative.GET_ALL_RELATIVE_BY_EMPLOYEE_ID,Relative.class))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedRelativeList);

        //empty list
        List<RelativeDTO> expectedRelativeDTOList = new ArrayList<>();
        when(relativeMapper.RelativesToRelativeDtos(expectedRelativeList))
                .thenReturn(expectedRelativeDTOList);
        List<RelativeDTO> actualRelativeDTOList = relativeService.getAllRelativeByEmployeeId(employeeId);
        assertEquals(expectedRelativeDTOList,actualRelativeDTOList);
    }

    @Test
    void getRelativeByEmployeeIdAndRelativeId_RightEmployeeIdAndRelativeId_ShouldReturnRelative(){
        int employeeId = 3;
        int relativeId = 2;
        Relative expectedRelative = relativeList.stream()
                .filter(relative -> relative.getEmployee().getId() == employeeId && relative.getId() == relativeId)
                .findFirst().get();
        when(entityManager.createNamedQuery(Relative.GET_RELATIVE_BY_EMPLOYEE_ID_AND_RELATIVE_ID, Relative.class))
                .thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(expectedRelative);
        RelativeDTO expectedRelativeDTO = relativeMapper.RelativeToRelativeDto(expectedRelative);
        when(relativeMapper.RelativeToRelativeDto(expectedRelative)).thenReturn(expectedRelativeDTO);
        RelativeDTO actualRelativeDTO = relativeService.getRelativeByEmployeeIdAndRelativeId(employeeId,relativeId);
        assertEquals(expectedRelativeDTO,actualRelativeDTO);
    }

    @Test
    void getRelativeByEmployeeIdAndRelativeId_RightEmployeeIdAndWrongRelativeId_ShouldReturnNull(){
        when(entityManager.createNamedQuery(Relative.GET_RELATIVE_BY_EMPLOYEE_ID_AND_RELATIVE_ID,Relative.class))
                .thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(null);
        RelativeDTO actualRelativeDTO = relativeService.getRelativeByEmployeeIdAndRelativeId(anyInt(),anyInt());
        assertNull(actualRelativeDTO);
    }

//    @Test
//    void addRelative_rightRelativeDTO_ShouldReturnRelative(){
//        RelativeRequest relativeRequest = new RelativeRequest();
//        relativeRequest.setFullName("A");
//        relativeRequest.setPhoneNumber("0909111222");
//        relativeRequest.setGender(Gender.FEMALE);
//        relativeRequest.setRelationship(Relationship.MOTHER);
//
//        int employeeId = 3;
//
//        when(employeeService.getEmployeeByIdFromDataBase(employeeId))
//                .thenReturn(employee);
//
//        Relative relativeToBeAdd = new Relative();
//        relativeToBeAdd.setFullName(relativeRequest.getFullName());
//        relativeToBeAdd.setPhoneNumber(relativeRequest.getPhoneNumber());
//        relativeToBeAdd.setGender(relativeRequest.getGender());
//        relativeToBeAdd.setRelationship(relativeRequest.getRelationship());
//        RelativeDTO returnedRelativeDTO = relativeService.addRelativeByEmployeeId(employeeId,relativeRequest);
//        assertEquals(relativeRequest,returnedRelativeDTO);
//        assertEquals(relativeToBeAdd.getEmployee().getFirstName(),employee.getFirstName());
//        verify(entityManager).persist(relativeToBeAdd);
//    }


//    @Test
//    void deleteRelativeByEmployeeIdAndRelativeId_rightId_ShouldDeleteRelative(){
//        int employeeId = 3;
//        int relativeId = 1;
//        when(entityManager.createNamedQuery(Relative.GET_RELATIVE_BY_EMPLOYEE_ID_AND_RELATIVE_ID,Relative.class))
//                .thenReturn(typedQuery);
//        when(typedQuery.getSingleResult()).thenReturn(relative1);
//        relativeService.deleteRelativeByEmployeeIdAndRelativeId(employeeId,relativeId);
//        verify(entityManager).remove(relative1);
//    }
//
//    @Test
//    void deleteRelativeByEmployeeIdAndRelativeId_wrongRelativeId_ShouldNotDeleteRelative(){
//        int employeeId = 3;
//        int relativeId = 10;
//        when(entityManager.createNamedQuery(Relative.GET_RELATIVE_BY_EMPLOYEE_ID_AND_RELATIVE_ID,Relative.class))
//                .thenReturn(typedQuery);
//        when(typedQuery.getSingleResult()).thenReturn(null);
//        relativeService.deleteRelativeByEmployeeIdAndRelativeId(employeeId,relativeId);
//        verify(entityManager,never()).remove(relative1);
//    }
//
//    @Test
//    void updateRelative_rightRelativeId_ShouldReturnRelative(){
//        int employeeId = 3;
//        int relativeId = 2;
//        RelativeRequest relativeRequest = new RelativeRequest();
//        relativeRequest.setFullName("A");
//        relativeRequest.setPhoneNumber("0909111222");
//        relativeRequest.setGender(Gender.FEMALE);
//        relativeRequest.setRelationship(Relationship.MOTHER);
//
//        when(entityManager.createNamedQuery(Relative.GET_RELATIVE_BY_EMPLOYEE_ID_AND_RELATIVE_ID,Relative.class))
//                .thenReturn(typedQuery);
//        when(typedQuery.getSingleResult()).thenReturn(relative2);
//
//        Relative expectedRelative = relative2;
//
//        expectedRelative.setFullName(relativeRequest.getFullName());
//        expectedRelative.setPhoneNumber(relativeRequest.getPhoneNumber());
//        expectedRelative.setGender(relativeRequest.getGender());
//        expectedRelative.setRelationship(relativeRequest.getRelationship());
//
//        RelativeRequest returnedRelativeRequest = relativeService.updateRelativeByEmployeeIdAndRelativeId(employeeId,relativeId, relativeRequest);
////        assertEquals(relativeRequest,returnedRelativeRequest);
//        verify(entityManager).merge(expectedRelative);
//    }


}
