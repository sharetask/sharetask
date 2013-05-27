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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.sharetask.api.TemplateMessageService;
import org.springframework.stereotype.Service;

import com.floreysoft.jmte.Engine;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
@Service
public class MessageTemplateServiceImpl implements TemplateMessageService {

	@Inject
	private Engine templateEngine;  
	
	private final Map<TemplateList, String> templates = new HashMap<TemplateList, String>();

	{
		this.templates.put(TemplateList.INVITATION, "templates/invitation.jmte");
	};

	/*
	 * (non-Javadoc)
	 * @see org.sharetask.api.TemplateMessageService#prepareMessage(java.lang.String, java.lang.Object)
	 */
	@Override
	public String prepareMessage(final TemplateList template, final Map<String, Object> model) {
		final String loadTemplate = loadTemplate(this.templates.get(template));
		return this.templateEngine.transform(loadTemplate, model);
	}

	private String loadTemplate(final String resourceName) {
		final StringBuilder result = new StringBuilder();
		final InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourceName);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String strLine;
		try {
			while ((strLine = br.readLine()) != null) {
				result.append(strLine);
			}
			br.close();
			br = null;
		} catch (final IOException e) {
			log.error("Cannot load default violation properties.");
			throw new RuntimeException("Can not load resource file: " + resourceName, e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (final IOException e) {
					log.error("Can not close imput stream.");
				}
			}
		}
		return result.toString();
	}
}
