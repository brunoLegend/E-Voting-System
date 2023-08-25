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


    <title>Create Election</title>
    <s:form action="createElection" method="post">
        <s:text name="Departamento dos votantes: " />
        <s:textfield name="departamento" /><br>
        <s:text name="Fun��o dos votantes (Estudante/Funcionario/Docente):" />
        <s:textfield name="funcao" /><br>
        <s:text name="Titulo da elei��o::" />
        <s:textfield name="titulo" /><br>
        <s:text name="Data de inicio da elei��o::" />
        <s:textfield name="data_init" /><br>
        <s:text name="Data de fim da elei��o:" />
        <s:textfield name="data_fim" /><br>
        <s:text name="mesa da elei��o:" />
        <s:textfield name="mesa" /><br>
        <s:text name="Descri��o da elei��o: " />
        <s:textfield name="descricao" /><br>
        <s:submit />
    </s:form>


    <p><a href="<s:url action="admin" />">Voltar</a></p>

</body>
</html>