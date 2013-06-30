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
package org.sharetask.utility;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.sharetask.api.RunnableQuartzJob;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class GenericQuartzJob extends QuartzJobBean {

	private String batchProcessorName;

	public String getBatchProcessorName() {
		return batchProcessorName;
	}

	public void setBatchProcessorName(final String name) {
		batchProcessorName = name;
	}

	@Override
	protected void executeInternal(final JobExecutionContext jobCtx) throws JobExecutionException {
		try {
			final SchedulerContext schedCtx = jobCtx.getScheduler().getContext();

			final ApplicationContext appCtx = (ApplicationContext) schedCtx.get("applicationContext");
			final RunnableQuartzJob proc = (RunnableQuartzJob) appCtx.getBean(batchProcessorName);
			proc.doService();
		} catch (final Exception ex) {
			ex.printStackTrace();
			throw new JobExecutionException("Unable to execute batch job: " + batchProcessorName, ex);
		}
	}
}