<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<farmatic:layout pageName= "ventas">
	<h2> Venta en Proceso</h2>
	<table class = "table table-striped">
		<thead>
		<tr>
			<th>Código</th>
			<th>Nombre</th>
			<th>PvP</th>
			<th>Stock</th>
			<th>Stock Min.</th>
			<th>Cantidad</th>
			<th>T.A</th>
			<th>Importe</th>
		</tr>
		</thead>
		<tbody>
			<c:forEach items="${ventaActual.lineaVenta}" var="linea">
				<tr>
					<td>
						<input disabled type="text" class="form-control" name="code" value="${linea.producto.code}"/>
					</td>
					<td>
						<c:out value="${linea.producto.name}"/>
					</td>
					<td>
						<c:out value="${linea.producto.pvp}"/>
					</td>
					<td>
						<c:out value="${linea.producto.stock}"/>
					</td>
					<td>
						<c:out value="${linea.producto.minStock}"/>
					</td>
					<td>
						<input disabled type="text" class="form-control" name="Cantidad" value="${linea.cantidad}"/>
					</td>
					<td>
						<input disabled type="text" class="form-control" name="tipoTasa" value="${linea.tipoTasa}"/>
					</td>
					<td>
						<c:out value="${linea.importe}"/>
					</td>
					<td>
						<a class="btn btn-default btn-sm" href="/ventas/actual/${linea.id}">
	                	<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
	                	</a>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${!producto['new']}">
				<form:form modelAttribute="nuevaLinea" class="form-horizontal" id="add-linea-form">
					<tr>
						<th>${nuevaLinea.producto.code}</th>
						<th>${nuevaLinea.producto.name}</th>
						<th>${nuevaLinea.producto.pvp}</th>
						<th>${nuevaLinea.producto.stock}</th>
						<th>${nuevaLinea.producto.minStock}</th>
						<th>
							<input type="text" class="form-control" name="Cantidad" value="${nuevaLinea.cantidad}"/>
						</th>
						<th>
							<select class="form-control" name="tipoTasa">
							<option value="TSI001">TSI001</option>
							<option value="TSI002">TSI002</option>
							<option value="TSI003">TSI003</option>
							<option value="TSI004">TSI004</option>
							<option value="TSI005">TSI005</option>
						</select>
						</th>
						<th>
							<c:out value="${linea.importe}"/>
						</th>
						<th>
							<input type="hidden" name="venta" value="${nuevaLinea.venta.id}"/>
							<input type="hidden" name="Producto" value="${nuevaLinea.producto.id}"/>
							<button class="btn btn-default btn-sm" type="submit">
								<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
							</button>
						</th>
					</tr>
				</form:form>
			</c:if>
			<form:form modelAttribute="producto" class="form-horizontal" id="search-producto-form">
				<tr>
					<td>
						<input class="form-control" type="text" name="code"/>
					</td>
					<td></td>
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
	<a href="/ventas/actual/pagar" class="btn btn-default">Pagar</a>
</farmatic:layout>