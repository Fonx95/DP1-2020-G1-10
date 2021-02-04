<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<farmatic:layout pageName= "usuario">
	
    <sec:authorize access= "hasAuthority('cliente')">
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
			            <th>Deuda:</th>
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
    </sec:authorize>
    <sec:authorize access= "hasAuthority('farmaceutico')">
    	<div class="row no-gutters">
    		<div class="col-12 col-sm-6 col-md-8">
			    <h2>Información del farmaceutico</h2>
			    <table class="table table-striped">
			        <tr>
			            <th>Nombre</th>
			            <td><c:out value="${farmaceutico.name}"/></td>
			        </tr>
			        <tr>
			            <th>Apellidos</th>
			            <td><c:out value="${farmaceutico.surnames}"/></td>
			        </tr>
			        <tr>
			        	<th>DNI</th>
			        	<td><c:out value="${farmaceutico.dni}"/></td>
			        </tr>
			        <tr>
			            <th>Direccion</th>
			            <td><c:out value="${farmaceutico.pharmacyAddress}"/></td>
			        </tr>
			    </table>
			</div>
	    	<div class="col-6 col-md-4">
	    		<h2>Informacion de usuario</h2>
	    		<table class="table table-striped">
	    			<tr>
			        	<th>Usuario</th>
			        	<td><c:out value="${farmaceutico.user.username}"/></td>
			    	</tr>
	    		</table>
	    	</div>
	    </div>
    </sec:authorize>
    <sec:authorize access= "hasAuthority('proveedor')">
    	<div class="row no-gutters">
    		<div class="col-12 col-sm-6 col-md-8">
			    <h2>Información del proveedor</h2>
			    <table class="table table-striped">
			        <tr>
			            <th>Empresa</th>
			            <td><c:out value="${proveedor.empresa}"/></td>
			        </tr>
			        <tr>
			            <th>CIF</th>
			            <td><c:out value="${proveedor.cif}"/></td>
			        </tr>
			        <tr>
			            <th>Direccion</th>
			            <td><c:out value="${proveedor.direccion}"/></td>
			        </tr>
			    </table>
			</div>
	    	<div class="col-6 col-md-4">
	    		<h2>Informacion de usuario</h2>
	    		<table class="table table-striped">
	    			<tr>
			        	<th>Usuario</th>
			        	<td><c:out value="${proveedor.user.username}"/></td>
			    	</tr>
	    		</table>
	    	</div>
	    </div>
    </sec:authorize>
</farmatic:layout>