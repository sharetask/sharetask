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

<!doctype html>
<html>
	<head>
		<title><spring:message code="index.title" /></title>
	</head>
	<body>
		<div class="panel-head" data-ng-init="rootScope.currentPage='home'">
			<div class="container">
				<table class="head" role="presentation">
				<tr>
					<td>
						<div id="myCarousel" class="carousel slide">
							<ol class="carousel-indicators">
								<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
								<li data-target="#myCarousel" data-slide-to="1"></li>
								<li data-target="#myCarousel" data-slide-to="2"></li>
								<li data-target="#myCarousel" data-slide-to="3"></li>
							</ol>
							<div class="carousel-inner">
								<div class="item active">
									<h2 class="h1"><spring:message code="index.banner.1" /></h2>
									<h2><spring:message code="index.banner.1.message" /></h2>
								</div>
								<div class="item">
									<h2 class="h1"><spring:message code="index.banner.2" /></h2>
									<h2><spring:message code="index.banner.2.message" /></h2>
								</div>
								<div class="item">
									<h2 class="h1"><spring:message code="index.banner.3" /></h2>
									<h2><spring:message code="index.banner.3.message" /></h2>
								</div>
								<div class="item">
									<h2 class="h1"><spring:message code="index.banner.4" /></h2>
									<h2><spring:message code="index.banner.4.message" /></h2>
								</div>
							</div>
						</div>
					</td>
				</tr>
				</table>
			</div>
		</div>	
		<div class="panel-body">
			<div class="row">
				<div class="container large">
					<div class="span4 center"><i class="icon-check icon-11x"></i></div>
					<div class="span8">
						<h1><spring:message code="index.intro.1" /></h1>
						<br />
						<p>
							<a class="btn btn-large" href="<c:url value="/features" />"><spring:message code="index.intro.button.features" /></a>
							<a class="btn btn-large btn-inverse" href="<c:url value="/register" />"><spring:message code="index.intro.button.signup" /></a>
						</p>
					</div>
				</div>
			</div>
			<div class="row odd">
				<div class="container">
					<div class="span7">
						<h2 class="h1"><spring:message code="index.intro.2" /></h2>
						<p class="subtext"><spring:message code="index.intro.2.message" /></p>
					</div>
					<div class="span5 center"><i class="icon-desktop icon-11x"></i></div>
				</div>
			</div>
			<div class="row">
				<div class="container">
					<div class="span4 center"><i class="icon-mobile-phone icon-15x"></i></div>
					<div class="span8">
						<h2 class="h1"><spring:message code="index.intro.3" /></h2>
						<p class="subtext"><spring:message code="index.intro.3.message" /></p>
						<br />
						<h2><spring:message code="index.intro.4" /></h2>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>