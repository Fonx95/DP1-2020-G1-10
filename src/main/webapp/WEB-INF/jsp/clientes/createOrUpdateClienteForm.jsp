<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName="productos">
    <h2>
        <c:if test="${cliente['new']}">Nuevo </c:if> Cliente
    </h2>
    <form:form modelAttribute="cliente" class="form-horizontal" id="add-owner-form">
        <div class="form-group has-feedback">
            <farmatic:inputField label="Nombre" name="name"/>
            <farmatic:inputField label="Apellidos" name="surnames"/>
            <farmatic:inputField label="DNI" name="dni"/>
            <farmatic:inputField label="Provincia" name="provincia"/>
            <farmatic:inputField label="Localidad" name="localidad"/>
            <farmatic:inputField label="Direccion" name="direccion"/>
            <farmatic:inputField label="Usuario" name="user.username"/>
            <farmatic:inputField label="Contraseña" name="user.password"/>
            <input type="hidden" name="Id" value="${cliente.id}"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
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

