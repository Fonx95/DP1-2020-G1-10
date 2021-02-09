<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<farmatic:layout pageName= "ventas">
	<h2>Nueva venta</h2>
	<label style="width: 100px">Importe Total:</label>
	<section style="display: inline">
		<input disabled class="marcador" type="text" name="pagado" value="${ventaActual.porPagar} &#8364"/>
	</section>
	<br>
	<br>
	<form:form modelAttribute="cliente" class="form-horizontal" id="asing-cliente-form">
		<section style="display: inline">
			<div class="col-sm-10">
			<farmatic:inputType label="DNI cliente:" name="dni" type="text" holder="DNI" value="${cliente.dni}"  display="2 control"/>
			</div>
			<button class="btn btn-default" type="submit">Buscar</button>
		</section>
	</form:form>
	<c:if test="${cliente.name != null}">
		<br>
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
	    <form:form modelAttribute="Cliente" class="form-horizontal" id="asing-cliente-form">
	    	<input type="hidden" name="Id" value="${cliente.id}"/>
			<button class="btn btn-default" type="submit">Aceptar</button>
		</form:form>
	</c:if>
</farmatic:layout>