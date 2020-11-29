<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName= "pedidos">
	<h2> Pedidos</h2>
	
	<table class = "table table-striped">
		<thead>
		<tr>
			<th>Código</th>
			<th>Fecha_pedido</th>
			<th>Fecha_entrega</th>
			<th>Estado</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${pedidos.pedidoLista}" var="pedido">
			<tr>
				<td>
					<c:out value="${pedido.codigo}"/>
				</td>
				<td>
					<c:out value="${pedido.fechaPedido}"/>
				</td>
				<td>
					<c:out value="${pedido.fechaEntrega}"/>
				</td>
				<td>
					<c:out value="${pedido.estadoPedido}"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>

	</table>
</farmatic:layout>