<%--
  Created by IntelliJ IDEA.
  User: bruno
  Date: 13/05/2021
  Time: 19:06
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Escolha a lista que pretende editar</title>
</head>
<body>

<form action="chooseListas" method="post">
    <h3>Selecione a sua Lista Candidata preferida!</h3>
    <c:forEach items="${session.election.candidates}" var="value">
    <p><input type="checkbox" name="lista"  value="${value.key.name}" onclick="onlyOne(this)" /> <c:out value="${value.key.name}" /><br></p>
    <p><input type="submit" value="submit"/>
        </c:forEach>
</form>

<p><a href="<s:url action="gerirEleicaopage" />">Voltar</a></p>
</body>
</html>
