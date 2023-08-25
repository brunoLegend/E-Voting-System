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
</head>
<body>

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
      writeToHistory('Get a real browser which supports WebSocket.');
      return;
    }

    websocket.onopen    = onOpen; // set the 4 event listeners below
    websocket.onclose   = onClose;
    websocket.onmessage = onMessage;
    websocket.onerror   = onError;
  }

  function onOpen(event){
    var sessionValue = '<%=session.getAttribute("live")%>';
    doSend("vote "+sessionValue);
    //writeToHistory('Connected to ' + window.location.host + '.');


  }

  function onClose(event) {
    writeToHistory('WebSocket closed (code ' + event.code + ').');

  }

  function onMessage(message) { // print the received message
    console.log("m " + message.data);
    var split = new Array();
    split = message.data.toString().split(" ");
    console.log(split[0].trim());
    if (split[0].trim() == "votes") {
      writeToHistory(split[2] + "\n");
      writeToHistory(split[0] + " " + split[1] + " " + split[3]);
    }
    else if (split[0].trim() == "vote") {
      writeToHistory(message.data);
    }
  }

  function onError(event) {
    writeToHistory('WebSocket error.');

  }

  function doSend(message) {

    websocket.send(message); // send the message to the server

  }

  function writeToHistory(text) {
    var history = document.getElementById('history');
    var line = document.createElement('p');
    line.style.wordWrap = 'break-word';
    line.innerHTML = text;
    history.appendChild(line);
    history.scrollTop = history.scrollHeight;
  }


</script>

<div>
  <div id="container"><div id="history"></div></div>

</div>


<p><a href="<s:url action="admin" />">Voltar</a></p>

</body>
</html>
