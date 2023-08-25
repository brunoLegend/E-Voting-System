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
<title>Detalhes da eleição</title>

<p>Número de votos : "${session.election.num_votes}"</p>
<p>Número de votos em branco: "${session.election.brancos}"</p>
<p>Número de votos nulos: "${session.election.nulos}"</p>
<p>Lista vencedora : "${session.election.winner}"</p>


<p><a href="<s:url action="gerirEleicao" />">Voltar</a></p>

</body>
</html>
