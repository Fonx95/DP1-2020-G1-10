<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName= "ventasCliente">
	<h2> Cliente</h2>
	
	<table id="productosTable" class = "table table-striped">
		<tr>
			<th>Nombre</th>
			<td><c:out value="${cliente.name}"/></td>
		</tr>
		<tr>
			<th>Apellidos</th>
			<td><c:out value="${cliente.surnames}"/></td>
		</tr>
		<tr>
			<th>Deuda Total</th>
			<td><c:out value="${cliente.porPagarTotal}"/> &#8364</td>
		</tr>
	</table>
	<h2> Mis Ventas</h2>
	<table class = "table table-striped">
		<thead>
			<tr>
				<th>Id</th>
				<th>Fecha</th>
				<th>Importe Total</th>
				<th>Pagado</th>
				<th>Deuda</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${cliente.venta}" var="venta">
				<spring:url value="/misVentas/{idVenta}" var="mostrarVenta">
            	<spring:param name="idVenta" value="${venta.id}"/>
           		</spring:url>
				<tr>
					<td>
						<a href="${fn:escapeXml(mostrarVenta)}"><c:out value="${venta.id}"/></a>
					</td>
					<td>
						<a href="${fn:escapeXml(mostrarVenta)}"><c:out value="${venta.fecha}"/></a>
					</td>
					<td>
						<c:out value="${venta.importeTotal}"/>
					</td>
					<td>
						<c:out value="${venta.pagado}"/>
					</td>
					<td>
						<c:out value="${venta.porPagar}"/>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</farmatic:layout>
	