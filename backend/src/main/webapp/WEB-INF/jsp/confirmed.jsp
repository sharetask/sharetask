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

<%@ page import="org.pac4j.oauth.client.Google2Client"%>
<%@ page import="org.pac4j.oauth.client.FacebookClient"%>
<%@ page import="org.pac4j.core.context.J2EContext"%>
<%@ page import="org.pac4j.core.context.WebContext"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@applicationProps['application.version']" var="applicationVersion" />

<%
    WebContext context = new J2EContext(request, response); 
	Google2Client gClient= (Google2Client) application.getAttribute("GoogleClient");
    String redirectionGoogleUrl = gClient.getRedirectionUrl(context);
%>

<!doctype html>
<html>
	<head>
		<title><spring:message code="invitation.title" /> - ShareTa.sk</title>
	</head>
	<body>
		<!-- slide header -->
		<div class="panel-head">
			<div class="container">
				<table class="head">
				<tr>
					<td class="login" data-ng-controller="AuthCtrl">
						<table style="width:100%;">
						<tr>
							<td style="width:33%;padding-right:15px;border-right:1px solid rgba(255,255,255,0.5);">
								<p><spring:message code="login.msg.3rdparty" /></p>
							</td>
							<td style="width:34%;padding:0 15px;border-right:1px solid rgba(255,255,255,0.5);">
								<p><spring:message code="login.msg.signup" /></p>
							</td>
							<td style="width:33%;padding-left:15px;">
								<p><spring:message code="login.msg.sharetask" /></p>
							</td>
						</tr>
						<tr>
							<td style="padding-right:15px;border-right:1px solid rgba(255,255,255,0.5);">
								<p><a href="<%= redirectionGoogleUrl%>" class="btn btn-inverse"><i class="icon-google-plus"></i> <spring:message code="login.account.google" /></a></p><br />
								<!--<p><a href="" class="btn btn-inverse"><i class="icon-facebook"></i> <spring:message code="login.account.facebook" /></a></p><br />
								<p><a href="" class="btn btn-inverse"><i class="icon-twitter"></i> <spring:message code="login.account.twitter" /></a></p>-->
							</td>
							<td style="padding:0 15px;border-right:1px solid rgba(255,255,255,0.5);">
								<a href="<c:url value="/register" />" class="btn btn-inverse"><spring:message code="account.create.button" /></a><br />
							</td>
							<td style="padding-left:15px;">
								<form name="formLogin" novalidate class="css-form">
									<div class="alert alert-error" data-ng-class="{'hidden':!loginData || !loginData.result || loginData.result == 1 || loginData.result == 0}">
										<a class="close" data-ng-click="loginData.result = 0">&times;</a>
										<spring:message code="login.error" />
									</div>
									<input type="text" name="username" placeholder="<spring:message code="login.username.placeholder" />" data-ng-model="user.username" ui-keypress="{enter:'login()'}" required auto-fillable-field /><br />
									<input type="password" name="password" placeholder="<spring:message code="login.password.placeholder" />" data-ng-model="user.password" ui-keypress="{enter:'login()'}" required auto-fillable-field /><br />
									<button class="btn btn-inverse" data-ng-click="login()" data-ng-disabled="formLogin.$invalid || loginData.processing"><spring:message code="login.button.submit" /></button>
								</form>
								<p class="pull-right"><small><a href="<c:url value="/forgot_password" />"><spring:message code="forgot.password.title" /></a></small></p>
							</td>
						</tr>
						</table>
					</td>
				</tr>
				</table>
			</div>
		</div>
		<!-- page body -->
		<div class="panel-body">
			<div class="container">
				<h2><spring:message code="invitation.title" /></h2>
				<c:choose>
					<c:when test="${param.error.equals('notexists')}">
						<spring:message code="invitation.result.notexists" />
					</c:when>
					<c:when test="${param.error.equals('userNotExists')}">
						<spring:message code="invitation.result.userNotExists" />
					</c:when>
					<c:otherwise>
						<spring:message code="invitation.result.ok" />
					</c:otherwise>
				</c:choose>
			</div>
		</div>	
	</body>
</html>				