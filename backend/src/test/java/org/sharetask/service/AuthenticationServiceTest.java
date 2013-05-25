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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;

import javax.inject.Inject;

import org.junit.Test;
import org.sharetask.api.UserService;
import org.sharetask.data.DbUnitTest;
import org.sharetask.entity.Role;
import org.sharetask.repository.UserRepository;
import org.sharetask.security.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.codec.Hex;

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
	public void testPasswordEncoding() throws NoSuchAlgorithmException, NoSuchProviderException {
		ArrayList<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		list.add(new SimpleGrantedAuthority(Role.ROLE_USER.name()));
		list.add(new SimpleGrantedAuthority(Role.ROLE_ADMINISTRATOR.name()));
		User u = new UserDetailsImpl("dev1@shareta.sk", "password", "6ef7a5723d302c64d65d02f5c6662dc61bebec930ea300620bc9ff7f12b49fda11e2e57933526fd3b73840b0693a7cf4abe05fbfe16223d4bd42eb3043cf5d24", list);
		String password = passwordEncoder.encodePassword("password", saltSource.getSalt(u));
		org.sharetask.entity.User user = userRepository.findOne(u.getUsername());
		assertEquals(password, user.getPassword());
	    Authentication authentication = new UsernamePasswordAuthenticationToken("dev1@shareta.sk", "password");
	    try {
	    	authenticationManager.authenticate(authentication);
	    } catch (BadCredentialsException e) {
	    	fail("Problem with authentication: user/password");
	    }
	}
	
	@Test
	public void testSaltPasswordEncoder() throws NoSuchAlgorithmException {
		String username = "test3@test.com";
		String password = "password";
		ArrayList<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		list.add(new SimpleGrantedAuthority(Role.ROLE_USER.name()));
		list.add(new SimpleGrantedAuthority(Role.ROLE_ADMINISTRATOR.name()));
		MessageDigest mda = MessageDigest.getInstance("SHA-512");
		String baseSalt = String.valueOf(System.currentTimeMillis()) + "dev1@shareta.sk";
		byte [] digest = mda.digest(baseSalt.getBytes());
		String salt = new String(Hex.encode(digest));
		User u = new UserDetailsImpl(username, password, salt, list);
		String passwordEncoded = passwordEncoder.encodePassword("password", saltSource.getSalt(u));
		System.out.println("User name: " + username);
		System.out.println("Password: " + password);
		System.out.println("Salt: " + salt);
		System.out.println("Encoded password: " + passwordEncoded);
	}
}
