<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<farmatic:layout pageName="home">
    <h2><fmt:message key="welcome"/></h2>
    <div>
    	<h2>Project: ${title}</h2>
    	<h2>Group: ${group}</h2>
    	<p><ul>
    	<c:forEach items="${persons}" var="person">
    		<li><p>${person.firstName} &nbsp ${person.lastName}</p></li>
    	</c:forEach></ul>
    </div>
    <div class="row">
        <div class="col-md-12">
            <spring:url value="/resources/images/logo.png" htmlEscape="true" var="petsImage"/>
            <img class="img-responsive" src="${petsImage}"/>
        </div>
    </div>
</farmatic:layout>
