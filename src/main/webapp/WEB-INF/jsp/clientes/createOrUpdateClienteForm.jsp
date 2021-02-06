<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName="clientes">
    <h2>
        <c:if test="${cliente['new']}">Nuevo </c:if> Cliente
    </h2>
    <form:form modelAttribute="cliente" class="form-horizontal" id="add-cliente-form">
        <div class="form-group has-feedback">
            <farmatic:inputType label="Nombre" name="name" type="text" holder="Nombre" value="${cliente.name}" display="2 control"/>
            <farmatic:inputType label="Apellidos" name="surnames" type="text" holder="Apellidos" value="${cliente.surnames}" display="2 control"/>
            <farmatic:inputType label="DNI" name="dni" type="text" holder="DNI" value="${cliente.dni}" display="2 control"/>
            <farmatic:inputType label="Provincia" name="provincia" type="text" holder="Provincia" value="${cliente.provincia}" display="2 control"/>
            <farmatic:inputType label="Localidad" name="localidad" type="text" holder="Localidad" value="${cliente.localidad}" display="2 control"/>
            <farmatic:inputType label="Direccion" name="direccion" type="text" holder="Direccion" value="${cliente.direccion}" display="2 control"/>
            <input type="hidden" name="user" value="${cliente.user.username}"/>
            <input type="hidden" name="Id" value="${cliente.id}"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<a href="/clientes/${not cliente['new'] ? cliente.id : ''}" class="btn btn-default">Volver</a>
                <c:choose>
                    <c:when test="${cliente['new']}">
                        <button class="btn btn-default" type="submit">Añadir Cliente</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Actualizar Cliente</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</farmatic:layout>

