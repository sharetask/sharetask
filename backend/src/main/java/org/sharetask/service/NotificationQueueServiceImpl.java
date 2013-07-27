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

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.sharetask.api.NotificationQueueService;
import org.sharetask.entity.NotificationQueue;
import org.sharetask.entity.NotificationQueue.NotificationType;
import org.sharetask.entity.Priority;
import org.sharetask.entity.Priority.PriorityType;
import org.sharetask.repository.NotificationQueueRepository;
import org.sharetask.repository.PriorityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class NotificationQueueServiceImpl implements NotificationQueueService {

	@Inject
	private NotificationQueueRepository notificationQueueRepository;

	@Inject
	private PriorityRepository priorityRepository;

	@Override
	@Transactional
	public void storeInvitation(final String from, final List<String> to, final String subject, final String msg,
			final int retry) {
		final Priority priorityHigh = priorityRepository.read(PriorityType.HIGH.name());
		final NotificationQueue notificationQueue = new NotificationQueue(null, NotificationType.EMAIL, from, 
				to, subject, msg.getBytes(), retry, priorityHigh, new Date());
		
		notificationQueueRepository.save(notificationQueue);
	}
}
