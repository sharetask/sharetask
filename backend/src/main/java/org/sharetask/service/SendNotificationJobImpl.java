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

import javax.annotation.Resource;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.sharetask.api.MailService;
import org.sharetask.api.RunnableQuartzJob;
import org.sharetask.api.SendNotificationService;
import org.sharetask.entity.NotificationQueue;
import org.sharetask.repository.NotificationQueueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
@Service("sendNotificationJob")
@Transactional(readOnly = true)
public class SendNotificationJobImpl implements RunnableQuartzJob, SendNotificationService {

	@Inject
	private MailService mailService;

	@Inject
	private NotificationQueueRepository nqRepository;

	@Resource(name = "sendNotificationJob")
	private SendNotificationService self;

	@Override
	public void doService() {
		final List<NotificationQueue> findEMailByPriority = nqRepository.findEMailByPriority();
		log.info("There will be {} email(s) proceeded", findEMailByPriority.size());
		for (final NotificationQueue notificationQueue : findEMailByPriority) {
			self.sendNotification(notificationQueue.getId());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void sendNotification(final Long notificationQueueId) {
		final NotificationQueue notificationQueue = nqRepository.read(notificationQueueId);
		nqRepository.delete(notificationQueue);
		mailService.sendEmail(notificationQueue.getFrom(), notificationQueue.getTo(), notificationQueue.getSubject(),
				notificationQueue.getSubject(), notificationQueue.getRetry());
	}
}
