<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName= "productos">
	<h2> Productos</h2>
	<a href="/productos/new" class="btn btn-default btn-right">Nuevo Producto</a>
	<div class="btn-group" role="group" aria-label="Button group with nested dropdown">
		
		<a class="btn btn-default dropdown-toggle" href="/productos">Todos</a>
		
		<div class="btn-group" role="group">
			<button id="btnGroupDrop1" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			Tipos de medicamento
			</button>
			<div class="dropdown-menu" aria-labelledby="btnGroupDrop1">
				<c:forEach items="${tipoMedicamento}" var="tipo">
					<spring:url value="../../productos/tipo/{idTipo}" var="editUrl">
				        <spring:param name="idTipo" value="${tipo.id}"/>
				    </spring:url>
					<div>
					<div class="text-center">
						<div class="text-center">
							<a class="dropdown-item" href="${fn:escapeXml(editUrl)}">${tipo.tipo}</a>
						</div>
					</div>
				</div>
				</c:forEach>
			</div>
		</div>
	</div>
	<br>
	<br>
	
	<table id="productosTable" class = "table table-striped">
		<thead>
		<tr>
			<th style="width: 170px;">Codigo</th>
			<th>Nombre</th>
			<th>Tipo de Producto</th>
			<th>Stock</th>
		</tr>
		</thead>
		<tbody>
		<form:form modelAttribute="producto" class="form-horizontal" id="search-producto-form">
			<tr>
				<td>
					<farmatic:inputTypeSimple name="code" type="text" value="${producto.code}"/>
				</td>
				<td>
					<farmatic:inputTypeSimple name="name" type="text" value="${producto.name}"/>
				</td>
				<td></td>
				<td></td>
				<td>
					<button class="btn btn-default btn-sm" type="submit">
						<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					</button>
				</td>
			</tr>
		</form:form>
		<c:forEach items="${productos}" var="producto">
			<tr>
				<td>
					<c:out value="${producto.code}"/>
				</td>
				<td>
					<c:out value="${producto.name}"/>
				</td>
				<td>
					<c:out value="${producto.productType}"/>
				</td>
				<td>
					<c:out value="${producto.stock}"/>
				</td>
				<td>
                   	<spring:url value="/productos/{idProducto}" var="mostrarProducto">
                   	<spring:param name="idProducto" value="${producto.id}"/>
                   	</spring:url>
                    <a href="${fn:escapeXml(mostrarProducto)}" class="btn btn-default">Detalles</a>
                </td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<c:if test="${vacio}">
		<span class="help-inline">No existe el producto</span>
	</c:if>
</farmatic:layout>
	