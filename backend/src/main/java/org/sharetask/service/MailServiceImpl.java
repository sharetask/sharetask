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

import javax.inject.Inject;

import org.sharetask.api.MailService;
import org.sharetask.api.dto.InvitationDTO;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Service
public class MailServiceImpl implements MailService {

	@Inject
	private JavaMailSender mailSender;

	@Inject
	private SimpleMailMessage templateMessage;

	public void sendInvitation(final InvitationDTO invitation) {
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setTo("michal.bocek@gmail.com");
		msg.setText("test");
		try {
			this.mailSender.send(msg);
		} catch (MailException ex) {
			// simply log it and go on...
			System.err.println(ex.getMessage());
		}
	}
}
