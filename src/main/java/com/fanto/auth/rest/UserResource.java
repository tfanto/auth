package com.fanto.auth.rest;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fanto.auth.service.UserService;

@Path("user")
public class UserResource {

	@Inject
	private UserService userService;

	@POST
	@Path(value = "{uid}/{pwd}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(@PathParam("uid") String uid, @PathParam("pwd") String pwd) {
		userService.add(uid, pwd);
		return Response.ok().header("uid", uid).build();
	}

	@POST
	@Path(value = "{uid}/{role}/{description}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRole(@PathParam("uid") String uid, @PathParam("role") String role,
			@PathParam("description") String description) {
		userService.addUserRole(uid, role, description);
		return Response.ok().header("uid", uid).header("description", description).build();
	}

	@DELETE
	@Path(value = "{uid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeUser(@PathParam("uid") String uid) {
		userService.delete(uid);
		return Response.ok().header("deleted_uid", uid).build();
	}

}
