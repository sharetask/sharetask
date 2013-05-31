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

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.codec.Hex;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class HashCodeUtil {

	public static String getHashCode(final String data) {
		try {
			final MessageDigest mda = MessageDigest.getInstance("SHA-512");
			final String baseSalt = System.currentTimeMillis() + "dev1@shareta.sk";
			final byte [] digest = mda.digest(baseSalt.getBytes(Charset.forName("UTF-8")));
			return new String(Hex.encode(digest));
		} catch (final NoSuchAlgorithmException e) {
			throw new UnsupportedOperationException(e);
		}
	}
}
