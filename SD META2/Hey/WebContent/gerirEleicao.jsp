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
</head>
<body>

<p>Escolha a eleicao que pretende modificar: </p>

<c:forEach items="${heyBean.listelections}" var="value">
  <c:choose>
    <c:when test="${value.state == 2}">
      <c:out value="${value.title}" /><p>(terminada)</p><br>
  </c:when>

  <c:when test="${value.state == 1}">
      <c:out value="${value.title}" /><p>(a decorrer)</p><br>
  </c:when>
    <c:when test="${value.state == 0}">
      <c:out value="${value.title}" /><p>(por começar)</p><br>
    </c:when>
</c:choose>
</c:forEach>







<s:form action="gerirEleicao" method="post">
  <s:text name="nome da eleicao que pretende editar/consultar:" />
  <s:textfield name="nome_elec" /><br>
  <s:submit />
</s:form>


<p><a href="<s:url action="admin" />">Voltar</a></p>
</body>







</body>
</html>
