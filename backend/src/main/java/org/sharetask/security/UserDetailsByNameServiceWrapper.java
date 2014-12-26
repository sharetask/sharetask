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

import org.pac4j.core.profile.CommonProfile;
import org.pac4j.springframework.security.authentication.ClientAuthenticationToken;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class UserDetailsByNameServiceWrapper<T extends Authentication> implements AuthenticationUserDetailsService<T>,
		InitializingBean {
	
	protected UserDetailsService userDetailsService = null;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.userDetailsService, "UserDetailsService must be set");
	}

	public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
		if (authentication instanceof ClientAuthenticationToken) {
			final CommonProfile profile = (CommonProfile)((ClientAuthenticationToken)authentication).getUserProfile();
			try {
				return this.userDetailsService.loadUserByUsername(profile.getEmail());
			} catch (UsernameNotFoundException e) {
				return null;
			}
		} else {
			throw new SecurityException("Wrong authentication object");
		}
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
}
