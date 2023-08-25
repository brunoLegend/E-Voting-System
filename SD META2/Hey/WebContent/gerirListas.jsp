<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Adicione ou remova pessoas à lista!</title>
</head>
<body>

<s:form action="gerirlistas" method="post">
  <p>Adicionar:</p>
  <s:text name="Nome da pessoa :" />
  <s:textfield name="nome_add" /><br>
  <s:text name="Cc da pessoa:" />
  <s:textfield name="cc_add" /><br>
  <p>Remover:</p>
  <s:text name="Nome da pessoa :" />
  <s:textfield name="nome_rem" /><br>
  <s:text name="Cc da pessoa:" />
  <s:textfield name="cc_rem" /><br>

  <s:submit />
</s:form>

<p><a href="<s:url action="chooseListaspage" />">Voltar</a></p>

</body>


</html>
