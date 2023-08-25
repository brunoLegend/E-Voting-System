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
    <title>Title</title>
</head>
<body>
<h3>Assista a uma das seguintes eleições em direto!</h3>

  <c:forEach items="${heyBean.listelections}" var="value">
  <c:choose>

  <c:when test="${value.state == 1}">
    <c:out value="${value.title}" /><br>
  </c:when>

  </c:choose>
  </c:forEach>

  <p><a href="<s:url action="index" />">Start</a></p>


  <s:form action="assistirlive" method="post">
    <s:text name="nome da eleicao que pretende visualizar:" />
    <s:textfield name="nome_live" /><br>
    <s:submit />
  </s:form>
  </body>


</html>
