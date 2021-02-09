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
		<h2>Registro</h2>
		<c:choose>
			<c:when test="${cliente['new']}">
				<form:form modelAttribute="cliente" class="form-horizontal" id="add-cliente-form">
					<farmatic:inputType label="DNI" name="dni" type="text" holder="DNI" display="2 control" value="${cliente.dni}"/>
					<button class="btn btn-default" type="submit">Aceptar</button>
				</form:form>
			</c:when>
			<c:otherwise>
				<form:form modelAttribute="cliente" class="form-horizontal" id="add-user-form">
					<div class="form-group">
						<label class="col-sm-2 control-label">Nombre: </label>
						<div class="col-sm-10">
							<input readonly class="form-control" type="text" name="name" value="${cliente.name}">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">Apellidos: </label>
						<div class="col-sm-10">
							<input readonly class="form-control" type="text" name="surnames" value="${cliente.surnames}">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">DNI: </label>
						<div class="col-sm-10">
							<input readonly class="form-control" type="text" name="dni" value="${cliente.dni}">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">Provincia: </label>
						<div class="col-sm-10">
							<input readonly class="form-control" type="text" name="provincia" value="${cliente.provincia}">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">Localidad: </label>
						<div class="col-sm-10">
							<input readonly class="form-control" type="text" name="localidad" value="${cliente.localidad}">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">Direccion: </label>
						<div class="col-sm-10">
							<input readonly class="form-control" type="text" name="direccion" value="${cliente.direccion}">
						</div>
					</div>
					<form:hidden path="Id"/>
					<hr>
					<farmatic:inputType label="Usuario: " name="user.username" type="text" value="${cliente.user.username}" holder="Usuario" display="5 left"/>
					<farmatic:inputType label="Contraseña: " name="user.password" type="password" holder="Contraseña" display="5 left"/>
					<spring:bind path="user.password">
						<c:if test="${status.error}">
			                <div class="alert alert-danger" role="alert">
			                	<h4>La contraseña debe:</h4>
			                	<ul>
				                    <li>Tener entre 5 y 16 caracteres</li>
				                    <li>Al menos un numero</li>
				                    <li>Al menos una minuscula</li>
				                    <li>Al menos una mayuscula</li>
				            	</ul>
			                </div>
			            </c:if>
					</spring:bind>
					<button class="btn btn-default" type="submit">Aceptar</button>
				</form:form>
			</c:otherwise>
		</c:choose>
	</div>
</farmatic:layout>