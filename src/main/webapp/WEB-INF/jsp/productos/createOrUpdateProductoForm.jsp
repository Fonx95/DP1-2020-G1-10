<%@page import="org.springframework.samples.farmatic.model.TipoProducto"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName="productos">
    <h2>
        <c:if test="${producto['new']}">Nuevo </c:if> Producto
    </h2>
    <form:form modelAttribute="producto" class="form-horizontal" id="add-product-form">
        <div class="form-group has-feedback">
            <farmatic:inputField label="Nombre" name="name"/>
            <farmatic:inputField label="Código" name="code"/>
            <farmatic:selectField label="Tipo Producto" name="productType" size="1" names="${tipoProducto}"/>
            <farmatic:selectMultiple items="${tipoMedicamento}" values="${producto.tipoMedicamento}" label="Tipo Medicamento " name="tipoMedicamento"/>
            <farmatic:inputField label="Precio de vental al público" name="pvp"/>
            <farmatic:inputField label="Precio de venta Farmacia" name="pvf"/>
            <farmatic:inputField label="Stock" name="stock"/>
            <farmatic:inputField label="Stock mínimo" name="minStock"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<a href="/productos/${not producto['new'] ? producto.id : ''}" class="btn btn-default">Volver</a>
                <c:choose>
                    <c:when test="${producto['new']}">
                        <button class="btn btn-default" type="submit">Añadir Producto</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Modificar Producto</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</farmatic:layout>

