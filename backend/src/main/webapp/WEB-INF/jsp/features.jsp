<%--
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
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@applicationProps['application.version']" var="applicationVersion" />

<!doctype html>
<html>
	<head>
		<title><spring:message code="menu.features" /> - ShareTa.sk</title>
	</head>
	<body>
		<div class="panel-body">
			<div class="row">
				<div class="container">
					<div class="span12">
						<h1><spring:message code="menu.features" /></h1>
						<h3><spring:message code="features.intro" /></h3>
					</div>
				</div>
			</div>
			<div class="row odd">
				<div class="container">
					<div class="span12"><img src="<c:url value="/resources-${applicationVersion}/img/features/tasks.png" />" alt="" /></div>
				</div>
			</div>
			<div class="row">
				<div class="container">
					<div class="span3"><img src="<c:url value="/resources-${applicationVersion}/img/features/select-workspace.png" />" alt="" /></div>
					<div class="span9">
						<h2><spring:message code="features.workspace.organize.title" /></h2>
						<ul>
							<li><spring:message code="features.workspace.organize.1" /></li>
							<li><spring:message code="features.workspace.organize.2" /></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="row odd">
				<div class="container">
					<div class="span6">
						<h2><spring:message code="features.workspace.manage.title" /></h2>
						<ul>
							<li><spring:message code="features.workspace.manage.1" /></li>
							<li><spring:message code="features.workspace.manage.2" /></li>
							<li><spring:message code="features.workspace.manage.3" /></li>
							<li><spring:message code="features.workspace.manage.4" /></li>
						</ul>
					</div>
					<div class="span6"><img src="<c:url value="/resources-${applicationVersion}/img/features/manage-workspace-members.png" />" alt="" /></div>
				</div>
			</div>
			<div class="row">
				<div class="container">
					<div class="span3"><img src="<c:url value="/resources-${applicationVersion}/img/features/task-queues.png" />" alt="" /></div>
					<div class="span9">
						<h2><spring:message code="features.task.queues.title" /></h2>
						<ul>
							<li><spring:message code="features.task.queues.1" /></li>
							<li><spring:message code="features.task.queues.2" /></li>
							<li><spring:message code="features.task.queues.3" /></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="row odd">
				<div class="container">
					<div class="span7">
						<h2><spring:message code="features.task.add-labels.title" /></h2>
						<ul>
							<li><spring:message code="features.task.add-labels.1" /></li>
							<li><spring:message code="features.task.add-labels.2" /></li>
						</ul>
					</div>
					<div class="span5"><img src="<c:url value="/resources-${applicationVersion}/img/features/task-add-label.png" />" alt=""/></div>
				</div>
			</div>
			<div class="row">
				<div class="container">
					<div class="span7"><img src="<c:url value="/resources-${applicationVersion}/img/features/task-labels.png" />" alt=""/></div>
					<div class="span5">
						<h2><spring:message code="features.task.filter-labels.title" /></h2>
						<ul>
							<li><spring:message code="features.task.filter-labels.1" /></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="row odd">
				<div class="container">
					<div class="span7">
						<h2><spring:message code="features.task.forward.title" /></h2>
						<ul>
							<li><spring:message code="features.task.forward.1" /></li>
						</ul>
					</div>
					<div class="span5"><img src="<c:url value="/resources-${applicationVersion}/img/features/task-forward.png" />" alt="" /></div>
				</div>
			</div>
			<div class="row">
				<div class="container">
					<div class="span7"><img src="<c:url value="/resources-${applicationVersion}/img/features/task-comments.png" />" alt="" /></div>
					<div class="span5">
						<h2><spring:message code="features.task.comments.title" /></h2>
						<ul>
							<li><spring:message code="features.task.comments.1" /></li>
							<li><spring:message code="features.task.comments.2" /></li>
							<li><spring:message code="features.task.comments.3" /></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="row odd">
				<div class="container">
					<div class="span6">
						<h2><spring:message code="features.task.duedate.title" /></h2>
						<ul>
							<li><spring:message code="features.task.duedate.1" /></li>
						</ul>
					</div>
					<div class="span6"><img src="<c:url value="/resources-${applicationVersion}/img/features/task-due-date.png" />" alt="" /></div>
				</div>
			</div>
			<div class="row">
				<div class="container">
					<div class="span7"><img src="<c:url value="/resources-${applicationVersion}/img/features/tasks-batch-operations.png" />" alt="" /></div>
					<div class="span5">
						<h2><spring:message code="features.task.batch.title" /></h2>
						<ul>
							<li><spring:message code="features.task.batch.1" /></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>