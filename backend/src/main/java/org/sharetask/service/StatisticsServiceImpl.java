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
package org.sharetask.service;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;
import org.sharetask.api.StatisticsService;
import org.sharetask.api.dto.StatisticsDataDTO;
import org.sharetask.api.dto.StatisticsOverviewDTO;
import org.sharetask.repository.TaskRepository;
import org.sharetask.repository.UserInformationRepository;
import org.sharetask.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Service("statisticsService")
@Transactional(readOnly = true)
public class StatisticsServiceImpl implements StatisticsService {

	@Inject
	private UserInformationRepository userInformationRepository;

	@Inject
	private WorkspaceRepository workspaceRepository;
	
	@Inject
	private TaskRepository taskRepository;
	
	public StatisticsOverviewDTO getOverview() {
		StatisticsOverviewDTO statisticsOverviewDTO = new StatisticsOverviewDTO();
		statisticsOverviewDTO.setStatisticsTotal(getStatisticsTotal());
		statisticsOverviewDTO.setStatisticsPerLastYear(getStatisticsPerLastYear());
		statisticsOverviewDTO.setStatisticsPerLastWeek(getStatisticsPerLastWeek());
		statisticsOverviewDTO.setStatisticsPerLastDay(getStatisticsPerLastDay());
		statisticsOverviewDTO.setStatisticsPerLastHour(getStatisticsPerLastHour());
		return statisticsOverviewDTO;
	}

	private StatisticsDataDTO getStatisticsPerLastHour() {
		Calendar calendar = Calendar.getInstance();
		Date date = DateUtils.truncate(calendar.getTime(), Calendar.HOUR_OF_DAY);
		return new StatisticsDataDTO.Builder()
				.usersCount(userInformationRepository.getCountCreatedAfter(date))
				.workspacesCount(workspaceRepository.getCountCreatedAfter(date))
				.tasksCount(taskRepository.getCountCreatedAfter(date))
				.build();
	}

	private StatisticsDataDTO getStatisticsPerLastDay() {
		Calendar calendar = Calendar.getInstance();
		Date date = DateUtils.truncate(calendar.getTime(), Calendar.DATE);
		return new StatisticsDataDTO.Builder()
				.usersCount(userInformationRepository.getCountCreatedAfter(date))
				.workspacesCount(workspaceRepository.getCountCreatedAfter(date))
				.tasksCount(taskRepository.getCountCreatedAfter(date))
				.build();
	}

	private StatisticsDataDTO getStatisticsPerLastYear() {
		Calendar calendar = Calendar.getInstance();
		Date date = DateUtils.truncate(calendar.getTime(), Calendar.YEAR);
		return new StatisticsDataDTO.Builder()
				.usersCount(userInformationRepository.getCountCreatedAfter(date))
				.workspacesCount(workspaceRepository.getCountCreatedAfter(date))
				.tasksCount(taskRepository.getCountCreatedAfter(date))
				.build();
	}

	private StatisticsDataDTO getStatisticsPerLastWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date date = DateUtils.truncate(calendar.getTime(), Calendar.DATE);
		return new StatisticsDataDTO.Builder()
				.usersCount(userInformationRepository.getCountCreatedAfter(date))
				.workspacesCount(workspaceRepository.getCountCreatedAfter(date))
				.tasksCount(taskRepository.getCountCreatedAfter(date))
				.build();
	}

	private StatisticsDataDTO getStatisticsTotal() {
		return new StatisticsDataDTO.Builder()
				.usersCount(userInformationRepository.getTotalCount())
				.workspacesCount(workspaceRepository.getTotalCount())
				.tasksCount(taskRepository.getTotalCount())
				.build();
	}
}
