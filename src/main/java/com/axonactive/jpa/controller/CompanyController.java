package com.axonactive.jpa.controller;

import com.axonactive.jpa.service.EmployeeService;
import com.axonactive.jpa.service.impl.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/statistic")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyController {
    @Inject
    EmployeeServiceImpl employeeService;

    @Inject
    RelativeServiceImpl relativeService;

    @Inject
    ProjectServiceImpl projectService;

    @Inject
    HealthInsuranceServiceImpl healthInsuranceService;

    @Inject
    AddressServiceImpl addressService;

    @GET
    public Response getAllEmployeeGroupByDepartment(){
        return Response.ok(employeeService.getAllEmployeeGroupByDepartment()).build();
    }

    @GET
    @Path("/employeebybirthmonth/{month}")
    public Response getEmployeeByBirthMonth(@PathParam("month") int month){
        return Response.ok(employeeService.getEmployeeByBirthMonth(month)).build();
    }

    @GET
    @Path("relativeofemployee")
    public Response getRelativeOfEmployee(){
        return Response.ok(relativeService.getRelativeOfEmployee()).build();
    }

    @GET
    @Path("employee-emergency")
    public Response getEmployeeEmergencyRelative(){
        return Response.ok(relativeService.getEmployeeEmergencyRelative()).build();
    }

    @GET
    @Path("projectofdepartment")
    public Response getProjectOfDepartment(){
        return Response.ok(projectService.getProjectOfDepartment()).build();
    }

    @GET
    @Path("employeeinproject")
    public Response getEmployeeInProject(){
        return Response.ok(projectService.getEmployeeInProject()).build();
    }

    @GET
    @Path("empnotinproject")
    public Response getEmpNotInProject(){
        return Response.ok(employeeService.getEmpNotInProject()).build();
    }

    @GET
    @Path("empinotherdepartmentproject")
    public Response getEmployeesWorkOnOtherDepartmentProject(){
        return Response.ok(employeeService.getEmployeesWorkOnOtherDepartmentProject()).build();
    }

    @GET
    @Path("healthinsuranceofemployees")
    public Response getHealthInsuranceOfEmployee(){
        return Response.ok(healthInsuranceService.getHealthInsuranceOfEmployee()).build();
    }

    @GET
    @Path("addressofemployees")
    public Response getAddressOfEmployees(){
        return Response.ok(addressService.getAddressOfEmployees()).build();
    }


}
