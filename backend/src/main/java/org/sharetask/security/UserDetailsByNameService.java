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

import java.util.ArrayList;
import java.util.Collection;

import org.pac4j.core.profile.CommonProfile;
import org.pac4j.springframework.security.authentication.ClientAuthenticationToken;
import org.sharetask.entity.Role;
import org.sharetask.entity.UserInformation;
import org.sharetask.repository.UserInformationRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Transactional(readOnly = true)
public class UserDetailsByNameService<T extends Authentication> implements AuthenticationUserDetailsService<T>,
		InitializingBean {
	
	private UserInformationRepository userInformationRepository;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.userInformationRepository, "UserInformationRepository must be set");
	}

	public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
		if (authentication instanceof ClientAuthenticationToken) {
			final CommonProfile profile = (CommonProfile)((ClientAuthenticationToken)authentication).getUserProfile();
			UserInformation user = this.userInformationRepository.findByUsername(profile.getEmail());
			if (user == null) {
				return null;
			} else {
				final Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
				for (final Role role : user.getRoles()) {
					authorities.add(new SimpleGrantedAuthority(role.name()));
				}
				return buildUserDetails(profile.getEmail(), authorities);			}
		} else {
			throw new SecurityException("Wrong authentication object");
		}
	}
	
	private UserDetails buildUserDetails(final String email, final Collection<GrantedAuthority> authorities) {
		return new UserDetails() {

			private static final long serialVersionUID = 4726237436112264492L;

			@Override
			public boolean isEnabled() {
				return true;
			}
			
			@Override
			public boolean isCredentialsNonExpired() {
				return true;
			}
			
			@Override
			public boolean isAccountNonLocked() {
				return true;
			}
			
			@Override
			public boolean isAccountNonExpired() {
				return true;
			}
			
			@Override
			public String getUsername() {
				return email;
			}
			
			@Override
			public String getPassword() {
				return null;
			}
			
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return authorities;
			}
		};
	}

	public void setUserInformationRepository(UserInformationRepository userInformationRepository) {
		this.userInformationRepository = userInformationRepository;
	}
}
