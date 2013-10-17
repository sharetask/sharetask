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

import javax.annotation.Resource;
import javax.inject.Inject;

import org.junit.Test;
import org.sharetask.data.DbUnitTest;
import org.sharetask.entity.Role;
import org.sharetask.repository.UserAuthenticationRepository;
import org.sharetask.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static Logger log = LoggerFactory.getLogger(AuthenticationServiceTest.class);

	@Inject
	private UserAuthenticationRepository userRepository;

	@Inject
	private PasswordEncoder passwordEncoder;

	@Inject
	private SaltSource saltSource;

	@Resource(name = "authenticationManagerStd")
	private AuthenticationManager authenticationManager;

	public AuthenticationServiceTest() {
		enableSecurity = false;
	}

	@Test
	public void testPasswordEncoding() throws NoSuchAlgorithmException, NoSuchProviderException {
		final ArrayList<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		list.add(new SimpleGrantedAuthority(Role.ROLE_USER.name()));
		list.add(new SimpleGrantedAuthority(Role.ROLE_ADMINISTRATOR.name()));
		final User u = new UserDetailsImpl("dev1@shareta.sk", "password", "6ef7a5723d302c64d65d02f5c6662dc61bebec930ea300620bc9ff7f12b49fda11e2e57933526fd3b73840b0693a7cf4abe05fbfe16223d4bd42eb3043cf5d24", list);
		final String password = passwordEncoder.encodePassword("password", saltSource.getSalt(u));
		final org.sharetask.entity.UserAuthentication user = userRepository.findOne(u.getUsername());
		assertEquals(password, user.getPassword());
	    final Authentication authentication = new UsernamePasswordAuthenticationToken("dev1@shareta.sk", "password");
	    try {
	    	authenticationManager.authenticate(authentication);
	    } catch (final BadCredentialsException e) {
	    	fail("Problem with authentication: user/password");
	    }
	}

	@Test
	public void testSaltPasswordEncoder() throws NoSuchAlgorithmException {
		final String username = "test3@test.com";
		final String password = "password";
		final ArrayList<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		list.add(new SimpleGrantedAuthority(Role.ROLE_USER.name()));
		list.add(new SimpleGrantedAuthority(Role.ROLE_ADMINISTRATOR.name()));
		final MessageDigest mda = MessageDigest.getInstance("SHA-512");
		final String baseSalt = String.valueOf(System.currentTimeMillis()) + "dev1@shareta.sk";
		final byte [] digest = mda.digest(baseSalt.getBytes());
		final String salt = new String(Hex.encode(digest));
		final User u = new UserDetailsImpl(username, password, salt, list);
		final String passwordEncoded = passwordEncoder.encodePassword("password", saltSource.getSalt(u));
		log.info("UserAuthentication name: {}", username);
		log.info("Password: {}", password);
		log.info("Salt: {}", salt);
		log.info("Encoded password: {}", passwordEncoded);
	}
}
