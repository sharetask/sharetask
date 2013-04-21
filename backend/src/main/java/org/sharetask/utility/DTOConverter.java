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

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public final class DTOConverter {
	
	private DTOConverter() {
	}
	
	public static <T> T convert(final Object source, final Class<T> destinationClass) {
		final Mapper instance = DozerBeanMapperSingletonWrapper.getInstance();
		return instance.map(source, destinationClass);
	}

	public static void convert(final Object source, final Object destination) {
		final Mapper instance = DozerBeanMapperSingletonWrapper.getInstance();
		instance.map(source, destination);
	}
	
	public static <T, TT> List<T> convertList(final List<TT> list, final Class<T> destinationClass) {
		List<T> result = null;
		if (list != null) {
			final Mapper instance = DozerBeanMapperSingletonWrapper.getInstance();
			result = new ArrayList<T>(list.size());
			for (TT sourceObject : list) {
				final T destination = instance.map(sourceObject, destinationClass);
				result.add(destination);
			}
		}
		return result;
	}
}
