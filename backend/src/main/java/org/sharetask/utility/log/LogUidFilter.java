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
package org.sharetask.utility.log;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class LogUidFilter implements Filter {

	private static final String NOT_KNOWN = "N/A";

	private static final String IDENTIFIERT = "identifiert";

	private String identifiert = "uid";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException,
			ServletException {
	    try {
	    	final SecurityContext context = SecurityContextHolder.getContext();
	    	String uid = null;
	    	if (context != null) {
	    		final Authentication authentication = context.getAuthentication();
	    		if (authentication != null) {
	    			uid = context.getAuthentication().getName();
	    		}
	    	}
            MDC.put(identifiert, uid == null ? NOT_KNOWN : uid);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(identifiert);
        }
	}

	/* (non-Javado
		* @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(final FilterConfig config) throws ServletException {
		final String id = config.getInitParameter(IDENTIFIERT);
		if (id != null && !id.isEmpty()) {
			identifiert = id;
		}
	}
}
