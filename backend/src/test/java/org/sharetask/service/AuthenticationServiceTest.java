/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.sharetask.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import javax.inject.Inject;

import org.junit.Test;
import org.sharetask.api.UserService;
import org.sharetask.data.DbUnitTest;
import org.sharetask.entity.Role;
import org.sharetask.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class AuthenticationServiceTest extends DbUnitTest {

	@Inject
	private UserService userService;

	@Inject
	private UserRepository userRepository;

	@Inject
	private PasswordEncoder passwordEncoder;
	
	@Inject
	private SaltSource saltSource;
	
	@Inject
	private AuthenticationManager authenticationManager;
	
	public AuthenticationServiceTest() {
		this.enableSecurity = false;
	}
	
	@Test
	public void testPasswordEncoding() {
		ArrayList<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		list.add(new SimpleGrantedAuthority(Role.ROLE_USER.name()));
		list.add(new SimpleGrantedAuthority(Role.ROLE_ADMINISTRATOR.name()));
		User u = new User("admin@shareta.sk", "password", list);
		String password = passwordEncoder.encodePassword("password", saltSource.getSalt(u));
		org.sharetask.entity.User user = userRepository.findOne(u.getUsername());
		assertEquals(password, user.getPassword());
	    Authentication authentication = new UsernamePasswordAuthenticationToken("admin@shareta.sk", "password");
	    try {
	    	authenticationManager.authenticate(authentication);
	    } catch (BadCredentialsException e) {
	    	fail("Problem with authentication: user/password");
	    }
	}
}
