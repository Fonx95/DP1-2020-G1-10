<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags" %>


<farmatic:layout pageName="Pedidos">
    <jsp:body>
        <h2>Pedido</h2>


        <form:form modelAttribute="pedido" class="form-horizontal">
            <div class="form-group has-feedback">
                <farmatic:inputField label="codigo" name="codigo"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="pedidoId" value="${pedido.id}"/>
                    <button class="btn btn-default" type="submit">terminar pedido</button>
                </div>
            </div>
        </form:form>
        
        <br/>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>fechaPedido</th>
                <th>fechaEntrega</th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${LocalDate.now()}"/></td>
                <td><c:out value="${pedido.fechaEntrega}"/></td>
            </tr>
        </table>    
        
        <br/> 
        <b>LineaPedido</b>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Nombre</th>
                <th>Cantidad</th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${pedido.lineaPedido.producto.name}"/></td>
                <td><c:out value="${pedido.lineaPedido.cantidad}"/></td>
            </tr>
        </table>    
    </jsp:body>

</farmatic:layout>