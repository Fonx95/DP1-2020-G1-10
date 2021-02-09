<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName="error">

    <spring:url value="/resources/images/error.png" var="errorImage"/>
    <img src="${errorImage}"/>
	
	<div class="alert alert-danger" role="alert">
	
    	<strong><h4>Something happened...</h4></strong>
		${exception.message}
		
	</div>    

</farmatic:layout>
