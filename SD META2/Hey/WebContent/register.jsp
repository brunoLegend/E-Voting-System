<%--
  Created by IntelliJ IDEA.
  User: bruno
  Date: 11/05/2021
  Time: 17:45
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<head>
    <title>regist</title>
</head>
<body>
<p>READY FOR REGISTER!!</p>

<s:form action="register" method="post">
    <s:text name="Nome:" />
    <s:textfield name="nome" /><br>
    <s:text name="Cc:" />
    <s:textfield name="cc" /><br>
    <s:text name="Departamento:" />
    <s:textfield name="departamento" /><br>
    <s:text name="Password:" />
    <s:textfield name="password" /><br>
    <s:text name="Função (Estudante/Funcionario/Docente)::" />
    <s:textfield name="funcao" /><br>
    <s:submit />
</s:form>

<p><a href="<s:url action="admin" />">Voltar</a></p>
</body>




</html>
