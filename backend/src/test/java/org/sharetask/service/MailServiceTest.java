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
import java.util.Locale;

import javax.inject.Inject;
import javax.mail.MessagingException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sharetask.api.MailService;
import org.sharetask.api.dto.InvitationDTO;
import org.sharetask.data.DbUnitTest;
import org.sharetask.entity.Invitation.InvitationType;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class MailServiceTest extends DbUnitTest {
	
	@Inject
	private MailService mailService;
    
	private Wiser testSmtp;
    
    @Before
    public void testSmtpInit(){
    	testSmtp = new Wiser();
    	testSmtp.setPort(2500);
        testSmtp.start();
        Locale.setDefault(Locale.GERMANY);
    }
    
    @After
    public void cleanup(){
        testSmtp.stop();
    }
    
	/**
	 * Test method for {@link org.sharetask.api.MailService#sendInvitation(org.sharetask.api.dto.InvitationDTO)}.
	 * @throws MessagingException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testSendInvitation() throws MessagingException, InterruptedException {
		final InvitationDTO invitationDTO = new InvitationDTO();
		invitationDTO.setEmail("test3@test.com");
		invitationDTO.setInvitingUser("test1@test.com");
		invitationDTO.setEntityId(100L);
		invitationDTO.setInvitationType(InvitationType.ADD_WORKSPACE_MEMBER.name());
		invitationDTO.setInvitationCode("xxxxxxyyyyyyyy");
		mailService.sendInvitation(invitationDTO);
		
		final List<WiserMessage> messages = testSmtp.getMessages();
		Assert.assertEquals(1, messages.size());
		Assert.assertEquals("You are added to workspace on shareta.sk", messages.get(0).getMimeMessage().getSubject());
	}
}
