<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<farmatic:layout pageName= "pedidos">
	<h2> Pedido en Proceso</h2>
	<table class = "table table-striped">
		<thead>
		<tr>
			<th>C�digo</th>
			<th>Nombre</th>
			<th>PvP</th>
			<th>PvF</th>
			<th>Stock</th>
			<th>Stock Minimo</th>
			<th>Cantidad</th>
		</tr>
		</thead>
		<tbody>
			<c:forEach items="${pedidoActual.lineaPedido}" var="linea">
				<c:choose>
					<c:when test="${linea.id == editaLinea.id}">
						<form:form modelAttribute="editaLinea" class="form-horizontal" id="edit-linea-form">
							<tr>
								<th>
									<c:out value="${editaLinea.producto.code}"/>
								</th>
								<th>
									<c:out value="${editaLinea.producto.name}"/>
								</th>
								<th>
									<c:out value="${editaLinea.producto.pvp}"/>
								</th>
								<th>
									<c:out value="${editaLinea.producto.pvf}"/>
								</th>
								<th>
									<c:out value="${editaLinea.producto.stock}"/>
								</th>
								<th>
									<c:out value="${editaLinea.producto.minStock}"/>
								</th>
								<th>
									<input type="text" name="Cantidad" value="${editaLinea.cantidad}"/>
								</th>
								<th>
									<input type="hidden" name="Id" value="${editaLinea.id}"/>
									<input type="hidden" name="Pedido" value="${editaLinea.pedido.id}"/>
									<input type="hidden" name="Producto" value="${editaLinea.producto.id}"/>
				                	<button class="btn btn-default btn-sm" type="submit">
				                		<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
				                	</button>
								</th>
							</tr>
						</form:form>
					</c:when>
					<c:otherwise>
						<tr>
							<td>
								<input disabled type="text" name="code" value="${linea.producto.code}"/>
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
								<input disabled type="text" name="Cantidad" value="${linea.cantidad}"/>
							</td>
							<td>
								<a class="btn btn-default btn-sm" href="/pedidos/actual/${linea.id}">
			                	<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
			                	</a>
							</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<form:form modelAttribute="producto" class="form-horizontal" id="search-producto-form">
				<tr>
					<td>
						<input type="text" name="code"/>
					</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td>
						<button class="btn btn-default btn-sm" type="submit">
							<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						</button>
					</td>
				</tr>
			</form:form>
		</tbody>
	</table>
</farmatic:layout>