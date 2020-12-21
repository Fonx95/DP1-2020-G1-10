<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName= "pedidos">
	<h2>Informacion del Pedido</h2>
	<form:form modelAttribute="proveedor" class="form-horizontal" id="edit-linea-form">
		<table class = "table table-striped">
			<tr>
				<th>Código</th>
				<th>${pedidoActual.codigo}</th>
			</tr>
			<tr>
				<th>Proveedor</th>
				<th><select class="form-control form-control-sm" name="proveedor">
					<c:forEach items="${proveedores}" var="proveedor">
						<option value="${proveedor.id}">${proveedor.empresa}</option>
					</c:forEach>
				</select>
			</tr>
			<tr>
				<th>Estado</th>
				<th>${pedidoActual.estadoPedido}</th>
			</tr>
		</table><!--  
		<input type="hidden" name="codigo" value="${pedidoActual.codigo}"/>
		<input type="hidden" name="estadoPedido" value="${pedidoActual.estadoPedido}"/>
		<input type="hidden" name="Id" value="${pedidoActual.id}"/>-->
		<button class="btn btn-default btn-sm" type="submit">Enviar</button>
	</form:form>
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
			<c:forEach items="${pedidoActual.lineaPedido}" var="linea">
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
	<a href="/pedidos/actual" class="btn btn-default">Volver</a>
</farmatic:layout>