<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>


<farmatic:layout pageName= "ventas">
		<div class="row no-gutters">
    		<div class="col-12 col-sm-6 col-md-8">
				<h2>Informacion de la venta</h2>
				<table class = "table table-striped">
					<tr>
						<th>Fecha de venta</th>
						<td>${venta.fecha}</td>
					</tr>
					<sec:authorize access= "hasAuthority('farmaceutico')">
						<tr>
							<th>Cliente</th>
							<spring:url value="/clientes/{idCliente}" var="mostrarCliente">
	                   		<spring:param name="idCliente" value="${venta.cliente.id}"/>
	                   		</spring:url>
							<td><a href="${fn:escapeXml(mostrarCliente)}">
								${venta.cliente.name} &nbsp ${venta.cliente.surnames}
							</a></td>
						</tr>
					</sec:authorize>
					<tr>
						<th>Importe Total</th>
						<td>${venta.importeTotal} &#8364</td>
					</tr>
					<tr>
						<th>Pagado</th>
						<td>${venta.pagado} &#8364</td>
					</tr>
					<tr>
						<th>Deuda</th>
						<td>${venta.porPagar} &#8364</td>
					</tr>
				</table>
			</div>
			<sec:authorize access= "hasAuthority('farmaceutico')">
				<c:if test="${venta.comprador != null}">
					<div class="col-6 col-md-4">
						<h2>Registro de Estupefaciente</h2>
						<table class = "table table-striped">
							<tr>
								<th>Nombre</th>
								<td>${venta.comprador.name}</td>
							</tr>
							<tr>
								<th>Apellidos</th>
								<td>${venta.comprador.apellidos}</td>
							</tr>
							<tr>
								<th>DNI</th>
								<td>${venta.comprador.dni}</td>
							</tr>
						</table>
					</div>
				</c:if>
			</sec:authorize>
		</div>
	
	<h2>Lineas:</h2>
	<table class = "table table-striped">
		<thead>
			<tr>
				<th>Código</th>
				<th>Nombre</th>
				<th>Tipo</th>
				<th>PvP</th>
				<th>Stock</th>
				<th>Cantidad</th>
				<th>T.A</th>
				<th>Importe</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${venta.lineaVenta}" var="linea">
				<tr>
					<td>
						<c:out value="${linea.producto.code}"/>
					</td>
					<td>
						<c:out value="${linea.producto.name}"/>
					</td>
					<td>
						<c:out value="${linea.producto.productType}"/>
					</td>
					<td>
						<c:out value="${linea.producto.pvp}"/>
					</td>
					<td>
						<c:out value="${linea.producto.stock}"/>
					</td>
					<td>
						<c:out value="${linea.cantidad}"/>
					</td>
					<td>
						<c:out value="${linea.tipoTasa}"/>
					</td>
					<td>
						<c:out value="${linea.importe}"/>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<sec:authorize access= "hasAuthority('farmaceutico')">
		<spring:url value="/ventas/" var="Volver">
  		</spring:url>
	</sec:authorize>
	<sec:authorize access= "hasAuthority('cliente')">
		<spring:url value="/misVentas/" var="Volver">
  		</spring:url>
	</sec:authorize>
	<a href="${fn:escapeXml(Volver)}" class="btn btn-default">Volver</a>
</farmatic:layout>