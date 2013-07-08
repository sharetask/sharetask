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

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.sharetask.api.MailService;
import org.sharetask.api.NotificationQueueService;
import org.sharetask.api.TemplateMessageService;
import org.sharetask.api.TemplateMessageService.TemplateList;
import org.sharetask.api.dto.InvitationDTO;
import org.sharetask.entity.Invitation.InvitationType;
import org.sharetask.entity.User;
import org.sharetask.entity.Workspace;
import org.sharetask.repository.UserRepository;
import org.sharetask.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
@Service
public class MailServiceImpl implements MailService {

	@Inject
	private JavaMailSender mailSender;

	@Inject
	private TemplateMessageService templateMessageService;

	@Inject
	private SimpleMailMessage templateMessage;
	
	@Inject
	private UserRepository userRepository;

	@Inject
	private NotificationQueueService notificationQueueService;

	@Inject
	private WorkspaceRepository workspaceRepository;
	
	@Value("#{applicationProps['applicationUrl']}")
	private String applicationUrl;

	@Override
	public void sendInvitation(final InvitationDTO invitation) {
		final SimpleMailMessage msg = new SimpleMailMessage(templateMessage);
		msg.setTo(invitation.getEmail());
		final Map<String, Object> model = prepareInvitationMode(invitation);
		final InvitationType invitationType = InvitationType.valueOf(invitation.getInvitationType());
		switch (invitationType) {
			case ADD_WORKSPACE_MEMBER:
				msg.setText(templateMessageService.prepareMessage(TemplateList.WORKSPACE_INVITATION, model, null));
				break;
			case USER_REGISTRATION:
				msg.setText(templateMessageService.prepareMessage(TemplateList.USER_REGISTRATION_INVITATION, model, null));
				break;
		}
		try {
			mailSender.send(msg);
		} catch (final MailException ex) {
			log.error("Problem in sending email notification:", ex.getMessage());
			notificationQueueService.storeInvitation(msg.getFrom(), msg.getTo(), msg.getText());
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
		return model;
	}

	private Map<String, Object> prepareAddWorkspaceMember(final InvitationDTO invitation) {
		final Map<String, Object> model = new HashMap<String, Object>();
		final User invitingUser = userRepository.findOne(invitation.getInvitingUser());
		model.put("userName", invitingUser.getName());
		model.put("userSurName", invitingUser.getSurName());
		final Workspace workspace = workspaceRepository.findOne(invitation.getEntityId());		
		model.put("workspaceName", workspace.getTitle());
		model.put("confimationLink", applicationUrl + "?code=" + invitation.getInvitationCode());
		return model;
	}
}
