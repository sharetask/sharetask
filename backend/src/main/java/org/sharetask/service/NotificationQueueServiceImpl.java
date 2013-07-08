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

import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;

import org.sharetask.api.NotificationQueueService;
import org.sharetask.entity.NotificationQueue;
import org.sharetask.entity.NotificationQueue.NotificationType;
import org.sharetask.repository.NotificationQueueRepository;
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

	@Override
	@Transactional
	public void storeInvitation(final String from, final String[] to, final String msg) {
		final NotificationQueue notificationQueue = new NotificationQueue(null, NotificationType.EMAIL, from,
				Arrays.asList(to), msg.getBytes(), 0, NotificationQueue.Priority.HIGH, new Date());
		notificationQueueRepository.save(notificationQueue);
	}
}
