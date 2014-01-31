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

import org.sharetask.api.InvitationService;
import org.sharetask.api.MailService;
import org.sharetask.api.UserService;
import org.sharetask.api.dto.InvitationDTO;
import org.sharetask.api.dto.UserDTO;
import org.sharetask.api.dto.UserInfoDTO;
import org.sharetask.entity.Role;
import org.sharetask.entity.UserAuthentication;
import org.sharetask.entity.UserInformation;
import org.sharetask.repository.UserAuthenticationRepository;
import org.sharetask.repository.UserInformationRepository;
import org.sharetask.security.UserDetailsImpl;
import org.sharetask.utility.DTOConverter;
import org.sharetask.utility.HashCodeUtil;
import org.sharetask.utility.SecurityUtil;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
@Service("userService")
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	@Inject
	private UserAuthenticationRepository userAuthenticationRepository;

	@Inject
	private UserInformationRepository userInformationRepository;

	@Inject
	private PasswordEncoder passwordEncoder;

	@Inject
	private SaltSource saltSource;

	@Inject
	private InvitationService invitationService;

	@Inject
	private MailService mailService;

	private static class UserDetailBuilder {

		private final UserAuthentication user;

		public UserDetailBuilder(final UserAuthentication user) {
			this.user = user;
		}

		public UserDetails build() {
			final String username = user.getUsername();
			final String password = user.getPassword();
			final String salt = user.getSalt();
			final boolean enabled = user.isEnabled();
			final boolean accountNonExpired = user.isEnabled();
			final boolean credentialsNonExpired = user.isEnabled();
			final boolean accountNonLocked = user.isEnabled();

			final Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for (final Role role : user.getUserInfo().getRoles()) {
				authorities.add(new SimpleGrantedAuthority(role.name()));
			}

			final UserDetails userDetails = new UserDetailsImpl(username, password, salt,
					enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
			return userDetails;
		}
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final UserAuthentication user = userAuthenticationRepository.findByUsername(username);
		UserDetails userDetails = null;
		if (user != null) {
			userDetails = new UserDetailBuilder(user).build();
		} else {
			throw new UsernameNotFoundException("UserAuthentication with name: " + username + " not found");
		}
		return userDetails;
	}

	@Override
	@Transactional
	public UserDTO create(final UserDTO userDTO) {
		log.info("Registering user: {}", userDTO);
		final UserAuthentication user = new UserAuthentication();
		user.setUsername(userDTO.getUsername());
		final UserInformation userInfo = user.getUserInfo();
		userInfo.setUsername(userDTO.getUsername());
		userInfo.setName(userDTO.getName());
		userInfo.setSurName(userDTO.getSurName());
		userInfo.setMobilePhone(userDTO.getMobilePhone());

		if (userAuthenticationRepository.findByUsername(userDTO.getUsername()) != null) {
			throw new UserAlreadyExistsException();
		}

		final Collection<Role> roles = new ArrayList<Role>();
		user.setEnabled(false);

		roles.add(Role.ROLE_USER);
		userInfo.setRoles(roles);

		// salt create
		final String salt = getSalt(userDTO.getUsername());
		user.setSalt(salt);

		final UserDetails userDetails = new UserDetailsImpl(user.getUsername(), "password", salt,
				new ArrayList<GrantedAuthority>());
		user.setPassword(passwordEncoder.encodePassword(userDTO.getPassword(),
				saltSource.getSalt(userDetails)));
		final UserAuthentication storedUser = userAuthenticationRepository.save(user);
		invitationService.inviteRegisteredUser(userDTO.getUsername());
		return DTOConverter.convert(storedUser,  UserDTO.class);
	}

	private String getSalt(final String username) {
		return HashCodeUtil.getHashCode(System.currentTimeMillis() + username);
	}

	@Override
	@Transactional
	public UserInfoDTO update(final UserInfoDTO userInfoDTO) {
		final UserInformation user = userInformationRepository.read(userInfoDTO.getUsername());
		user.setName(userInfoDTO.getName());
		user.setSurName(userInfoDTO.getSurName());
		user.setMobilePhone(userInfoDTO.getMobilePhone());
		final UserInformation storedUser = userInformationRepository.save(user);
		return DTOConverter.convert(storedUser, UserInfoDTO.class);
	}

	@Override
	public UserInfoDTO read(final String username) {
		final UserInformation user = userInformationRepository.read(username);
		return DTOConverter.convert(user, UserInfoDTO.class);
	}

	@Override
	@Transactional
	public void changePassword(final String password) {
		final String username = SecurityUtil.getCurrentSignedInUsername();
		final UserAuthentication user = userAuthenticationRepository.read(username);
		final UserDetails userDetails = new UserDetailsImpl(user.getUsername(), "password", user.getSalt(),
				new ArrayList<GrantedAuthority>());
		user.setPassword(passwordEncoder.encodePassword(password, saltSource.getSalt(userDetails)));
		userAuthenticationRepository.save(user);
	}

	@Override
	@Transactional
	public void confirmInvitation(final String code) {
		final InvitationDTO invitation = invitationService.confirmInvitation(code);
		final UserAuthentication user = userAuthenticationRepository.read(invitation.getEmail());
		user.setEnabled(true);
		userAuthenticationRepository.save(user);
	}

	@Override
	@Transactional
	public void resetPassword(final String email) {
		final String password = SecurityUtil.generatePassword();
		final UserAuthentication user = userAuthenticationRepository.read(email);
		final UserDetails userDetails = new UserDetailsImpl(user.getUsername(), "password", user.getSalt(),
				new ArrayList<GrantedAuthority>());
		user.setPassword(passwordEncoder.encodePassword(password, saltSource.getSalt(userDetails)));
		userAuthenticationRepository.save(user);
		mailService.sendResetPasswordMail(email, password);
	}
}
