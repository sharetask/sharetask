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

import java.io.InputStream;

import javax.mail.internet.MimeMessage;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
public class DummyJavaMailSender implements JavaMailSender {

	@Setter
	private JavaMailSender innerSender;

	/* (non-Javadoc)
	 * @see org.springframework.mail.MailSender#send(org.springframework.mail.SimpleMailMessage)
	 */
	@Override
	public void send(final SimpleMailMessage simpleMessage) {
		dumpMimeMessages(new Object[] {simpleMessage});
	}

	/* (non-Javadoc)
	 * @see org.springframework.mail.MailSender#send(org.springframework.mail.SimpleMailMessage[])
	 */
	@Override
	public void send(final SimpleMailMessage[] simpleMessages) {
		dumpMimeMessages(simpleMessages);
	}

	/* (non-Javadoc)
	 * @see org.springframework.mail.javamail.JavaMailSender#createMimeMessage()
	 */
	@Override
	public MimeMessage createMimeMessage() {
		return innerSender.createMimeMessage();
	}

	/* (non-Javadoc)
	 * @see org.springframework.mail.javamail.JavaMailSender#createMimeMessage(java.io.InputStream)
	 */
	@Override
	public MimeMessage createMimeMessage(final InputStream contentStream) {
		return innerSender.createMimeMessage(contentStream);
	}

	/* (non-Javadoc)
	 * @see org.springframework.mail.javamail.JavaMailSender#send(javax.mail.internet.MimeMessage)
	 */
	@Override
	public void send(final MimeMessage mimeMessage) {
		dumpMimeMessages(new Object[] {mimeMessage});
	}

	/* (non-Javadoc)
	 * @see org.springframework.mail.javamail.JavaMailSender#send(javax.mail.internet.MimeMessage[])
	 */
	@Override
	public void send(final MimeMessage[] mimeMessages) {
		dumpMimeMessages(mimeMessages);
	}

	/* (non-Javadoc)
	 * @see org.springframework.mail.javamail.JavaMailSender#send(org.springframework.mail.javamail.MimeMessagePreparator)
	 */
	@Override
	public void send(final MimeMessagePreparator mimeMessagePreparator) {
		dumpMimeMessages(new Object[] {mimeMessagePreparator});
	}

	/* (non-Javadoc)
	 * @see org.springframework.mail.javamail.JavaMailSender#send(org.springframework.mail.javamail.MimeMessagePreparator[])
	 */
	@Override
	public void send(final MimeMessagePreparator[] mimeMessagePreparators) {
		dumpMimeMessages(mimeMessagePreparators);
	}

	private void dumpMimeMessages(final Object[] objects) {
		for (final Object object : objects) {
			log.info(ToStringBuilder.reflectionToString(object, ToStringStyle.MULTI_LINE_STYLE));
		}
	}
}
