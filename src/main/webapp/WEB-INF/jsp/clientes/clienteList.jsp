<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName= "clientes">
	<h2> Clientes</h2>
	
	<table id="productosTable" class = "table table-striped">
		<thead>
		<tr>
			<th>Nombre</th>
			<th>Apellidos</th>
			<th>Debe</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${clientes}" var="cliente">
			<tr>
				<td>
					<c:out value="${cliente.name}"/>
				</td>
				<td>
					<c:out value="${cliente.surnames}"/>
				</td>
				<td>
					<c:out value="${cliente.porPagarTotal}"/>
				</td>
				<td>
                   	<spring:url value="/clientes/clienteList/{idCliente}" var="mostrarCliente">
                   	<spring:param name="idCliente" value="${cliente.id}"/>
                   	</spring:url>
                    <a href="${fn:escapeXml(mostrarCliente)}" class="btn btn-default">Detalles</a>
                </td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<a href="/clientes/new" class="btn btn-default">Registrar Cliente</a>
</farmatic:layout>
	