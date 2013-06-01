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

import org.junit.Test;
import org.sharetask.api.MailService;
import org.sharetask.api.dto.InvitationDTO;
import org.sharetask.data.DbUnitTest;
import org.sharetask.entity.Invitation.InvitationType;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class MailServiceTest extends DbUnitTest {
	
	@Inject
	private MailService mailService;
	
	/**
	 * Test method for {@link org.sharetask.api.MailService#sendInvitation(org.sharetask.api.dto.InvitationDTO)}.
	 */
	@Test
	public void testSendInvitation() {
		final InvitationDTO invitationDTO = new InvitationDTO();
		invitationDTO.setEmail("test3@test.com");
		invitationDTO.setInvitingUser("test1@test.com");
		invitationDTO.setEntityId(100L);
		invitationDTO.setInvitationType(InvitationType.ADD_WORKSPACE_MEMBER.name());
		invitationDTO.setInvitationCode("xxxxxxyyyyyyyy");
		mailService.sendInvitation(invitationDTO);
	}
}
