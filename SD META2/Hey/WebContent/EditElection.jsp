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
    <title>Write on the field(s) you want to change</title>

    <title>Current Data</title>

    <p>Titulo: "${session.election.title}"</p>
    <p>Descricao: "${session.election.descriprion}"</p>
    <p>Nome da mesa: "${session.election.nome_mesa}"</p>
    <p>Funcao do eleitor: "${session.election.voter_type}"</p><br>

    <p><a href="<s:url action="chooseListaspage" />">Gerir Listas Candidatas</a></p>
    <h3>Listas Candidatas:</h3>
    <c:forEach items="${session.election.candidates}" var="value">
        <h4><c:out value="${value.key.name}" /></h4>
        <c:forEach items="${value.key.candidates}" var="value2">


            <c:out value="(${value2.name},${value2.id_card})" /><br>



        </c:forEach>




    </c:forEach>

    <br>



    <s:form action="EditElection" method="post">
        <s:text name="Novo titulo:" />
        <s:textfield name="titulo" /><br>
        <s:text name="Nova descricao:" />
        <s:textfield name="descricao" /><br>
        <s:text name="Novo lugar da mesa:" />
        <s:textfield name="nome_mesa" /><br>
        <s:text name="Nova funcao do eleitor:" />
        <s:textfield name="funcao" /><br>
        <s:text name="Adicionar lista:" />
        <s:textfield name="lista_add" /><br>
        <s:text name="Remover Lista:" />
        <s:textfield name="lista_remove" /><br>
        <s:submit />
    </s:form>



    <p><a href="<s:url action="admin" />">Voltar</a></p>


</body>
</html>
