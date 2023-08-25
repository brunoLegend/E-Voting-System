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

<form action="associarMesa" method="post">
  <h3>Eleições que podem ser adicionadas a uma mesa</h3>

  <c:choose>
    <c:when test="${heyBean.list_add != null}">
      <c:forEach items="${heyBean.list_add}" var="value">
      <p><input type="checkbox" name="election_add"  value="${value.title}" onclick="onlyOne(this)" /> <c:out value="${value.title}" /><br></p>
      <p><input type="submit" value="submit"/>
        </c:forEach>
    </c:when>
      <c:otherwise>
        <p>Todas as eleições já têm mesas associadas</p>
        </c:otherwise>
  </c:choose>

</form>

<script>
  function onlyOne(checkbox) {
    var checkboxes = document.getElementsByName('election_add')
    checkboxes.forEach((item) => {
      if (item !== checkbox) item.checked = false
    })
  }
</script>
<p><a href="<s:url action="gerirMesaspage" />">Voltar</a></p>
</body>
</html>
