<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<farmatic:layout pageName="error">
	<spring:url value="/resources/images/error.png" var="errorImage"/>
    <img src="${errorImage}"/>
    
    <div class="alert alert-danger" role="alert">
    
	    <strong><h2>${titulo}</h2></strong>
	
	    ${descripcion}
	
	</div>
    
    <br>
    
    <c:if test="${codigo == '500'}">
    	<p>${exception.message}</p>
    </c:if>
    
</farmatic:layout>