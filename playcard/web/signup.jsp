<%-- 
    Document   : signup
    Created on : 2016-1-26, 16:13:08
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
         <form action="http://localhost:8080/playcard/login/signup" method="post">
            Player ID:
            <input type="text" name="playeridtxt" size="30" />
            <br> <br>         
            Password: 
            <input type="text"  name="passwordtxt" size="30" />
            <input type="submit" id="subBtn" value="Create New Player" />
        </form>
        
        <h3>${sessionScope.status}</h3>
    </body>
</html>
