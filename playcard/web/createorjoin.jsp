<%-- 
    Document   : createorjoin
    Created on : 2016-1-25, 16:23:56
    Author     : Summer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

    </head>
    <body>
        <h1 align="center"> Welcome To UNO Games</h1>
        <h2 align="center" id="showplayerid">Player ID: ${sessionScope.id} </h2>        
<!--        怎么从这个里面把playerid给取出来。-->
        <br><br>
        <a href="/playcard/table.html?playerid=${sessionScope.id}" >Create Game</a>
        <br><br>
        <a href="/playcard/player.html?playerid=${sessionScope.id}">Join Game</a>
    </body>
</html>
