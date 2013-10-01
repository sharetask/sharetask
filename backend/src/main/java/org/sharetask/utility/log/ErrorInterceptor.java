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

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.sharetask.api.Constants;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.util.Assert;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class ErrorInterceptor extends CustomizableTraceInterceptor {

	private static final long serialVersionUID = Constants.VERSION;

	private String exceptionMessage = DEFAULT_EXCEPTION_MESSAGE;

	/**
	 * The default message used for writing exception messages.
	 */
	private static final String DEFAULT_EXCEPTION_MESSAGE =
			"Exception thrown in method '" + PLACEHOLDER_METHOD_NAME + "' of class [" + PLACEHOLDER_TARGET_CLASS_NAME + "]";

	/**
	 * Set the template used for method exception log messages.
	 * This template can contain any of the following placeholders:
	 * <ul>
	 * <li>{@code $[targetClassName]}</li>
	 * <li>{@code $[targetClassShortName]}</li>
	 * <li>{@code $[argumentTypes]}</li>
	 * <li>{@code $[arguments]}</li>
	 * <li>{@code $[exception]}</li>
	 * </ul>
	 */
	@Override
	public void setExceptionMessage(final String exceptionMessage) {
		Assert.hasText(exceptionMessage, "'exceptionMessage' must not be empty");
		Assert.doesNotContain(exceptionMessage, PLACEHOLDER_RETURN_VALUE,
				"exceptionMessage cannot contain placeholder [" + PLACEHOLDER_RETURN_VALUE + "]");
		Assert.doesNotContain(exceptionMessage, PLACEHOLDER_INVOCATION_TIME,
				"exceptionMessage cannot contain placeholder [" + PLACEHOLDER_INVOCATION_TIME + "]");
		this.exceptionMessage = exceptionMessage;
	}

	@Override
	protected Object invokeUnderTrace(final MethodInvocation invocation, final Log logger) throws Throwable {
		Object returnValue;
		try {
			returnValue = invocation.proceed();
			return returnValue;
		} catch (final Throwable ex) {
			writeToLog(logger, replacePlaceholders(exceptionMessage, invocation, null, ex, 0), ex);
			throw ex;
		}
	}

	@Override
	protected void writeToLog(final Log logger, final String message, final Throwable ex) {
		if (ex != null) {
			logger.error(message, ex);
		} else {
			logger.trace(message);
		}
	}


}
