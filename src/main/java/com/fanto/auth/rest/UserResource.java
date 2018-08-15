package com.fanto.auth.rest;

import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.fanto.auth.service.UserService;
import com.fnt.dto.UserDto;

@Path("user")
public class UserResource {

	@Inject
	private UserService userService;

	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public Response getAll() {
		List<UserDto> users = userService.getAll();
		return Response.ok(users).build();
	}

	@GET
	@Path(value = "{login}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public Response get(@PathParam("login") String login) {
		UserDto user = userService.get(login);
		return Response.ok(user).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	// @PermitAll()
	public Response create(UserDto user) {
		UserDto created = userService.create(user);
		return Response.ok(created).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public Response update(UserDto user) {
		UserDto created = userService.update(user);
		return Response.ok(created).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("updpwd")
	public Response updatePassword(@Context SecurityContext ctx, Map<String, String> payload) {

		String currentUser = ctx.getUserPrincipal().getName();
		if (currentUser != null) {
			String login = payload.get("login");
			if (login != null) {
				if(login.equals(currentUser)) {
					String oldpwd = payload.get("oldpwd");
					String newpwd = payload.get("newpwd");
					if(userService.updatePwd(currentUser, login, oldpwd,newpwd)) {
						return Response.status(Status.OK).build();						
					}
				}
			}
		}
		return Response.status(Status.FORBIDDEN).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	@Path(value = "{uid}/{pwd}")
	public Response update(@PathParam("uid") String login, @PathParam("pwd") String pwd) {
		Boolean ok = userService.update(login, pwd);
		return Response.ok(ok).build();
	}

	@DELETE
	@Path(value = "{uid}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public Response removeUser(@PathParam("uid") String uid) {
		userService.delete(uid);
		return Response.ok().header("deleted_uid", uid).build();
	}

}
