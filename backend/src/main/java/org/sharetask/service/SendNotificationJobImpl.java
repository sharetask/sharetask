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

import java.util.List;

import javax.inject.Inject;

import org.sharetask.api.MailService;
import org.sharetask.api.RunnableQuartzJob;
import org.sharetask.entity.NotificationQueue;
import org.sharetask.repository.NotificationQueueRepository;
import org.springframework.stereotype.Service;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Service("sendNotificationJob")
public class SendNotificationJobImpl implements RunnableQuartzJob {

	@Inject
	private MailService mailService;
		
	@Inject
	private NotificationQueueRepository nqRepository;

	@Override
	public void doService() {
		final List<NotificationQueue> findEMailByPriority = nqRepository.findEMailByPriority();
		for (final NotificationQueue notificationQueue : findEMailByPriority) {
			mailService.sendEmail(notificationQueue.getFrom(), notificationQueue.getTo(),
					notificationQueue.getSubject(), notificationQueue.getSubject(), notificationQueue.getRetry());
		}
	}
}
