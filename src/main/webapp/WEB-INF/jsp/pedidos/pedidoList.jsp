<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName= "pedidos">
	<h2> Pedidos</h2> 
	<a href="/pedidos/actual" class="btn btn-default btn-right">Pedido en proceso</a>
	<br/>
	<table class = "table table-striped">
		<thead>
		<tr>
			<th>Código</th>
			<th>Fecha de pedido</th>
			<th>Estado</th>
			<th>Fecha de entrega</th>
			<th>Proveedor</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${pedidos.pedidoLista}" var="pedido">
			<tr>
				<td>
					<spring:url value="/pedidos/{idPedido}" var="mostrarPedido">
                   	<spring:param name="idPedido" value="${pedido.id}"/>
                   	</spring:url>
                    <a href="${fn:escapeXml(mostrarPedido)}"><c:out value="${pedido.codigo}"/></a>
				</td>
				<td>
					<c:out value="${pedido.fechaPedido}"/>
				</td>
				<td>
					<c:out value="${pedido.estadoPedido}"/>
				</td>
				<td>
					<c:out value="${pedido.fechaEntrega}"/>
				</td>
				<td>
					<c:out value = "${pedido.proveedor.empresa}" />
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</farmatic:layout>