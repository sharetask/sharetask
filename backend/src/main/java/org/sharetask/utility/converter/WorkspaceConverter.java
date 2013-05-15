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
package org.sharetask.utility.converter;

import java.util.ArrayList;
import java.util.Collection;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.sharetask.api.dto.UserDTO;
import org.sharetask.api.dto.WorkspaceDTO;
import org.sharetask.entity.User;
import org.sharetask.entity.Workspace;
import org.sharetask.utility.DTOConverter;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class WorkspaceConverter implements CustomConverter {

	/* (non-Javadoc)
	 * @see org.dozer.CustomConverter#convert(java.lang.Object, java.lang.Object, java.lang.Class, java.lang.Class)
	 */
	@Override
	public Object convert(final Object destination, final Object source, final Class<?> destClass, 
	                      final Class<?> sourceClass) {
		Object result = null;
		if (source instanceof WorkspaceDTO) {
			final Workspace Workspace = convert((WorkspaceDTO) source, (Workspace) destination);
			result = Workspace;
		} else if (source instanceof Workspace) {
			final WorkspaceDTO Workspace = convert((Workspace) source);
			result = Workspace;
		} else if (source != null) {
			throw new MappingException("Converter WorkspaceConverter used incorrectly. Arguments passed were:"
					+ destination + " and " + source);
		}
		return result;
	}

	private WorkspaceDTO convert(final Workspace source) {
		final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
		workspaceDTO.setId(source.getId());
		workspaceDTO.setTitle(source.getTitle());
		workspaceDTO.setDescription(source.getDescription());
		if (source.getOwner() != null) {
			workspaceDTO.setOwner(DTOConverter.convert(source.getOwner(), UserDTO.class));
		}
		if (source.getMembers() != null) {
			final Collection<User> members = source.getMembers();
			workspaceDTO.setMembers(DTOConverter.convertList(new ArrayList<User>(members), UserDTO.class));
		}
		return workspaceDTO;
	}

	private Workspace convert(final WorkspaceDTO sourceDTO, final Workspace destination) {
		final Workspace workspace = destination == null ? new Workspace() : destination;
		workspace.setTitle(sourceDTO.getTitle());
		workspace.setDescription(sourceDTO.getDescription());
		if (sourceDTO.getOwner() != null) {
			workspace.setOwner(new User(sourceDTO.getOwner().getUsername()));
		}
		return workspace;
	}
}
