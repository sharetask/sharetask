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
package org.sharetask.repository;

import java.util.Date;

import org.sharetask.entity.UserInformation;
import org.sharetask.repository.base.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public interface UserInformationRepository extends BaseJpaRepository<UserInformation, String> {
	
	/**
	 * Find user by user name.
	 * @param id
	 * @return
	 */
	UserInformation findByUsername(final String username);
	
	/**
	 * Gets the total count.
	 *
	 * @return the total count
	 */
	@Query("SELECT count(ui) FROM UserInformation ui")
	Long getTotalCount();

	/**
	 * Gets the last week count.
	 *
	 * @return the last week count
	 */
	@Query("SELECT count(ui) FROM UserInformation ui WHERE ui.createdOn > ?1")
	Long getCountCreatedAfter(final Date date);
}
