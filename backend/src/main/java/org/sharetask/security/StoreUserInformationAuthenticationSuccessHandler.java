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
package org.sharetask.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.pac4j.oauth.profile.google2.Google2Profile;
import org.pac4j.springframework.security.authentication.ClientAuthenticationToken;
import org.sharetask.entity.Role;
import org.sharetask.entity.UserInformation;
import org.sharetask.repository.UserInformationRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
public class StoreUserInformationAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Inject
	private UserInformationRepository userRepository;

	interface AuthGrantedAuthority extends GrantedAuthority {
	};

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException {

		final Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		if (principal instanceof ClientAuthenticationToken) {
			log.debug("Token is pac4j token.");

			final Google2Profile profile = (Google2Profile)((ClientAuthenticationToken)principal).getUserProfile();
			if (userRepository.findByUsername(profile.getEmail()) == null) {
				log.debug("User with name: {} doesne exist's. Will be created", profile.getEmail());
				final UserInformation userInformation = new UserInformation(profile.getEmail());
				userInformation.setName(profile.getFirstName());
				userInformation.setSurName(profile.getFamilyName());
				userInformation.setLanguage("en");
				final ArrayList<Role> list = new ArrayList<Role>();
				list.add(Role.ROLE_USER);
				userInformation.setRoles(list);
				userRepository.save(userInformation);
			}

			final ArrayList<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
			list.add(new SimpleGrantedAuthority(Role.ROLE_USER.name()));
			final Authentication authToken = new UsernamePasswordAuthenticationToken(profile.getEmail(), "", list);
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}

		super.onAuthenticationSuccess(request, response, authentication);
	}
}
