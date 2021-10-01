package com.redhat.challenge.discount;

import com.redhat.challenge.discount.model.DiscountCode;
import com.redhat.challenge.discount.model.DiscountCodeType;
import com.redhat.challenge.discount.service.DiscountService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/discounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DiscountCodesResource {

  @Inject
  DiscountService discountService;

  @POST
  public Response create(DiscountCode discountCode) {
    return Response.created(URI.create(discountService.create(discountCode))).build();
  }

  @GET
  @Path("/consume/{name}")
  @Produces(MediaType.TEXT_PLAIN)
  public String consume(@PathParam("name") String name) {
    return discountService.consume(name);
  }

  @GET
  @Path("/{type}")
  public DiscountCodes getByType(@PathParam("type") DiscountCodeType type) {
    List<DiscountCode> discountCodes = discountService.getByType(type);
    return new DiscountCodes(discountCodes, discountCodes.size());
  }

}
