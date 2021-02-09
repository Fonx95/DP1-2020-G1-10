<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<farmatic:layout pageName= "clientes">
	<div class="row no-gutters">
    	<div class="col-12 col-sm-6 col-md-8">
		    <h2>Información del cliente</h2>
		    <table class="table table-striped">
		        <tr>
		            <th>Nombre</th>
		            <td><c:out value="${cliente.name}"/></td>
		        </tr>
		        <tr>
		            <th>Apellidos</th>
		            <td><c:out value="${cliente.surnames}"/></td>
		        </tr>
		        <tr>
		        	<th>DNI</th>
		        	<td><c:out value="${cliente.dni}"/></td>
		        </tr>
		        <tr>
		            <th>Provincia</th>
		            <td><c:out value="${cliente.provincia}"/></td>
		        </tr>
		        <tr>
		        	<th>Localidad</th>
		        	<td><c:out value="${cliente.localidad}"/></td>
		        </tr>
		        <tr>
		        	<th>Dirección</th>
		        	<td><c:out value="${cliente.direccion}"/></td>
		        </tr>
		        <tr>
		            <th>Debe:</th>
		            <td><c:out value="${cliente.porPagarTotal}"/> &#8364</td>
		        </tr>
		    </table>
    	</div>
    	<div class="col-6 col-md-4">
    		<h2>Informacion de usuario</h2>
    		<table class="table table-striped">
    			<tr>
		        	<th>Usuario</th>
		        	<td><c:out value="${cliente.user.username}"/></td>
		    	</tr>
    		</table>
    	</div>
    </div>
   	<a href="/clientes" class="btn btn-default">Volver</a>
    <spring:url value="{idCliente}/edit" var="editUrl">
        <spring:param name="idCliente" value="${cliente.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Modificar</a>
    <br>
    <br>
   	<h2>Ventas</h2>
	<table class = "table table-striped">
		<thead>
			<tr>
				<th>Id</th>
				<th>Fecha</th>
				<th>Importe Total</th>
				<th>Pagado</th>
				<th>Deuda</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${cliente.venta}" var="venta">
				<spring:url value="/ventas/{idVenta}" var="mostrarVenta">
            	<spring:param name="idVenta" value="${venta.id}"/>
           		</spring:url>
				<tr>
					<td>
						<a href="${fn:escapeXml(mostrarVenta)}"><c:out value="${venta.id}"/></a>
					</td>
					<td>
						<a href="${fn:escapeXml(mostrarVenta)}"><c:out value="${venta.fecha}"/></a>
					</td>
					<td>
						<c:out value="${venta.importeTotal}"/>
					</td>
					<td>
						<c:out value="${venta.pagado}"/>
					</td>
					<td>
						<c:out value="${venta.porPagar}"/>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</farmatic:layout>