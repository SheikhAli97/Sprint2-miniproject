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
    <title>Product Editing</title>
</head>
<body>
<h2>New/edit Product Information</h2>
<form:form method="POST" modelAttribute="product" action="/product/add">
   <table>
    <tr>
        <td><form:label path="id">Id</form:label></td>
        <td><form:input path="id" readonly="true"/></td>
    <tr>
        <td><form:label path="name">Name</form:label></td>
        <td><form:input path="name" /></td>
        <td><form:errors path="name" cssClass="error"/></td>
    </tr>
    <tr>
        <td><form:label path="description">Description</form:label></td>
        <td><form:input path="description" /></td>
        <td><form:errors path="description" cssClass="error"/></td>
    </tr>
    <tr>
        <td><form:label path="price">Price</form:label></td>
        <td><form:input path="price" /></td>
        <td><form:errors path="price" cssClass="error"/></td>
    </tr>
    <tr>
        <td colspan="2">
            <input type="submit" value="Submit" class="btn btn-default"/>
        </td>
    </tr>
</table>  
</form:form>
</body>
</html>
