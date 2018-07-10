package com.fanto.auth.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("rest")
public class JAXRSConfiguration extends Application {
	private final Set<Object> singletons = new HashSet<>();
	private final Set<Class<?>> set = new HashSet<>();

	public JAXRSConfiguration() {
		// mandatory
	       set.add(AppServletContextListener.class);


		// the app
		set.add(LoginResource.class);
		set.add(UserResource.class);
	}

	@Override
	public Set<Class<?>> getClasses() {
		return set;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

}
