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
    <title>Bem-vindo</title>

  <form action="vote" method="post">
    <h3>Selecione a sua Lista Candidata preferida!</h3>
    <c:forEach items="${session.eleicao.candidates}" var="value">
      <p><input type="checkbox" name="lista"  value="${value.key.name}" onclick="onlyOne(this)" /> <c:out value="${value.key.name}" /><br></p>

        </c:forEach>
    <p><input type="checkbox" name="lista"  value="Branco" onclick="onlyOne(this)" /> <c:out value="Voto em Branco" /><br></p>
    <p><input type="checkbox" name="lista"  value="Nulo" onclick="onlyOne(this)" /> <c:out value="Voto Nulo" /><br></p>
    <p><input type="submit" value="submit"/>
  </form>
</head>
<body>
<script>
function onlyOne(checkbox) {
var checkboxes = document.getElementsByName('lista')
checkboxes.forEach((item) => {
if (item !== checkbox) item.checked = false
})
}
</script>

<button><a href="<s:url action="logout"/>">logout</a></button>


</body>
</html>
