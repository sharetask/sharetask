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
		<title><spring:message code="menu.signin" /> - ShareTa.sk</title>
	</head>
	<body>
		<div class="panel-body" data-ng-init="rootScope.currentPage='signin'">
			<div class="container" data-ng-controller="ForgotPasswordCtrl">
				<h1><spring:message code="forgot.password.title" /></h1>
				<br />
				<p><spring:message code="forgot.password.instructions" /></p>
				<br />
				<form name="formForgotPassword" novalidate class="css-form">
					<div class="alert alert-success" data-ng-show="forgotPasswordData.result == 1">
						<a class="close" data-ng-click="forgotPasswordData.result = 0">&times;</a>
						<spring:message code="forgot.password.msg.success" />
					</div>
					<div class="alert alert-error" data-ng-class="{'hidden':forgotPasswordData.result == 1 || forgotPasswordData.result == 0}">
						<a class="close" data-ng-click="forgotPasswordData.result = 0">&times;</a>
						<spring:message code="forgot.password.msg.error" />
					</div>
					<label>* <spring:message code="forgot.password.email" /></label>
					<input class="span4" type="email" name="username" placeholder="<spring:message code="forgot.password.email.placeholder" />" data-ng-model="username" required /><br />
					<br />
					<button class="btn btn-inverse" data-ng-click="send()" ng-disabled="formForgotPassword.$invalid || forgotPasswordData.processing"><spring:message code="forgot.password.button.submit" /><span data-ng-show="forgotPasswordData.processing"> processing...</span></button>
					<br /><br />
					<p><spring:message code="mandatory" /></p>
				</form>
			</div>
		</div>
	</body>
</html>