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
package org.sharetask.api.dto;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@ToString
public class StatisticsDataDTO {

	@Getter
	private Long usersCount;

	@Getter
	private Long workspacesCount;

	@Getter
	private Long tasksCount;

	public static class Builder {
		private Long usersCount;
		private Long workspacesCount;
		private Long tasksCount;

		public Builder usersCount(Long usersCount) {
			this.usersCount = usersCount;
			return this;
		}

		public Builder workspacesCount(Long workspacesCount) {
			this.workspacesCount = workspacesCount;
			return this;
		}

		public Builder tasksCount(Long tasksCount) {
			this.tasksCount = tasksCount;
			return this;
		}

		public StatisticsDataDTO build() {
			return new StatisticsDataDTO(this);
		}
	}

	private StatisticsDataDTO(Builder builder) {
		this.usersCount = builder.usersCount;
		this.workspacesCount = builder.workspacesCount;
		this.tasksCount = builder.tasksCount;
	}
}
