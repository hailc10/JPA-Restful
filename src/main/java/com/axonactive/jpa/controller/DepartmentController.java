package com.axonactive.jpa.controller;

import com.axonactive.jpa.controller.request.DepartmentRequest;
import com.axonactive.jpa.entities.Department;
import com.axonactive.jpa.service.DepartmentService;
import com.axonactive.jpa.service.JWTAuthenticationServices;
import com.axonactive.jpa.service.impl.DepartmentServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("departments")
@Api(value = "departmentSwagger")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
public class DepartmentController {

    @Inject
    private DepartmentServiceImpl departmentService;
    @Inject
    JWTAuthenticationServices jwtAuthenticationServices;

    @GET
    @ApiOperation(value = "getAllDepartments", response =Department.class, responseContainer = "List")
    public Response getAllDepartments(@HeaderParam("Authorization") String authorization){
        jwtAuthenticationServices.checkAuthorized(authorization);
        return Response.ok(departmentService.findAll()).build();
    }


    @GET
    @Path("/{id}")
    public Response getDepartmentById(@HeaderParam("Authorization") String authorization, @PathParam("id") int id){
        jwtAuthenticationServices.checkAuthorized(authorization);
        return Response.ok(departmentService.findById(id)).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDepartment(@HeaderParam("Authorization") String authorization, DepartmentRequest departmentRequest){
        jwtAuthenticationServices.checkAuthorized(authorization);
        return Response.ok(departmentService.saveDepartment(departmentRequest)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDepartment(@PathParam("id") int id){
        departmentService.deleteDepartment(id);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    public Response updateDepartment(@PathParam("id") int id, DepartmentRequest departmentRequest){
        return Response.ok(departmentService.updateDepartment(id,departmentRequest)).build();
    }

}
