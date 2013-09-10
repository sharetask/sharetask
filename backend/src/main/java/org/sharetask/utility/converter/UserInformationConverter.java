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
package org.sharetask.utility.converter;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.sharetask.api.dto.UserInfoDTO;
import org.sharetask.entity.UserInformation;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class UserInformationConverter implements CustomConverter {

	/* (non-Javadoc)
	 * @see org.dozer.CustomConverter#convert(java.lang.Object, java.lang.Object, java.lang.Class, java.lang.Class)
	 */
	@Override
	public Object convert(final Object destination, final Object source, final Class<?> destClass,
			final Class<?> sourceClass) {
		Object result = null;
		if (source instanceof UserInformation) {
			final UserInfoDTO teacher = convert((UserInformation) source);
			result = teacher;
		} else if (source != null) {
			throw new MappingException("Converter UserInformationConverter used incorrectly. Arguments passed were:"
					+ destination + " and " + source);
		}
		return result;
	}

	private UserInfoDTO convert(final UserInformation source) {
		final UserInfoDTO userInfoDTO = new UserInfoDTO();
		userInfoDTO.setUsername(source.getUsername());
		userInfoDTO.setName(source.getName());
		userInfoDTO.setSurName(source.getSurName());
		return userInfoDTO;
	}
}
