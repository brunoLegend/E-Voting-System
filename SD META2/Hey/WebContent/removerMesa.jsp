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
    <title>Title</title>
</head>
<body>

<form action="removerMesa" method="post">
  <h3>Remova a mesa de uma das seguintes eleições</h3>

  <c:choose>
    <c:when test="${heyBean.list_rem != null}">
      <c:forEach items="${heyBean.list_rem}" var="value">
        <p><input type="checkbox" name="election_rem"  value="${value.title}" onclick="onlyOne(this)" /> <c:out value="${value.title}" /><br></p>
        <p><input type="submit" value="submit"/>
      </c:forEach>
    </c:when>
    <c:otherwise>
      <p>Nenhuma eleição tem mesa associada</p>
    </c:otherwise>
  </c:choose>

</form>

<script>
  function onlyOne(checkbox) {
    var checkboxes = document.getElementsByName('election_rem')
    checkboxes.forEach((item) => {
      if (item !== checkbox) item.checked = false
    })
  }
</script>

<p><a href="<s:url action="gerirMesaspage" />">Voltar</a></p>
</body>
</html>
