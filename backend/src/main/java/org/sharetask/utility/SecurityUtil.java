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
package org.sharetask.utility;

import java.util.Random;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public final class SecurityUtil {

	private static final int PASSWORD_LENGT = 8;

	private SecurityUtil() {
	}
	
	/**
	 * Get current logged in user.
	 * @return
	 */
	public static String getCurrentSignedInUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	/**
	 * Generate password from alpha numerics characters in total length 8 characters. 
	 * @return
	 */
	public static String generatePassword() {
		final char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		final StringBuilder sb = new StringBuilder();
		final Random random = new Random();

		for (int i = 0; i < PASSWORD_LENGT; i++) {
		    final char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		return sb.toString();
	}
}
