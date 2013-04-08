<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>holaMundo</title>
        
        <style type="text/css">
            #busyContainer{
                
            }        
        </style>
    </head>
    <body>
        <h1>Ingreso de Archivos </h1>
        <form method="POST" enctype="multipart/form-data" action="filereceiver">
            <input type="file" name="input" />
            <br>
            <input type="file" name="input2" />
            <br>
            <input type="submit" value="enviar"/>
        </form>
        
        <div id="busyContainer" >
        </div>
        
    </body>
</html>