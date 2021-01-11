<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName= "clientes">

    <h2>Información del cliente</h2>


    <table class="table table-striped">
        <tr>
            <th>Nombre</th>
            <td><b><c:out value="${cliente.name}"/></b></td>
        </tr>
        <tr>
            <th>Apellidos</th>
            <td><b><c:out value="${cliente.surnames}"/></b></td>
        </tr>
        <tr>
        	<th>DNI</th>
        	<td><b><c:out value="${cliente.dni}"/></b></td>
        </tr>
        <tr>
            <th>Provincia</th>
            <td><c:out value="${cliente.provincia}"/></td>
        </tr>
        <tr>
        	<th>Localidad</th>
        	<td><b><c:out value="${cliente.localidad}"/></b></td>
        </tr>
        <tr>
        	<th>Dirección</th>
        	<td><b><c:out value="${cliente.direccion}"/></b></td>
        </tr>
        <tr>
            <th>Debe:</th>
            <td><b><c:out value="${cliente.porPagarTotal}"/><c:out value="€"></c:out></b></td>
        </tr>
        
    </table>

    
</farmatic:layout>