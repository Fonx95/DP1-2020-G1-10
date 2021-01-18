<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName= "ventas">
	<h2>Informacion de la venta</h2>
	<form:form modelAttribute="cliente" class="form-horizontal" id="edit-linea-form">
		<table class = "table table-striped">
			<tr>
				<th>Código</th>
				<th>${pedidoActual.codigo}</th>
			</tr>
			<tr>
				<th>Clientes</th>
				<th><select class="form-control form-control-sm" name="proveedor">
					<c:forEach items="${clientes}" var="cliente">
						<option value="${cliente.id}">${cliente.direccion}</option>
					</c:forEach>
				</select>
			</tr>
			<tr>
				<th>Estado</th>
				<th>${ventaActual.estadoVenta}</th>
			</tr>
		</table>
		<a href="/ventas/actual" class="btn btn-default">Volver</a>
		<button class="btn btn-default" type="submit">finalizar</button>
	</form:form>
	<br>
	<h2>Lineas:</h2>
	<table class = "table table-striped">
		<thead>
			<tr>
				<th>Código</th>
				<th>Nombre</th>
				<th>PvP</th>
				<th>PvF</th>
				<th>Cantidad</th>
				<th>Stock</th>
				<th>Stock Minimo</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${ventaActual.lineaVenta}" var="linea">
				<tr>
					<td>
						<c:out value="${linea.producto.code}"/>
					</td>
					<td>
						<c:out value="${linea.producto.name}"/>
					</td>
					<td>
						<c:out value="${linea.producto.pvp}"/>
					</td>
					<td>
						<c:out value="${linea.producto.pvf}"/>
					</td>
					<td>
						<c:out value="${linea.cantidad}"/>
					</td>
					<td>
						<c:out value="${linea.producto.stock}"/>
					</td>
					<td>
						<c:out value="${linea.producto.minStock}"/>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
</farmatic:layout>