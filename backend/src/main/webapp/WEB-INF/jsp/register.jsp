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
		<title><spring:message code="menu.signup" /> - ShareTa.sk</title>
	</head>
	<body>
		<div class="panel-body" data-ng-init="rootScope.currentPage='signup'">
			<div class="container" data-ng-controller="RegisterCtrl">
				<h1><spring:message code="register.title" /></h1>
				<br />
				<form name="formRegistration" novalidate class="css-form">
					<div class="alert alert-success" data-ng-show="newAccountData.result == 1">
						<a class="close" data-ng-click="newAccountData.result = 0">&times;</a>
						<spring:message code="register.msg.success" />
					</div>
					<div class="alert alert-error" data-ng-class="{'hidden':newAccountData.result == -2 || newAccountData.result == 1 || newAccountData.result == 0}">
						<a class="close" data-ng-click="newAccountData.result = 0">&times;</a>
						<spring:message code="register.msg.error" />
					</div>
					<div class="alert alert-error" data-ng-class="{'hidden':newAccountData.result == -1 || newAccountData.result == 1 || newAccountData.result == 0}">
						<a class="close" data-ng-click="newAccountData.result = 0">&times;</a>
						<spring:message code="register.msg.error.exists" />
					</div>
					<label><spring:message code="register.name" /></label>
					<input class="span3" type="text" name="firstname" placeholder="<spring:message code="register.firstname.placeholder" />" data-ng-model="newAccount.name" /> <input class="span3" type="text" name="lastname" placeholder="<spring:message code="register.lastname.placeholder" />" data-ng-model="newAccount.surName" /><br />
					<label>* <spring:message code="register.email" /></label>
					<input class="span3" type="text" name="mail" placeholder="<spring:message code="register.email.placeholder" />" data-ng-model="newAccount.username" required /><br />
					<label>* <spring:message code="register.language" /></label>
					<select class="span3" name="language" data-ng-model="language" required data-ng-options="language.code as (language.label | i18n) for language in languages"></select><br />
					<label>* <spring:message code="register.password" /></label>
					<input class="span3" type="password" name="password" placeholder="<spring:message code="register.password.placeholder" />" data-ng-model="newAccount.password1" required /><br />
					<label>* <spring:message code="register.password.check" /></label>
					<input class="span3" type="password" name="password" placeholder="<spring:message code="register.password.check.placeholder" />" data-ng-model="newAccount.password2" required /><br />
					<br />
					<button class="btn btn-inverse" data-ng-click="register()" data-ng-disabled="formRegistration.$invalid || newAccount.password1 != newAccount.password2 || newAccountData.processing"><spring:message code="register.button.submit" /><span data-ng-show="newAccountData.processing"> processing...</span></button>
					<br /><br />
					<p><spring:message code="mandatory" /></p>
				</form>
			</div>
		</div>	
	</body>
</html>				