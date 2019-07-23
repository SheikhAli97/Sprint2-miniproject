<!DOCTYPE HTML>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
</style>
<html>
<head>
    <title>All orders</title>
</head>
<body>
<h2>All orders</h2>
<section>
<a href="/order/add/" class="btn btn-default">Add new order</a>
<a href="/" class="btn btn-default">Main page</a>
<p/>
</section>
<table class="TFtable">
<tr>
  <td><h3>Id</h3></td>
  <td><h3>Date</h3></td>
  <td><h3>Description</h3></td>
  <td><h3>Cost</h3></td>
  <td><h3></h3></td>
  <td><h3></h3></td>
</tr>
<c:forEach items="${orderList}" var="order">
<tr>
	<td><c:out value="${order.getId()}"/></td>
	<td><c:out value="${order.getDate()}"/></td>
	<td><c:out value="${order.getDescription()}"/></td>
	<td><c:out value="${order.getCost()}"/></td>
	<td><a href="/order/add?orderId=${order.getId()}">Edit</a></td>
	<td><a href="/order/delete?orderId=${order.getId()}">Delete</a></td>
</tr>
</c:forEach>
</table>
</body>
</html>
