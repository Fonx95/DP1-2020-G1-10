<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>


<farmatic:layout pageName="Pedidos">
    <jsp:body>
        <h2>Nuevo Pedido</h2>

		<h2><c:if test="${pedido['new']}"></c:if></h2>
        <form:form modelAttribute="pedido" class="form-horizontal">
            <div class="form-group has-feedback">
                <farmatic:inputField label="codigo" name="codigo"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="pedidoId" value="${pedido.id}"/>
                    <button class="btn btn-default" type="submit">Guardar pedido</button>
                </div>
            </div>
        </form:form>
        
        <c:out value="${mensaje}"/>
   
        <br/> 
        <b>LineaPedido</b>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Nombre</th>
                <th>Cantidad</th>
            </tr>
            <c:forEach var="lineaPedido" items="${pedido.lineaPedido}">
                    <tr>
                        <td><c:out value="${lineaPedido.producto.name}"/></td>
                        <td><c:out value="${lineaPedido.cantidad}"/></td>
                    </tr>
            </c:forEach>
            </thead>
        </table>    
    </jsp:body>

</farmatic:layout>