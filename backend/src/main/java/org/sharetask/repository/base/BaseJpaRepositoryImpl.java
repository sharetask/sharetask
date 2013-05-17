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
package org.sharetask.repository.base;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class BaseJpaRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements
		BaseJpaRepository<T, ID> {

	private final EntityManager entityManager;
	private final Class<T> entityClass;

	public BaseJpaRepositoryImpl(Class<T> entityClass, EntityManager entityManager) {
		super(entityClass, entityManager);
		this.entityManager = entityManager;
		this.entityClass = entityClass;
	}

	public T read(ID id) {
		final T entity = entityManager.find(this.entityClass, id);
		if (entity == null) {
			throw new EntityNotFoundException("Entity for class " + this.entityClass + " with id " + id
					+ " can not be found!");
		}
		return entity;
	}
}
