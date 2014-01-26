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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of PermissionEvaluator. Extending possibility of evaluate different verb in hasPermission.
 * @author Michal Bocek
 * @since 1.0.0
 */
public class AccessPermissionEvaluator implements PermissionEvaluator {
	private Map<String, Permission> permissionNameToPermissionMap = new HashMap<String, Permission>();

	protected AccessPermissionEvaluator() {
	}

	public AccessPermissionEvaluator(final Map<String, Permission> permissionNameToPermissionMap) {
		Assert.notNull(permissionNameToPermissionMap);
		this.permissionNameToPermissionMap = permissionNameToPermissionMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.access.PermissionEvaluator#hasPermission(org.springframework.security.core.
	 * Authentication, java.lang.Object, java.lang.Object)
	 */
	@Override
	@Transactional
	public boolean hasPermission(final Authentication authentication, final Object targetDomainObject,
			final Object permission) {
		boolean hasPermission = false;
		if (canHandle(authentication, targetDomainObject, permission)) {
			hasPermission = checkPermission(authentication, targetDomainObject, (String) permission);
		}
		return hasPermission;
	}

	private boolean canHandle(final Authentication authentication, final Object targetDomainObject,
			final Object permission) {
		return targetDomainObject != null && authentication != null && permission instanceof String;
	}

	private boolean checkPermission(final Authentication authentication, final Object targetDomainObject,
			final String permissionKey) {
		verifyPermissionIsDefined(permissionKey);
		final Permission permission = permissionNameToPermissionMap.get(permissionKey);
		return permission.isAllowed(authentication, targetDomainObject);
	}

	private void verifyPermissionIsDefined(final String permissionKey) {
		if (!permissionNameToPermissionMap.containsKey(permissionKey)) {
			throw new PermissionNotDefinedException("No permission with key " + permissionKey + " is defined in "
					+ this.getClass().toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.access.PermissionEvaluator#hasPermission(org.springframework.security.core.
	 * Authentication, java.io.Serializable, java.lang.String, java.lang.Object)
	 */
	@Override
	public boolean hasPermission(final Authentication authentication, final Serializable targetId,
			final String targetType, final Object permission) {
		throw new PermissionNotDefinedException("Id and Class permissions are not supperted by "
				+ this.getClass().toString());
	}
}
