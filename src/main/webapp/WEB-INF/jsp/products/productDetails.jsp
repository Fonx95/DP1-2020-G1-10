<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName= "products">

    <h2>Product Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${product.name}"/></b></td>
        </tr>
        <tr>
            <th>Tipo_producto</th>
            <td><c:out value="${product.productType}"/></td>
        </tr>
        <tr>
            <th>Stock</th>
            <td><c:out value="${product.stock}"/></td>
        </tr>
    </table>

    <spring:url value="{idProducto}/edit" var="editUrl">
        <spring:param name="idProducto" value="${producto.id}"/>
    </spring:url>
</farmatic:layout>