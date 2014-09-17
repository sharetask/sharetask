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

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.sharetask.api.Constants;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class PerformanceInterceptor extends CustomizableTraceInterceptor {

	private static final long serialVersionUID = Constants.VERSION;

	/**
	 * The message for method exit.
	 */
	private String exitMessage = DEFAULT_EXIT_MESSAGE;

	/**
	 * The default message used for writing method exit messages.
	 */
	private static final String DEFAULT_EXIT_MESSAGE = "Exiting method '" + PLACEHOLDER_METHOD_NAME + "' of class ["
			+ PLACEHOLDER_TARGET_CLASS_NAME + "]";

	/**
	 * The {@code Pattern} used to match placeholders.
	 */
	private static final Pattern PATTERN = Pattern.compile("\\$\\[\\p{Alpha}+\\]");

	/**
	 * The {@code Set} of allowed placeholders.
	 */
	private static final Set<Object> ALLOWED_PLACEHOLDERS = new org.springframework.core.Constants(CustomizableTraceInterceptor.class)
			.getValues("PLACEHOLDER_");
	
	/**
	 * Set the template used for method exit log messages.
	 * This template can contain any of the following placeholders:
	 * <ul>
	 * <li>{@code $[targetClassName]}</li>
	 * <li>{@code $[targetClassShortName]}</li>
	 * <li>{@code $[argumentTypes]}</li>
	 * <li>{@code $[arguments]}</li>
	 * <li>{@code $[returnValue]}</li>
	 * <li>{@code $[invocationTime]}</li>
	 * </ul>
	 */
	@Override
	public void setExitMessage(final String exitMessage) {
		Assert.hasText(exitMessage, "'exitMessage' must not be empty");
		checkForInvalidPlaceholders(exitMessage);
		Assert.doesNotContain(exitMessage, PLACEHOLDER_EXCEPTION,
				"exitMessage cannot contain placeholder [" + PLACEHOLDER_EXCEPTION + "]");
		this.exitMessage = exitMessage;
	}

	/**
	 * Checks to see if the supplied {@code String} has any placeholders
	 * that are not specified as constants on this class and throws an
	 * {@code IllegalArgumentException} if so.
	 */
	private void checkForInvalidPlaceholders(final String message) throws IllegalArgumentException {
		final Matcher matcher = PATTERN.matcher(message);
		while (matcher.find()) {
			final String match = matcher.group();
			if (!ALLOWED_PLACEHOLDERS.contains(match)) {
				throw new IllegalArgumentException("Placeholder [" + match + "] is not valid");
			}
		}
	}
	@SuppressWarnings("PMD.AvoidCatchingThrowable")
	@Override
	protected Object invokeUnderTrace(final MethodInvocation invocation, final Log logger) throws Throwable {
		final String name = invocation.getMethod().getDeclaringClass().getName() + "." + invocation.getMethod().getName();
		final StopWatch stopWatch = new StopWatch(name);
		Object returnValue = null;
		try {
			stopWatch.start(name);
			returnValue = invocation.proceed();
			return returnValue;
		} catch (final Throwable ex) {
			if (stopWatch.isRunning()) {
				stopWatch.stop();
			}
			throw ex;
		} finally {
			if (stopWatch.isRunning()) {
				stopWatch.stop();
			}
			writeToLog(logger,
					replacePlaceholders(exitMessage, invocation, returnValue, null, stopWatch.getTotalTimeMillis()));
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
