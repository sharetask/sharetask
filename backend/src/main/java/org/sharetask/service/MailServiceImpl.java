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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;

import org.sharetask.api.MailService;
import org.sharetask.api.NotificationQueueService;
import org.sharetask.api.TemplateMessageService;
import org.sharetask.api.TemplateMessageService.TemplateList;
import org.sharetask.api.dto.InvitationDTO;
import org.sharetask.entity.Invitation.InvitationType;
import org.sharetask.entity.UserAuthentication;
import org.sharetask.entity.Workspace;
import org.sharetask.repository.UserAuthenticationRepository;
import org.sharetask.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
@Service
@Transactional
public class MailServiceImpl implements MailService {

	@Inject
	private JavaMailSender mailSender;

	@Inject
	private TemplateMessageService templateMessageService;

	@Inject
	private UserAuthenticationRepository userRepository;

	@Inject
	private NotificationQueueService notificationQueueService;

	@Inject
	private WorkspaceRepository workspaceRepository;

	@Inject
	private MessageSource messageSource;

	@Value("#{applicationProps['application.url']}")
	private String applicationUrl;

	@Value("#{applicationProps['application.mail.noreply']}")
	private String noreplyMail;

	@Override
	public void sendInvitation(final InvitationDTO invitation) {
		final Map<String, Object> model = prepareInvitationMode(invitation);
		final InvitationType invitationType = InvitationType.valueOf(invitation.getInvitationType());
		String mailMessage;
		String mailSubject;

		switch (invitationType) {
			case ADD_WORKSPACE_MEMBER:
				mailMessage = templateMessageService.prepareMessage(TemplateList.WORKSPACE_INVITATION, model, null);
				mailSubject = messageSource.getMessage("notification.mail.subject.addWorkspaceMember", null,
						LocaleContextHolder.getLocale());
				break;
			case USER_REGISTRATION:
				mailMessage = templateMessageService.prepareMessage(TemplateList.USER_REGISTRATION_INVITATION, model, null);
				mailSubject = messageSource.getMessage("notification.mail.subject.userRegistration", null,
						LocaleContextHolder.getLocale());
				break;
			default:
				throw new IllegalStateException("Invitation type for sending email isn't implemented!");
		}

		final List<String> to = new ArrayList<String>();
		to.add(invitation.getEmail());
		sendEmail(noreplyMail, to, mailSubject, mailMessage, 0);
	}

	@Override
	public void sendEmail(final String from, final List<String> to, final String subject, final String msq, final int retry) {
		final MimeMessage message = mailSender.createMimeMessage();
		final MimeMessageHelper helper = new MimeMessageHelper(message);

		try {
			helper.setFrom(noreplyMail);
			helper.setTo(to.toArray(new String[] {}));
			helper.setSubject(subject);
			helper.setText(msq, true /* html */);
			mailSender.send(message);
		} catch (final MailException ex) {
			log.error("Problem in sending email notification:", ex);
			notificationQueueService.storeInvitation(noreplyMail, to, subject, msq, retry + 1);
		} catch (final MessagingException e) {
			throw new IllegalStateException("Wrong mail message format: ", e);
		}
	}

	private Map<String, Object> prepareInvitationMode(final InvitationDTO invitation) {
		final InvitationType invitationType = InvitationType.valueOf(invitation.getInvitationType());
		Map<String, Object> result = null;
		switch (invitationType){
			case ADD_WORKSPACE_MEMBER:
				result = prepareAddWorkspaceMember(invitation);
				break;
			case USER_REGISTRATION:
				result = prepareUserRegistration(invitation);
				break;
		}
		return result;
	}

	private Map<String, Object> prepareUserRegistration(final InvitationDTO invitation) {
		final Map<String, Object> model = new HashMap<String, Object>();
		model.put("confimationLink", applicationUrl + "/api/user/confirm?code=" + invitation.getInvitationCode());
		model.put("applicationLink", applicationUrl);
		return model;
	}

	private Map<String, Object> prepareAddWorkspaceMember(final InvitationDTO invitation) {
		final Map<String, Object> model = new HashMap<String, Object>();
		final UserAuthentication invitingUser = userRepository.findOne(invitation.getInvitingUser());
		model.put("userName", invitingUser.getUserInfo().getName());
		model.put("userSurName", invitingUser.getUserInfo().getSurName());
		final Workspace workspace = workspaceRepository.findOne(invitation.getEntityId());
		model.put("workspaceName", workspace.getTitle());
		model.put("confimationLink", applicationUrl + "?code=" + invitation.getInvitationCode());
		return model;
	}
}
