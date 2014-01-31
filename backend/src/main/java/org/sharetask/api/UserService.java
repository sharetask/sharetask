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
package org.sharetask.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.sharetask.api.dto.UserDTO;
import org.sharetask.api.dto.UserInfoDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Validated
public interface UserService extends UserDetailsService {

	UserDTO create(@NotNull @Valid final UserDTO userDTO);

	UserInfoDTO update(@NotNull @Valid final UserInfoDTO userInfoDTO);

	UserInfoDTO read(@NotNull final String username);
	
	void changePassword(@NotNull final String password);
	
	void resetPassword(@NotNull final String email);

	void confirmInvitation(@NotNull final String code);
}
