<!DOCTYPE HTML>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" media="screen" href="/webjars/bootstrap/3.3.7/css/bootstrap.min.css">
<style type="text/css">
	.error {
		color: #ff0000;
	}
	.errorblock{
		color: #000;
		background-color: #ffEEEE;
		border: 3px solid #ff0000;
		padding:8px;
		margin:16px;
	}
</style>
<html>
<head>
    <title>Order Item Editing</title>
</head>
<body>
<h2>New/edit Order Item Information</h2>
<form:form method="POST" modelAttribute="itemFormDto" action="/item/add">
   <table>
    <tr>
        <td><form:label path="productId">Product</form:label></td>
    	<td><form:select path="productId">
    		<c:choose>
				<c:when test="${itemFormDto.getProductId() < 0}">
					<form:option value="0" label="--- none ---"/>
				</c:when>
				<c:otherwise>
					<form:option value="-1" label="--- none ---" selected="selected"/>
				</c:otherwise>
			</c:choose>
			<c:forEach var="item" items="${itemFormDto.getProductList()}">
				<c:choose>
					<c:when test="${itemFormDto.getProductId()==item.getId()}">
						<form:option value="${item.getId()}" label="${item.getName()}" selected="selected"/>
					</c:when>
					<c:otherwise>
						<form:option value="${item.getId()}" label="${item.getName()}"/>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</form:select></td>
		<td><form:errors path="productId" cssClass="error"/></td>
    </tr>
    <tr>
        <td><form:label path="amount">Amount</form:label></td>
        <td><form:input path="amount"/></td>
        <td><form:errors path="amount" cssClass="error"/></td>
    </tr>
    <form:hidden path="id" />
    <form:hidden path="orderId" />
    <tr>
        <td colspan="2">
            <input type="submit" name="action" value="Submit" class="btn btn-default"/>
            <input type="submit" name="action" value="Cancel" class="btn btn-default"/>
        </td>
    </tr>
</table>  
</form:form>
</body>
</html>
