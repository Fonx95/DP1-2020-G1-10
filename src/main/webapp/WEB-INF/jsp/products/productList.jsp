<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName= "productos">
	<h2> Productos</h2>
	
	<table id="productosTable" class = "table table-striped">
		<thead>
		<tr>
			<th>Nombre</th>
			<th>Tipo_Producto</th>
			<th>Stock</th>
			<th>Detalles</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${productos}" var="producto">
			<tr>
				<td>
					<c:out value="${producto.name }"/>
				</td>
				<td>
					<c:out value="${producto.productType }"/>
				</td>
				<td>
					<c:out value="${producto.stock }"/>
				</td>
				<td>
					<spring:url value="/products/productList/{idProducto}" var="productUrl">
						<spring:param name="idProducto" value="${producto.id}"/>
					</spring:url>
					<a href="${fn:escapeXml(productUrl)}">Detalles</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</farmatic:layout>
	