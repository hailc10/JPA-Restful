package com.axonactive.jpa.controller;

import com.axonactive.jpa.service.CompanyService;
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

    @Inject
    CompanyService companyService;

    //lấy danh sách address theo emp
    @GET
    @Path("addressofemployees")
    public Response getAddressOfEmployees(){
        return Response.ok(companyService.getAddressOfEmployees()).build();
    }

    //lấy danh sách các emp chưa làm project nào - java 8
    @GET
    @Path("empnotinproject")
    public Response getEmpNotInProject(){
        return Response.ok(companyService.getEmpNotInProject()).build();
    }

    //lấy danh sách các emp chưa làm project nào - jqpl
    @GET
    @Path("empnotinprojectjpql")
    public Response getEmpNotInProjectJPQL(){
        return Response.ok(companyService.getEmpNotInProjectJPQL()).build();
    }

    //lấy danh sách các emp chưa có thông tin bảo hiểm
    @GET
    @Path("empdonthavehealthinsurance")
    public Response getEmpDontHaveHealthInsurance(){
        return Response.ok(companyService.getEmpDontHaveHealthInsurance()).build();
    }

    //lấy danh sách các emp chưa có thông tin bảo hiểm - jpa query
    @GET
    @Path("/emphasnohealthinsurance")
    public Response getEmployeeHasNoHealthInsurance(){
        return Response.ok(companyService.getEmployeeHasNoHealthInsurance()).build();
    }

    //lấy danh sách các emp chưa có thông tin hộ khẩu
    @GET
    @Path("empdonthaveaddress")
    public Response getEmpDontHaveAddress(){
        return Response.ok(companyService.getEmpDontHaveAddress()).build();
    }

    //lấy danh sách emp làm việc trong nhiều hơn 1 project
    @GET
    @Path("empinmorethanoneproject")
    public Response getEmployeesWorkOnMoreThanOneProject(){
        return Response.ok(companyService.getEmployeesWorkOnMoreThanProject()).build();
    }

    //lấy danh sách emp làm việc trong project của dept khác - jpa query
    @GET
    @Path("empworkinotherdepartmentproject")
    public Response getEmployeeWorkOnOtherDepartmentProject(){
        return Response.ok(companyService.getEmployeeWorkOnOtherDepartmentProject()).build();
    }

    //lấy danh sách health insurance theo emp
    @GET
    @Path("healthinsuranceofemployees")
    public Response getHealthInsuranceOfEmployee(){
        return Response.ok(companyService.getHealthInsuranceOfEmployee()).build();
    }

    //lấy danh sách các project theo department
    @GET
    @Path("projectofdepartment")
    public Response getProjectOfDepartment(){
        return Response.ok(companyService.getProjectOfDepartment()).build();
    }

    //lấy danh sách nhân viên làm việc trong project, tổng số lượng nhân viên, tổng số lượng tgian, tổng lương phải trả
    @GET
    @Path("employeeinproject")
    public Response getEmployeeInProject(){
        return Response.ok(companyService.getEmployeeInProject()).build();
    }

    // lấy danh sách relative theo Emp
    @GET
    @Path("relativeofemployee")
    public Response getRelativeOfEmployee(){
        return Response.ok(companyService.getRelativeOfEmployee()).build();
    }

    //lấy 1 emergency relative of emp: Father > Mother > else
    @GET
    @Path("employee-emergency")
    public Response getEmployeeEmergencyRelative(){
        return Response.ok(companyService.getEmployeeEmergencyRelative()).build();
    }

    @GET
    public Response getAllEmployeeGroupByDepartment(){
        return Response.ok(employeeService.getAllEmployeeGroupByDepartment()).build();
    }

    @GET
    @Path("/employeebybirthmonth/{month}")
    public Response getEmployeeByBirthMonth(@PathParam("month") int month){
        return Response.ok(employeeService.getEmployeeByBirthMonth(month)).build();
    }







}
