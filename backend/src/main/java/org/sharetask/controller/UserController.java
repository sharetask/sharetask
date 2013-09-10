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
package org.sharetask.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.sharetask.api.UserService;
import org.sharetask.api.dto.UserDTO;
import org.sharetask.api.dto.UserInfoDTO;
import org.sharetask.controller.json.UserPassword;
import org.sharetask.utility.SecurityUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
@Controller
@RequestMapping("/api/user")
public class UserController {

	@Inject
	@Qualifier("authenticationManagerStd")
	private AuthenticationManager authenticationManager;

	@Inject
	private SecurityContextRepository repository;

	@Inject
	private RememberMeServices rememberMeServices;

	@Inject
	private UserService userService;

	@RequestMapping(value = "/login/status", method = RequestMethod.GET)
	public void loginStatus(final HttpServletRequest request, final HttpServletResponse response) {
		int resultCode = HttpStatus.UNAUTHORIZED.value();
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("authetication: {}", authentication);
		if (authentication != null && authentication.isAuthenticated()
				&& !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
			resultCode = HttpStatus.OK.value();
		}
		response.setStatus(resultCode);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void performLogin(@RequestBody final UserPassword login, final HttpServletRequest request,
			final HttpServletResponse response) {
		final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login.getUsername(),
				login.getPassword());
		try {
			final Authentication auth = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(auth);
			repository.saveContext(SecurityContextHolder.getContext(), request, response);
			rememberMeServices.loginSuccess(request, response, auth);
			response.setStatus(HttpStatus.OK.value());
		} catch (final BadCredentialsException ex) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public void performLogout(final HttpServletRequest request, final HttpServletResponse response) {
		SecurityContextHolder.clearContext();
		final HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	@RequestMapping(value = "/password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public void passwordChange(@RequestBody final UserPassword user) {
		userService.changePassword(user.getPassword());
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody public UserDTO create(@RequestBody final UserDTO user) {
		return userService.create(user);
	}


	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody public UserInfoDTO update(@RequestBody final UserInfoDTO user) {
		return userService.update(user);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public UserInfoDTO get() {
		return userService.read(SecurityUtil.getCurrentSignedInUsername());
	}

	@RequestMapping(value = "/confirm", method = RequestMethod.GET)
	public String invite(@RequestParam("code") final String code) {
		userService.confirmInvitation(code);
		return "redirect:/";
	}
}
