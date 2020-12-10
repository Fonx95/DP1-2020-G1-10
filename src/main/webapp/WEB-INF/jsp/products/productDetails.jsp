<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName= "productos">

    <h2>Product Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Nombre</th>
            <td><b><c:out value="${producto.name}"/></b></td>
        </tr>
        <tr>
        	<th>Codigo</th>
        	<td><b><c:out value="${producto.code}"/></b></td>
        </tr>
        <tr>
            <th>Tipo_producto</th>
            <td><c:out value="${producto.productType}"/></td>
        </tr>
        <tr>
        	<th>PvP</th>
        	<td><b><c:out value="${producto.pvp}"/></b></td>
        </tr>
        <tr>
        	<th>PvF</th>
        	<td><b><c:out value="${producto.pvf}"/></b></td>
        </tr>
        <tr>
            <th>Stock</th>
            <td><b><c:out value="${producto.stock}"/></b></td>
        </tr>
        <tr>
        	<th>Minimo_stock</th>
        	<td><b><c:out value="${producto.minStock}"/></b>
        
    </table>

    <spring:url value="{idProducto}/edit" var="editUrl">
        <spring:param name="idProducto" value="${producto.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Modificar</a>
</farmatic:layout>