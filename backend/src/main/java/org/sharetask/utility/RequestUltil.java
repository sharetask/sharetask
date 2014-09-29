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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public final class RequestUltil {
	
	public static final String LOCALE = "locale"; 
	public static final String LANGUAGE = "language"; 
	public static final String COUNTRY = "country"; 

	private RequestUltil() {
	}
	
	public static String getLocale(final HttpServletRequest request) {
		final Cookie[] cookies = request.getCookies();
		String result = "";
		for (final Cookie cookie : cookies) {
			if (cookie.getName().equals("locale")) {
				result = cookie.getValue();
				break;
			}
		}
		if (result.isEmpty()) {
			result = request.getLocale().getLanguage().toLowerCase();
			if (request.getLocale().getCountry().length() > 0) {
				result = request.getLocale().getLanguage().toLowerCase() + "-" + request.getLocale().getCountry().toLowerCase();
			}
		}
		return result;
	}

	public static String getLanguage(final HttpServletRequest request) {
		final Cookie[] cookies = request.getCookies();
		String result = "";
		for (final Cookie cookie : cookies) {
			if (cookie.getName().equals("language")) {
				result = cookie.getValue();
				break;
			}
		}
		if (result.isEmpty()) {
			result = request.getLocale().getLanguage().toLowerCase();
		}
		return result;
	}
	
	public static Map<String, String> getRequestData(final HttpServletRequest request) {
		final Map<String, String> result = new HashMap<String, String>();
		final Cookie[] cookies = request.getCookies();
		String locale = "";
		String language = "";
		String country = "";

		for (final Cookie cookie : cookies) {
			if (cookie.getName().equals("locale")) {
				locale = cookie.getValue();
				language = cookie.getValue();
				break;
			}
		}

		if (locale.isEmpty()) {
			language = request.getLocale().getLanguage().toLowerCase();
			locale = request.getLocale().getLanguage().toLowerCase();
			if (request.getLocale().getCountry().length() > 0) {
				country = request.getLocale().getCountry().toLowerCase();
				locale = request.getLocale().getLanguage().toLowerCase() + "-"
						+ request.getLocale().getCountry().toLowerCase();
			}
		}
		
		result.put(LOCALE, locale);
		result.put(LANGUAGE, language);
		result.put(COUNTRY, country);
		
		return result;
	}
}
