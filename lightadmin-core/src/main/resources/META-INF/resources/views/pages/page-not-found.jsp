<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="bean" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:message code="back.to.dashboard" var="back_to_dashboard"/>

<div class="wrapper">
	<div class="errorPage">
		<h2 class="red errorTitle"><span><bean:message key="something.went.wrong.here"/></span></h2>

		<h1>404</h1>
		<span class="bubbles"></span>

		<p><bean:message key="oops.sorry.an.error.has.occured"/><br/><bean:message key="page.not.found"/></p>

		<div class="backToDash"><a href="<light:url value='/dashboard'/>" title="${back_to_dashboard}"
								   class="seaBtn button">${back_to_dashboard}</a></div>
	</div>
</div>