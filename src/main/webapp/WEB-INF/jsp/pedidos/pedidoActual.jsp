<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<farmatic:layout pageName= "pedidos">
	<h2> Pedido en Proceso</h2>
	
	<form:form modelAttribute="producto" class="form-horizontal" id="search-producto-form">
		<div>
		<farmatic:inputField label="Codigo Producto:" name="code"/>
		<button class="btn btn-default btn-right" type="submit">Buscar</button>
		</div>
	</form:form>
	<c:if test="${!producto['new']}">
		<table class = "table table-striped">
			<form:form modelAttribute="nuevaLinea" class="form-horizontal" id="add-linea-form">
				<tr>
					<th>${nuevaLinea.producto.code}</th>
					<th>Nombre: ${nuevaLinea.producto.name}</th>
					<th>Stock: ${nuevaLinea.producto.stock}</th>
					<th>Stock minimo: ${nuevaLinea.producto.minStock}</th>
					<th>
						<farmatic:inputField label="Cantidad: " name="cantidad"/>
					</th>
					<th>
						<input type="hidden" name="Pedido" value="${nuevaLinea.pedido.id}"/>
						<input type="hidden" name="Producto" value="${nuevaLinea.producto.id}"/>
						<button class="btn btn-default btn-right" type="submit">Añadir</button>
					</th>
				</tr>
			</form:form>
		</table>
	</c:if>
	<table class = "table table-striped">
		<thead>
		<tr>
			<th>Código</th>
			<th>Nombre</th>
			<th>PvP</th>
			<th>PvF</th>
			<th>Stock</th>
			<th>Stock Minimo</th>
			<th>cantidad</th>
		</tr>
		</thead>
		<tbody>
			<form:form modelAttribute="pedidoActual" class="form-horizontal" id="put-linea-form">
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
							<c:out value="${linea.producto.stock}"/>
						</td>
						<td>
							<c:out value="${linea.producto.minStock}"/>
						</td>
						<td>
							<c:out value="${linea.cantidad}"/>
						</td>
					</tr>
				</c:forEach>
			</form:form>
		</tbody>
	</table>
</farmatic:layout>