<!DOCTYPE HTML>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" media="screen" href="/webjars/bootstrap/3.3.7/css/bootstrap.min.css">
<!-- source: http://www.textfixer.com/tutorials/css-table-alternating-rows.php -->
<style type="text/css">
	.TFtable{
		width:100%; 
		border-collapse:collapse; 
	}
	.TFtable td{ 
		padding:7px; border:#4e95f4 1px solid;
	}
	/* provide some minimal visual accomodation for IE8 and below */
	.TFtable tr{
		background: #b8d1f3;
	}
	/*  Define the background color for all the ODD background rows  */
	.TFtable tr:nth-child(odd){ 
		background: #b8d1f3;
	}
	/*  Define the background color for all the EVEN background rows  */
	.TFtable tr:nth-child(even){
		background: #dae5f4;
	}
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
    <title>Product Deal Management</title>
</head>
<body>
<h1>Product Deal Management</h1>
<section>
<a id="ToMainBtn" href="/" class="btn btn-default">Main page</a>
<p/>
</section>
<div>
<form:form method="POST" modelAttribute="dealFormDto" action="/deal/add">
   <table>
    <tr>
        <td colspan="2"><form:label path="productId">Product</form:label></td>
    		<td colspan="2"><form:select path="productId">
    			<c:choose>
				<c:when test="${dealFormDto.getProductId() < 0}">
					<form:option value="-1" label="--- none ---"/>
				</c:when>
			</c:choose>
			<c:forEach var="item" items="${dealFormDto.getProductList()}">
				<c:choose>
					<c:when test="${dealFormDto.getProductId()==item.getId()}">
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
        <td colspan="2"><form:label path="discount">Discount</form:label></td>
        <td colspan="2"><form:input path="discount" /></td>
        <td><form:errors path="discount"  cssClass="error" /></td>
    </tr>
    <tr>
        <td colspan="2"><form:label path="startDate">Start Date</form:label></td>
        <td><form:input path="startDate" /></td>
        <td><form:errors path="startDate"  cssClass="error" /></td>
        <td colspan="2"><form:label path="endDate">End Date</form:label></td>
        <td><form:input path="endDate" /></td>
        <td><form:errors path="endDate"  cssClass="error" /></td>
    </tr>
    <tr>
        <td colspan="2">
            <input id="PublishBtn" type="submit" name="action" value="Publish" class="btn btn-default"/>
        </td>
    </tr>
</table>  
</form:form>
</div>
<table id="dealTable" class="TFtable">
<tr>
  <td><h3>Id</h3></td>
  <td><h3>Product</h3></td>
  <td><h3>Discount</h3></td>
  <td><h3>Start date</h3></td>
  <td><h3>End date</h3></td>
  <td><h3>Active?</h3></td>
  <td><h3></h3></td>
</tr>
<c:forEach items="${dealList}" var="deal">
<tr>
	<td><c:out value="${deal.getId()}"/></td>
	<td><c:out value="${deal.getProduct().getName()}"/></td>
	<td><c:out value="${deal.getDiscount()}"/></td>
	<td>	<c:out value="${deal.getStartDateAsString()}"/></td>
	<td>	<c:out value="${deal.getEndDateAsString()}"/></td>
	<td><c:out value="${deal.isActive()}"/></td>
	<td><a id="CloseBtn${deal.getId()}" href="/deal/delete?dealId=${deal.getId()}" class="btn btn-default">CLOSE</a></td>
</tr>
</c:forEach>
</table>
</body>
</html>
