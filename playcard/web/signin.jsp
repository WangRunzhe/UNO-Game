<%-- 
    Document   : signin
    Created on : 2016-1-25, 16:47:51
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
        <div id="logindiv">
            <form action="http://localhost:8080/playcard/login/signin" method="post">
                Player ID:
                <input type="text" name="loginid" id="playeid" size="30" />
                <br> <br>        
                Password: 
                <input type="password"  name="loginpaw" size="30" />

                <input type="submit" id="loginBtn" value="Log in" />
            </form>
            
            <h3>${sessionScope.status}</h3>
        </div>  
    </body>
</html>
