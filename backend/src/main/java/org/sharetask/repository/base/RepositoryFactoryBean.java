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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class RepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable> extends
		JpaRepositoryFactoryBean<R, T, I> {

	@SuppressWarnings("rawtypes")
	protected RepositoryFactorySupport createRepositoryFactory(final EntityManager entityManager) {
		return new CrudRepositoryFactory(entityManager);
	}

	private static class CrudRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {

		private EntityManager entityManager;

		public CrudRepositoryFactory(final EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
		}

		@SuppressWarnings("unchecked")
		protected Object getTargetRepository(final RepositoryMetadata metadata) {
			if (BaseJpaRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
				return new BaseJpaRepositoryImpl<T, I>((Class<T>) metadata.getDomainType(), entityManager);
			} else {
				return super.getTargetRepository(metadata);
			}
		}

		protected Class<?> getRepositoryBaseClass(final RepositoryMetadata metadata) {
			if (BaseJpaRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
				return BaseJpaRepository.class;
			} else {
				return super.getRepositoryBaseClass(metadata);
			}
		}
	}
}