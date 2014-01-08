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

import lombok.extern.slf4j.Slf4j;

import org.sharetask.api.UserService;
import org.sharetask.api.WorkspaceService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
@Controller
@RequestMapping("/confirm")
public class ConfirmController {

	@Inject
	private WorkspaceService workspaceService;
	
	@Inject
	private UserService userService;

	@RequestMapping(value = "/userRegistration", method = RequestMethod.GET)
	public String invite(@RequestParam("code") final String code) {
		String result;
		try {
			userService.confirmInvitation(code);
			result = "redirect:/confirmed";
		} catch (final EmptyResultDataAccessException ex) {
			result = "redirect:/confirmed?error=notexists";
		}
		return result;
	}
	
	@RequestMapping(value = "/addWorkspaceMember", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public String addWorkspaceMember(@RequestParam("code") final String code) {
		String result;
		try {
			workspaceService.addMember(code);
			result = "redirect:/confirmed";
		} catch (final EmptyResultDataAccessException ex) {
			result = "redirect:/confirmed?error=notexists";
		} catch (final JpaObjectRetrievalFailureException ex) {
			result = "redirect:/confirmed?error=userNotExists";
		}
		return result;
	}

}
