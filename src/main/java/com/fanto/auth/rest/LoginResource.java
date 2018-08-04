package com.fanto.auth.rest;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.fanto.auth.service.LoginService;

@Path("login")
public class LoginResource {

	@Inject
	LoginService loginService;

	public LoginResource() {

	}

	@GET
	@Path(value = "{uid}/{pwd}")
	public Response login(@PathParam("uid") String uid, @PathParam("pwd") String pwd) throws Throwable {

		String jweString = loginService.login(uid, pwd);
		return Response.ok().header("Authorization", jweString).build();

	}

}
