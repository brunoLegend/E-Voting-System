<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<head>

    <title>Yo soy admin</title>
</head>
<body>
<p>BIEN VENIDO ADMIN</p>



<p><a href="<s:url action="registerpage" />">Registar utilizador</a></p>
<p><a href="<s:url action="createElectionpage" />">Criar eleição</a></p>
<p><a href="<s:url action="gerirEleicaopage" />">Gerir eleição</a></p>
<p><a href="<s:url action="consultardetalhespage" />">Consultar detalhes de uma pessoa</a></p>
<p><a href="<s:url action="gerirMesaspage" />">Gerir Mesas de voto</a></p>
<p><a href="<s:url action="onlineUsers" />">Ver online users</a></p>
<p><a href="<s:url action="electionlive" />">Ver eleição em direto</a></p>



<button><a href="<s:url action="logout"/>">logout</a></button>

</body>
</html>
