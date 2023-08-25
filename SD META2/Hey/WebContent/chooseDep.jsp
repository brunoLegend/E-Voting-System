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

    <form action="chooseDep" method="post">
        <h3>Selecione a mesa!</h3>
        <c:forEach items="${heyBean.mesa}" var="value">
            <c:if test="${value.value == true}">
                <p><input type="checkbox" name="dep"  value="${value.key}" onclick="onlyOne(this)" /> <c:out value="${value.key}" /><br></p>
            </c:if>

        </c:forEach>
        <p><input type="submit" value="submit"/>
    </form>



</head>
<body>

<script>
    function onlyOne(checkbox) {
        var checkboxes = document.getElementsByName('dep')
        checkboxes.forEach((item) => {
            if (item !== checkbox) item.checked = false
        })
    }
</script>


<p><a href="<s:url action="admin" />">Voltar</a></p>

</body>
</html>
