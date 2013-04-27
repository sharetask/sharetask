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

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.sharetask.api.UserAlreadyExists;
import org.sharetask.api.UserService;
import org.sharetask.api.dto.UserDTO;
import org.sharetask.entity.Role;
import org.sharetask.entity.User;
import org.sharetask.repository.UserRepository;
import org.sharetask.utility.DTOConverter;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
@Service("userService")
@Validated
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	@Inject
	private UserRepository userRepository;
	 
	@Inject
	private PasswordEncoder passwordEncoder;
	
	@Inject
	private SaltSource saltSource;
	
	private static class UserDetailBuilder {

		private final User user;
		
		public UserDetailBuilder(User user) {
			this.user = user;
		}

		public UserDetails build() {
			final String username = user.getUsername();
			final String password = user.getPassword();
			final boolean enabled = user.isEnabled();
			final boolean accountNonExpired = user.isEnabled();
			final boolean credentialsNonExpired = user.isEnabled();
			final boolean accountNonLocked = user.isEnabled();

			Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for (Role role : user.getRoles()) {
				authorities.add(new SimpleGrantedAuthority(role.name()));
			}

			UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, password,
					enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
			return userDetails;
		}
	}
	
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		UserDetails userDetails = null;
		if (user != null) {
			userDetails = new UserDetailBuilder(user).build();
		} else {
			throw new UsernameNotFoundException("User with name: " + username + " not found");
		}
		return userDetails;
	}
	
	@Transactional
	@Override
	public UserDTO create(final UserDTO userDTO) throws UserAlreadyExists {
		log.info("Registering user: {}", userDTO);
		User user = DTOConverter.convert(userDTO, User.class);
		
		if (userRepository.findByUsername(userDTO.getUsername()) != null) {
			throw new UserAlreadyExists();
		}
		
		user.setEmail(user.getUsername());
		Collection<Role> roles = new ArrayList<Role>();
		user.setEnabled(true);
		
		roles.add(Role.ROLE_USER);
		user.setRoles(roles);
		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), "password", new ArrayList<GrantedAuthority>());
		user.setPassword(passwordEncoder.encodePassword(userDTO.getPassword(), saltSource.getSalt(userDetails)));
		user = userRepository.save(user);
		return DTOConverter.convert(userDetails,  UserDTO.class);
	}
}
