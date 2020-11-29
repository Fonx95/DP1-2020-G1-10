<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName= "pedidos">

    <h2>Informacion de pedido</h2>


    <table class="table table-striped">
        <tr>
            <th>Codigo</th>
            <td><b><c:out value="${pedido.codigo}"/></b></td>
        </tr>
  
        <tr>
            <th>Fecha pedido</th>
            <td><c:out value="${pedido.fechaPedido}"/></td>
        </tr>
        <tr>
        	<th>Fecha Entrega</th>
        	<td><b><c:out value="${pedido.fechaEntrega}"/></b></td>
        </tr>
        <tr>
        	<th>Estado</th>
        	<td><b><c:out values="${pedido.estadoPedido}"/></b></td>
        </tr>

        
    </table>
		 <spring:url value="{idPedido}/edit" var="editUrl">
        <spring:param name="idPedido" value="${pedido.id}"/>
    </spring:url>
</farmatic:layout>