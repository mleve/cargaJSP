<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>holaMundo</title>
    </head>
    <body>
        <h1>Hola mundo </h1>
        <form method="POST" enctype="multipart/form-data" action="filereceiver">
            <input type="file" name="input" />
            <input type="submit" value="enviar"/>
        </form>
        
    </body>
</html>