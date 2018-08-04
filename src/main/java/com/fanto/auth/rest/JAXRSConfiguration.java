package com.fanto.auth.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.fanto.sys.AppExceptionMapper;
import com.fanto.sys.AppRequestFilter;
import com.fanto.sys.AppServletContextListener;

@ApplicationPath("rest")
public class JAXRSConfiguration extends Application {
	private final Set<Object> singletons = new HashSet<>();
	private final Set<Class<?>> set = new HashSet<>();

	public JAXRSConfiguration() {
		// mandatory
		set.add(AppServletContextListener.class);
		set.add(AppRequestFilter.class);
		set.add(AppExceptionMapper.class);

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
