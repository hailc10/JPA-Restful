package com.axonactive.jpa.controller;

import com.axonactive.jpa.controller.request.CustomerRequest;
import com.axonactive.jpa.service.CustomerService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customers")
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
@Produces(MediaType.APPLICATION_JSON)
public class CustomerController {
    @Inject
    CustomerService customerService;

    @GET
    public Response getAllCustomer(){
        return Response.ok(customerService.getAllCustomer()).build();
    }

    @GET
    @Path("/{customerId}")
    public Response getCustomerById(@PathParam("customerId") int customerId){
        return Response.ok(customerService.getCustomerById(customerId)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCustomer(CustomerRequest customerRequest){
        return Response.ok(customerService.addCustomer(customerRequest)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int customerId){
        customerService.deleteCustomer(customerId);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") int customerId, CustomerRequest customerRequest){
        return Response.ok(customerService.updateCustomer(customerId,customerRequest)).build();
    }

}
