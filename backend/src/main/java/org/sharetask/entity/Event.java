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
package org.sharetask.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.sharetask.api.Constants;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@ToString
@Entity
@Table(name = "EVENT")
@NoArgsConstructor
public class Event extends BaseImmutableEntity implements Serializable {

	private static final long serialVersionUID = Constants.VERSION;

	public static enum EventType {
		TASK_CREATED, TASK_COMMENT_ADDED, TASK_FORWARDED, TASK_FINISHED, TASK_RENEW;
	}
	
	@Getter @Setter
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Getter @Setter
	@Enumerated(value = EnumType.STRING)
	private EventType type;

	@ElementCollection
	@CollectionTable(name = "EVENT_VALUE", joinColumns = @JoinColumn(name = "EVENT_ID"))
	@Column(name = "VALUE")
	private List<String> values;
	
	public Event(final EventType type) {
		this(type, null);
	}
	
	public Event(final EventType type, final List<String> values) {
		this.type = type;
		this.values = values;
	}
	
	public Collection<String> getValues() {
		Collection<String> result = null;
		if (values != null) {
			result = Collections.unmodifiableCollection(values);
		}
		return result;
	}
}
