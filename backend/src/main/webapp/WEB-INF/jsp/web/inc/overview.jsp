<%
	// get selected locale first from cookie then from request locale
	Cookie[] cookies = request.getCookies();
	String locale = "";
	String language = "";
	if (cookies == null) {
		pageContext.setAttribute("language", request.getLocale().getLanguage().toLowerCase());
		pageContext.setAttribute("locale", request.getLocale().getLanguage().toLowerCase());
	} else {
		for(Cookie cookie : cookies) { 
			if (cookie.getName().equals("locale")) {
				locale = cookie.getValue();
				pageContext.setAttribute("locale", locale);
				pageContext.setAttribute("language", locale);
				break;
			}
		}
		if (locale.isEmpty()) {
			pageContext.setAttribute("language", request.getLocale().getLanguage().toLowerCase());
			pageContext.setAttribute("locale", request.getLocale().getLanguage().toLowerCase());
			if (request.getLocale().getCountry().length() > 0) {
				pageContext.setAttribute("country", request.getLocale().getCountry().toLowerCase());
				pageContext.setAttribute("locale", request.getLocale().getLanguage().toLowerCase() + "-" + request.getLocale().getCountry().toLowerCase());
			}
		}
	}
%>
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
						<h1><spring:message code="index.intro.2" /></h1>
					</div>
					<div class="span5 center"><i class="icon-desktop icon-11x"></i></div>
				</div>
			</div>
			<div class="row">
				<div class="container">
					<div class="span4 center"><i class="icon-mobile-phone icon-15x"></i></div>
					<div class="span8">
						<h1><spring:message code="index.intro.3" /></h1>
						<br />
						<p>
							<a href="https://play.google.com/store/apps/details?id=sk.shareta.android"><img alt="Nyní na Google Play" src="https://developer.android.com/images/brand/<c:out value="${language}" />_generic_rgb_wo_60.png" /></a>
						</p>
					</div>
				</div>
			</div>
		</div>