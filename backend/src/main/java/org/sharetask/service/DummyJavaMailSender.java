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

import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
public class DummyJavaMailSender implements JavaMailSender {

	/* (non-Javadoc)
	 * @see org.springframework.mail.MailSender#send(org.springframework.mail.SimpleMailMessage)
	 */
	@Override
	public void send(final SimpleMailMessage simpleMessage) throws MailException {
		log.info(String.valueOf(simpleMessage));
	}

	/* (non-Javadoc)
	 * @see org.springframework.mail.MailSender#send(org.springframework.mail.SimpleMailMessage[])
	 */
	@Override
	public void send(final SimpleMailMessage[] simpleMessages) throws MailException {
		log.info(String.valueOf(simpleMessages));
	}

	/* (non-Javadoc)
	 * @see org.springframework.mail.javamail.JavaMailSender#createMimeMessage()
	 */
	@Override
	public MimeMessage createMimeMessage() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.mail.javamail.JavaMailSender#createMimeMessage(java.io.InputStream)
	 */
	@Override
	public MimeMessage createMimeMessage(final InputStream contentStream) throws MailException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.mail.javamail.JavaMailSender#send(javax.mail.internet.MimeMessage)
	 */
	@Override
	public void send(final MimeMessage mimeMessage) throws MailException {
		log.info(String.valueOf(mimeMessage));
	}

	/* (non-Javadoc)
	 * @see org.springframework.mail.javamail.JavaMailSender#send(javax.mail.internet.MimeMessage[])
	 */
	@Override
	public void send(final MimeMessage[] mimeMessages) throws MailException {
		log.info(String.valueOf(mimeMessages));
	}

	/* (non-Javadoc)
	 * @see org.springframework.mail.javamail.JavaMailSender#send(org.springframework.mail.javamail.MimeMessagePreparator)
	 */
	@Override
	public void send(final MimeMessagePreparator mimeMessagePreparator) throws MailException {
		log.info(String.valueOf(mimeMessagePreparator));
	}

	/* (non-Javadoc)
	 * @see org.springframework.mail.javamail.JavaMailSender#send(org.springframework.mail.javamail.MimeMessagePreparator[])
	 */
	@Override
	public void send(final MimeMessagePreparator[] mimeMessagePreparators) throws MailException {
		log.info(String.valueOf(mimeMessagePreparators));
	}
}
