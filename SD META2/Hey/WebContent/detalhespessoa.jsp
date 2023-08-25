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
<h1>Detalhes de ${session.person.name}</h1>
<p>Nome: ${session.person.name}</p>
<p>Cc: ${session.person.id_card}</p>
<p>Time of vote: ${session.person.time_of_vote}</p>
<p>Place of vote: ${session.person.placeofVote}</p>

<p><a href="<s:url action="admin" />">Voltar->menu</a></p>
</body>


</html>
