<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName= "clientes">
	<h2> Cliente</h2>
	
	<table id="productosTable" class = "table table-striped">
		<thead>
		<tr>
			<th>Nombre</th>
			<th>Apellidos</th>
			
		</tr>
		</thead>
		<tbody>
			<tr>
				<td>
					<c:out value="${cliente.name}"/>
				</td>
				<td>
					<c:out value="${cliente.surnames}"/>
				</td>

			</tr>
	
		</tbody>
	</table>
	<h2> Mis Ventas</h2>
	<table class = "table table-striped">
		<thead>
			<tr>
				<th>Id</th>
				<th>Fecha</th>
				<th>Importe Total</th>
				<th>Por pagar</th>
				
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${cliente.venta}" var="venta">
				<tr>
				
				<td>
						<c:out value="${venta.id}"/>
					</td>
					<td>
						<c:out value="${venta.fecha}"/>
					</td>
					<td>
						<c:out value="${venta.importeTotal}"/>
					</td>
					<td>
						<c:out value="${venta.porPagar}"/>
					</td>

				</tr>
			</c:forEach>
		</tbody>
	</table>
</farmatic:layout>
	