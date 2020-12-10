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
			<tr>
				<th>${producto.code}</th>
				<th>Nombre: ${producto.name}</th>
				<th>Stock: ${producto.stock}</th>
				<th>Stock minimo: ${producto.minStock}</th>
				<th>
					<form:form modelAttribute="nuevaLinea" class="form-horizontal" id="add-linea-form">
						<div>
						<farmatic:inputField label="Cantidad: " name="cantidad"/>
						<farmatic:inputField label="id: " name="id"/>
						<button class="btn btn-default btn-right" onclick="form:search-producto-form()" type="submit">Añadir</button>
						</div>
					</form:form>
				</th>
			</tr>
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
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${nuevaLinea.pedido.lineaPedido}" var="lin">
			<tr>
				<td>
					<c:out value="${lin.producto.code}"/>
				</td>
				<td>
					<c:out value="${lin.producto.name}"/>
				</td>
				<td>
					<c:out value="${lin.producto.pvp}"/>
				</td>
				<td>
					<c:out value="${lin.producto.pvf}"/>
				</td>
				<td>
					<c:out value="${lin.producto.stock}"/>
				</td>
				<td>
					<c:out value="${lin.producto.minStock}"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</farmatic:layout>