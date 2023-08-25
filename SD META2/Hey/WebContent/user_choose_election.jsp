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
    <title>Bem-Vindo</title>
</head>
<body>


<form action="votepage" method="post">
  <h3>Selecione a eleição na qual pretende votar!</h3>
    <jsp:useBean id="test" class="hey.model.HeyBean"/>

<c:choose>
  <c:when test="${test.getUserlistelections(session.username,session.password)==null} ">
      <c:out value="Não pode votar em nehuma eleicao" /><br>
  </c:when>
<c:otherwise>

<c:forEach items="${test.getUserlistelections(session.username,session.password)}" var="value">

    <p><input type="checkbox" name="eleicao"  value="${value.title}" onclick="onlyOne(this)" /> <c:out value="${value.title}" /><br></p>

    <p><input type="submit" value="submit" ></p>



</c:forEach>
  </c:otherwise>
</c:choose>
</form>

<script>
function onlyOne(checkbox) {
var checkboxes = document.getElementsByName('eleicao')
checkboxes.forEach((item) => {
if (item !== checkbox) item.checked = false
})
}
</script>


<script type="text/javascript">

    var websocket = null;



    window.onload = function() { // URI = ws://10.16.0.165:8080/WebSocket/ws
        connect('ws://'+window.location.host+'/hey2/ws');


        //http://localhost:8080/project7_war_exploded/chat.html?room=1
    }

    function connect(host) { // connect to the host websocket
        if ('WebSocket' in window)
            websocket = new WebSocket(host);
        else if ('MozWebSocket' in window)
            websocket = new MozWebSocket(host);
        else {

            return;
        }

        websocket.onopen    = onOpen; // set the 4 event listeners below
        websocket.onclose   = onClose;
        websocket.onmessage = onMessage;
        websocket.onerror   = onError;
    }

    function onOpen(event) {
        var sessionValue = '<%=session.getAttribute("username")%>';

        console.log("seeeee "+sessionValue);
        doSend("new user "+sessionValue);
        // writeToHistory('Connected to ' + window.location.host + '.');


    }

    function onClose(event) {
        writeToHistory('WebSocket closed (code ' + event.code + ').');

    }

    function onMessage(message) { // print the received message
        console.log("Número de utilizadores online: "+message);

    }

    function onError(event) {


    }

    function doSend(message) {

        websocket.send(message); // send the message to the server

    }



</script>


<button><a href="<s:url action="logout"/>">logout</a></button>
</body>
</html>
