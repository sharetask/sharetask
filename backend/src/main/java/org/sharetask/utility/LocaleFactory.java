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

import java.util.Locale;

import org.sharetask.entity.Language;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public final class LocaleFactory {
	
	private LocaleFactory() {
	}

	public static Locale getLocale(String language) {
		Locale result = Locale.ENGLISH;

		if (Language.EN.getCode().equals(language)) {
			result = Locale.ENGLISH;
		} else if (Language.CS.getCode().equals(language)) {
			result = new Locale(language,"");
		}
		
		return result;
	}
}
