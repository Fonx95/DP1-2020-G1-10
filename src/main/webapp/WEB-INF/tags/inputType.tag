<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="name" required="true" rtexprvalue="true"
              description="Name of corresponding property in bean object" %>
<%@ attribute name="type" required="true" rtexprvalue="true"
              description="the name of the input type" %>
<%@ attribute name="holder" required="false" rtexprvalue="true"
              description="the text of the placeholder" %>
<%@ attribute name="value" required="false" rtexprvalue="true"
              description="the text of the value" %>
<%@ attribute name="label" required="true" rtexprvalue="true"
              description="Label appears in red color if input is considered as invalid after submission" %>
<%@ attribute name="readonly" required="false" rtexprvalue="true"
              description="Label appears in red color if input is considered as invalid after submission" %>
<%@ attribute name="display" required="true" rtexprvalue="true"
              description="Label appears in red color if input is considered as invalid after submission" %>

<spring:bind path="${name}">
	<c:set var="cssLabel" value="${displayleft ? 'col-sm-5 left-label' : 'col-sm-5 control-label'}"/>
	<c:set var="cssGroup" value="form-group ${status.error ? 'has-error' : '' }"/>
    <c:set var="valid" value="${not status.error and not empty status.actualValue}"/>
    <div class="${cssGroup}">
        <label class="col-sm-${display}-label">${label}</label>

        <div class="col-sm-10">
        	<input ${readonly ? 'readonly' : ''} type="${type}" class="form-control" placeholder="${holder}" name="${name}" value="${value}">
        	
        	<c:if test="${valid}">
                <span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span>
            </c:if>
            <c:if test="${status.error}">
                <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                <span class="help-inline">${status.errorMessage}</span>
            </c:if>
        </div>
    </div>
</spring:bind>