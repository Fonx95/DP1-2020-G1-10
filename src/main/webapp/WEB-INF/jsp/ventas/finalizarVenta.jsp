<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName= "ventas">
	<h2>Nueva venta</h2>
		
	<div class="container">
		<div class="row no-gutters">
			<div class="col-12 col-sm-6 col-md-8">
				<form:form modelAttribute="ventaActual" class="form-horizontal" id="new-venta-form">
					
					<label style="width: 100px">Importe Total:</label>
					<section style="display: inline">
						<input disabled class="marcador" type="text" name="pagado" value="${ventaActual.importeTotal} &#8364"/>
					</section>
					<br>
					<br>
					<label style="width: 100px">Importe:</label>
					<input type="text" name="pagado" value="${ventaActual.pagado}"/>
					<br>
					<br>
					<input type="hidden" name="venta" value="${ventaActual.id}"/>
					<a href="/ventas/actual" class="btn btn-default">Volver</a>
					<c:if test="${!estupefaciente || ventaActual.comprador.dni!=null}">
						<button class="btn btn-default" type="submit">Finalizar Venta</button>
					</c:if>
				</form:form>
			</div>
			<div class="col-6 col-md-4">
				<c:forEach items="${ventaActual.lineaVenta}" var="linea">
					<c:if test="${estupefaciente && ventaActual.comprador.dni==null}">
						<input type="hidden" value="${estupefaciente = false}"/>
						<br>
						<form:form modelAttribute="comprador" class="form-horizontal" id="new-comprador-form">
							<farmatic:inputField label="Nombre:" name="name"/>
							<farmatic:inputField label="Apellidos:" name="apellidos"/>
							<farmatic:inputField label="DNI:" name="dni"/>
							<input type="hidden" name="venta" value="${linea.venta.id}"/>
							<button class="btn btn-default" type="submit">Aceptar</button>
						</form:form>
					</c:if>
				</c:forEach>
			</div>
		</div>
	</div>
	<br>
	<h2>Lineas:</h2>
	<table class = "table table-striped">
		<thead>
			<tr>
				<th>Código</th>
				<th>Nombre</th>
				<th>Tipo</th>
				<th>PvP</th>
				<th>Cantidad</th>
				<th>T.A.</th>
				<th>Importe</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${ventaActual.lineaVenta}" var="linea">
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
	
</farmatic:layout>