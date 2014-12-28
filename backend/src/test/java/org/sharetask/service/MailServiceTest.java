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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.sharetask.api.MailService;
import org.sharetask.api.dto.InvitationDTO;
import org.sharetask.data.ServiceUnitTest;
import org.sharetask.entity.Invitation.InvitationType;
import org.sharetask.entity.Language;
import org.sharetask.service.mail.DummyJavaMailSender;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class MailServiceTest extends ServiceUnitTest {

	@Inject
	private MailService mailService;

	@Inject
	private DummyJavaMailSender mailSender;
	
	/**
	 * Test method for {@link org.sharetask.api.MailService#sendInvitation(org.sharetask.api.dto.InvitationDTO)}.
	 * @throws MessagingException
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	@Test
	public void testSendInvitation() throws MessagingException, InterruptedException, IOException {
		final InvitationDTO invitationDTO = new InvitationDTO();
		invitationDTO.setEmail("test3@test.com");
		invitationDTO.setInvitingUser("test1@test.com");
		invitationDTO.setEntityId(100L);
		invitationDTO.setInvitationType(InvitationType.ADD_WORKSPACE_MEMBER.name());
		invitationDTO.setInvitationCode("xxxxxxyyyyyyyy");
		mailService.sendInvitation(invitationDTO);

		final List<MimeMessage> messages = mailSender.getMimeMessages();
		mailSender.clearMimeMessages();
		assertThat(messages.size()).as("Count of emails in queue").isEqualTo(1);
		assertThat(messages.get(0).getSubject()).as("Email subject").isEqualTo("You are added to workspace on shareta.sk");
		assertThat((String)messages.get(0).getContent()).as("Email content").contains("you are invited to connect to");
	}
	
	/**
	 * Test method for {@link org.sharetask.api.MailService#sendInvitation(org.sharetask.api.dto.InvitationDTO)}.
	 * @throws MessagingException
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	@Test
	public void testSendInvitationCs() throws MessagingException, InterruptedException, IOException {
		final InvitationDTO invitationDTO = new InvitationDTO();
		invitationDTO.setEmail("test3@test.com");
		invitationDTO.setInvitingUser("test1@test.com");
		invitationDTO.setEntityId(100L);
		invitationDTO.setInvitationType(InvitationType.ADD_WORKSPACE_MEMBER.name());
		invitationDTO.setInvitationCode("xxxxxxyyyyyyyy");
		invitationDTO.setLanguage(Language.CS.getCode());
		mailService.sendInvitation(invitationDTO);

		final List<MimeMessage> messages = mailSender.getMimeMessages();
		mailSender.clearMimeMessages();
		assertThat(messages.size()).as("Count of emails in queue").isEqualTo(1);
		assertThat(messages.get(0).getSubject()).as("Email subject").isEqualTo("Stal jste se členem projektu na shareta.sk");
		assertThat((String)messages.get(0).getContent()).as("Email content").contains("by jsi pozván aby jsi se připojil k aplikaci");
	}
	
}
