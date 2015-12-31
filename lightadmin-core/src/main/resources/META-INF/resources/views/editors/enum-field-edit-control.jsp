<%@ page import="org.lightadmin.core.persistence.metamodel.PersistentPropertyType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="attributeMetadata" type="org.springframework.data.mapping.PersistentProperty" scope="request"/>
<c:set var="enumProperty" value="<%= PersistentPropertyType.forPersistentProperty(attributeMetadata) == PersistentPropertyType.ENUM%>"/>

<select name="${field}">
    <c:if test="${not enumProperty}"><option value=""></option></c:if>
    <c:forEach var="element" items="${elements}">
        <option value="${element.value}">
            <c:choose>
                <c:when test="${element.i18n == true}"><spring:message code="${element.label}"/></c:when>
                <c:otherwise>${element.label}</c:otherwise>
            </c:choose>
        </option>
    </c:forEach>
</select>