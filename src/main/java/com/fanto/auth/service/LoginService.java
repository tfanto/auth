package com.fanto.auth.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import com.fanto.auth.dao.LoginDao;
import com.fanto.auth.rest.AppServletContextListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectEncrypter;

public class LoginService {

	@Inject
	private LoginDao dao;

	private ObjectMapper MAPPER = new ObjectMapper();

	public String login(String login, String password)
			throws KeyLengthException, JOSEException, JsonProcessingException {

		List<String> roles = dao.login(login, password);

		EncryptionMethod encryptionMethod = EncryptionMethod.A128CBC_HS256;
		JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, encryptionMethod);

		// Set the plain text
		Payload payload = createPayLoad(login, roles);

		// Create the JWE object and encrypt it
		JWEObject jweObject = new JWEObject(header, payload);
		
		
		jweObject.encrypt(new DirectEncrypter(getKey()));

		// Serialize to compact JOSE form...
		String jweString = jweObject.serialize();

		return jweString;

	}

	private Payload createPayLoad(String login, List<String> roles) throws JsonProcessingException {

		Map<String, Object> map = new HashMap<>();
		map.put("user", login);
		String rolesString = "";
		for (String r : roles) {
			rolesString += r;
			rolesString += ",";
		}
		rolesString = rolesString.substring(0, rolesString.length() - 1);
		map.put("roles", rolesString);
		String json = MAPPER.writeValueAsString(map);
		return new Payload(json);
	}

	private SecretKey getKey() {
		return AppServletContextListener.getEncryptionKey();
	}

}
