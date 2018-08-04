package com.fanto.auth.rest;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fanto.auth.service.UserService;
import com.fnt.dto.UserDto;

@Path("user")
public class UserResource {

	@Inject
	private UserService userService;

	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed( "ADMIN" )
	public Response getAll() {
		List<UserDto> users = userService.getAll();
		return Response.ok(users).build();
	}

	@GET
	@Path(value = "{login}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed( "ADMIN" )
	public Response get(@PathParam("login") String login) {
		UserDto user = userService.get(login);
		return Response.ok(user).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed( "ADMIN" )
	public Response create(UserDto user) {
		UserDto created = userService.create(user);
		return Response.ok(created).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed( "ADMIN" )
	public Response update(UserDto user) {
		UserDto created = userService.update(user);
		return Response.ok(created).build();
	}

	@DELETE
	@Path(value = "{uid}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed( "ADMIN" )
	public Response removeUser(@PathParam("uid") String uid) {
		userService.delete(uid);
		return Response.ok().header("deleted_uid", uid).build();
	}

}
