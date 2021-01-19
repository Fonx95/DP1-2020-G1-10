<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<farmatic:layout pageName= "ventas">
    
	<h2>Mis Ventas</h2>
	<table class = "table table-hover">
		<thead>
		<tr>
			<th>Id</th>
			<th>Fecha de venta</th>
			<th>Cliente</th>
			<th>Importe Total</th>
			<th>Pagado</th>
			<th>Deuda</th>
		</tr>
		</thead>
		<tbody>
			<c:forEach items="${ventas}" var="venta">
				<c:if test="${venta.estadoVenta == 'Realizada'}">
					<spring:url value="/ventas/{idVenta}" var="mostrarPedido">
              		<spring:param name="idVenta" value="${venta.id}"/>
              		</spring:url>
					<tr>
						<td>
		                    <a href="${fn:escapeXml(mostrarPedido)}"><c:out value="${venta.id}"/></a>
						</td>
						<td>
							<a href="${fn:escapeXml(mostrarPedido)}"><c:out value="${venta.fecha}"/></a>
						</td>
						<td>
							<a href="${fn:escapeXml(mostrarPedido)}"><c:out value="${venta.cliente.name} ${venta.cliente.surnames}"/></a>
						</td>
						<td>
							<c:out value="${venta.importeTotal}"/> &#8364
						</td>
						<td>
							<c:out value = "${venta.pagado}" /> &#8364
						</td>
						<td>
							<c:out value = "${venta.porPagar}" /> &#8364
						</td>
					</tr>
				</c:if>
			</c:forEach>
		</tbody>
	</table>
</farmatic:layout>