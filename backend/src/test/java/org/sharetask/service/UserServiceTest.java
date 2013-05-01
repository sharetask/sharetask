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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import javax.inject.Inject;

import org.junit.Test;
import org.sharetask.api.UserAlreadyExists;
import org.sharetask.api.UserService;
import org.sharetask.api.dto.UserDTO;
import org.sharetask.data.DbUnitTest;
import org.sharetask.entity.Role;
import org.sharetask.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class UserServiceTest extends DbUnitTest {

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
	
	/**
	 * Test method for {@link com.tapas.evidence.service.UserServiceImpl#loadUserByUsername(java.lang.String)}.
	 */
	@Test
	public void testLoadUserByUsername() {
		UserDetails user = userService.loadUserByUsername("dev1@shareta.sk");
		assertNotNull(user);
		assertTrue(user.getAuthorities().size() == 1);
		ArrayList<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		list.add(new SimpleGrantedAuthority(Role.ROLE_USER.name()));
		assertTrue(user.getAuthorities().containsAll(list));
	}
	
	@Test
	public void testCreateUser() {
		UserDTO userDTO = new UserDTO();
		String name = String.valueOf(System.currentTimeMillis()) + "@test.test";
		userDTO.setUsername(name);
		userDTO.setName("Test");
		userDTO.setPassword("password");
		userDTO.setSurName("Test Surname");
		try {
			userService.create(userDTO );
		} catch (UserAlreadyExists e) {
			fail("User " + userDTO.getUsername() + " already exists!");
		}
		
		org.sharetask.entity.User user = userRepository.findByUsername(name);
		assertEquals(user.getEmail(), userDTO.getUsername());
		assertEquals(user.getName(), userDTO.getName());
		String password = passwordEncoder.encodePassword("password", 
				saltSource.getSalt(new User(name, "password", new ArrayList<GrantedAuthority>())));
		assertEquals(user.getPassword(), password);
		assertEquals(user.getSurName(), userDTO.getSurName());
		assertEquals(user.getUsername(), userDTO.getUsername());
		assertTrue(user.getRoles().size() == 1);
	}
}
