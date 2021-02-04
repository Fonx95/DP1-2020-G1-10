<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<farmatic:layout pageName= "usuario">
	<div style=" margin-left: 30%; margin-right: 30%;">
		<h2>Cambiar contraseña</h2>
		<form:form modelAttribute="user" class="form-horizontal" id="edit-password-form">
			<farmatic:inputType label="Usuario:" name="username" type="text" readonly="true" value="${user.username}" display="5 left"/>
			<farmatic:inputType label="Contraseña:" name="password" type="password" value="${user.password}" holder="Antigua contraseña" display="5 left"/>
			<farmatic:inputType label="Nueva contraseña:" name="newPassword"  type="password" value="${user.newPassword}" holder="Nueva contraseña" display="5 left"/>
			<farmatic:inputType label="Confirma contraseña:" name="validPassword"  type="password" value="${validPassword}" holder="Confirma contraseña" display="5 left"/>
			<button class="btn btn-default" type="submit">Aceptar</button>
		</form:form>
	</div>
</farmatic:layout>