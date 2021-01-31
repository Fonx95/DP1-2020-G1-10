<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>

<farmatic:layout pageName= "productos">

	<div class="row no-gutters">
   		<div class="col-12 col-sm-6 col-md-8">
		    <h2>Informacion del producto</h2>
		    <table class="table table-striped">
		        <tr>
		            <th>Nombre:</th>
		            <td><c:out value="${producto.name}"/></td>
		        </tr>
		        <tr>
		        	<th>Codigo:</th>
		        	<td><c:out value="${producto.code}"/></td>
		        </tr>
		        <tr>
		            <th>Tipo de producto:</th>
		            <td><c:out value="${producto.productType}"/></td>
		        </tr>
		        <c:if test="${producto.productType != 'PARAFARMACIA'}">
			        <tr>
			            <th>Tipo de medicamento:</th>
			            <td>
			            	<c:forEach items="${producto.tipoMedicamento}" var="tipo">
			            		<c:out value="${tipo.tipo}; "/>
			            	</c:forEach>
			            </td>
			        </tr>
		        </c:if>
		        <tr>
		        	<th>PvP:</th>
		        	<td><c:out value="${producto.pvp}"/></td>
		        </tr>
		        <tr>
		        	<th>PvF:</th>
		        	<td><c:out value="${producto.pvf}"/></td>
		        </tr>
		        <tr>
		            <th>Stock:</th>
		            <td><c:out value="${producto.stock}"/></td>
		        </tr>
		        <tr>
		        	<th>Stock minimo:</th>
		        	<td><c:out value="${producto.minStock}"/></td>
		        </tr>
		    </table>
    	</div>
    	<div class="col-6 col-md-4">
    		<c:if test="${producto.productType != 'PARAFARMACIA'}">
    			<c:forEach items="${producto.tipoMedicamento}" var="tipo">
    				<br>
            		<h4><c:out value="${tipo.tipo}"/></h4>
            		<p><c:out value="${tipo.descripcion}"/></p>
            	</c:forEach>
    		</c:if>
    	</div>
    </div>
	<a href="/productos" class="btn btn-default">Volver</a>
    <spring:url value="{idProducto}/edit" var="editUrl">
        <spring:param name="idProducto" value="${producto.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Modificar</a>
</farmatic:layout>