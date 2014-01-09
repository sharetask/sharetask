				<table class="head">
				<tr>
					<td class="login" ng-controller="AuthCtrl">
						<table style="width:100%;">
						<tr>
							<td style="width:33%;padding-right:15px;border-right:1px solid rgba(255,255,255,0.5);">
								<p><spring:message code="login.msg.3rdparty" /></p>
							</td>
							<td style="width:34%;padding:0 15px;border-right:1px solid rgba(255,255,255,0.5);">
								<p><spring:message code="login.msg.sharetask" /></p>
							</td>
							<td style="width:33%;padding-left:15px;">
								<p><spring:message code="login.msg.signup" /></p>
							</td>
						</tr>
						<tr>
							<td style="padding-right:15px;border-right:1px solid rgba(255,255,255,0.5);">
								<p><a href="<%= redirectionGoogleUrl%>" class="btn btn-inverse"><i class="icon-google-plus"></i> <spring:message code="login.account.google" /></a></p><br />
								<!--<p><a href="" class="btn btn-inverse"><i class="icon-facebook"></i> <spring:message code="login.account.facebook" /></a></p><br />
								<p><a href="" class="btn btn-inverse"><i class="icon-twitter"></i> <spring:message code="login.account.twitter" /></a></p>-->
							</td>
							<td style="padding:0 15px;border-right:1px solid rgba(255,255,255,0.5);">
								<form name="formLogin" novalidate class="css-form">
									<div class="alert alert-error" ng-class="{'hidden':!loginData || !loginData.result || loginData.result == 1 || loginData.result == 0}">
										<a class="close" ng-click="loginData.result = 0">&times;</a>
										<spring:message code="login.error" />
									</div>
									<input type="text" name="username" placeholder="<spring:message code="login.username.placeholder" />" ng-model="user.username" ui-keypress="{enter:'login()'}" required auto-fillable-field /><br />
									<input type="password" name="password" placeholder="<spring:message code="login.password.placeholder" />" ng-model="user.password" ui-keypress="{enter:'login()'}" required auto-fillable-field /><br />
									<button class="btn btn-inverse" ng-click="login()" ng-disabled="formLogin.$invalid || loginData.processing"><spring:message code="login.button.submit" /></button>
								</form>
							</td>
							<td style="padding-left:15px;">
								<a href="<c:url value="/register" />" class="btn btn-inverse"><spring:message code="account.create.button" /></a><br />
							</td>
						</tr>
						</table>
					</td>
				</tr>
				</table>