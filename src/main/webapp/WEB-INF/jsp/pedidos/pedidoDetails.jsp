<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName= "pedidos">
	<h2>Informacion del Pedido</h2>
	<table class = "table table-striped">
		<tr>
			<th>C�digo</th>
			<th>${pedido.codigo}</th>
		</tr>
		<tr>
			<th>Proveedor</th>
			<th>${pedido.proveedor.empresa}</th>
		</tr>
		<tr>
			<th>Fecha Pedido</th>
			<th>${pedido.fechaPedido}</th>
		</tr>
		<tr>
			<th>Estado</th>
			<th>${pedido.estadoPedido}</th>
		</tr>
		<tr>
			<th>Fecha Entrega</th>
			<th>${pedido.fechaEntrega}</th>
		</tr>
	</table>
	<h2>Lineas:</h2>
	<table class = "table table-striped">
		<thead>
			<tr>
				<th>C�digo</th>
				<th>Nombre</th>
				<th>PvP</th>
				<th>PvF</th>
				<th>Cantidad</th>
				<th>Stock</th>
				<th>Stock Minimo</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${pedido.lineaPedido}" var="linea">
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
	<a href="/pedidos/" class="btn btn-default">Volver</a>
</farmatic:layout>