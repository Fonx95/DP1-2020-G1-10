<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="name" required="true" rtexprvalue="true"
              description="Name of corresponding property in bean object" %>
<%@ attribute name="label" required="true" rtexprvalue="true"
              description="Label appears in red color if input is considered as invalid after submission" %>
<%@ attribute name="items" required="true" rtexprvalue="true" type="java.util.List"
              description="Names in the list" %>
<%@ attribute name="values" required="false" rtexprvalue="true" type="java.util.List"
              description="Valor de la variable a comparar" %>

<div class="form-group">
		<label class="col-sm-2 control-label">${label}</label>
		<div class="col-sm-10">
		<select class="form-control" name="${name}" multiple size="5">
			<c:forEach items="${items}" var="item">
				<c:set var="contiene" value="false"/>
				<c:forEach items="${values}" var="value">
					<c:if test="${value.tipo == item.tipo}">
						<c:set var="contiene" value="true"/>
					</c:if>
				</c:forEach>
				<option ${contiene ? 'selected' : ''}  value="${item.id}">${item.tipo}</option>
			</c:forEach>
		</select>
	</div>
</div>
