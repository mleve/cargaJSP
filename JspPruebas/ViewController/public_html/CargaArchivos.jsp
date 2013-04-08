<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=windows-1252"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>holaMundo</title>
        <script language="JavaScript" src="resources/js/jquery-1.9.1.min.js"></script>
        <style type="text/css">
            #busyContainer{
                display:none;
                position:absolute;
                top:100px;
                left:100px;
            }        
        </style>
    </head>
    <body>
        <div id="cargaFormDiv">
        <h1>Seleccione que archivos subira </h1>
        <div id="checkBoxesDiv">
            <input type="checkbox" name="vehicle" value="empleado">empleados.csv<br>
            <input type="checkbox" name="vehicle" value="Car">cargos.csv</br> 
        </div>
        <h1>Ingreso de Archivos </h1>
        <form method="POST" enctype="multipart/form-data" action="filereceiver">
            <label for="empleados">Archivo Empleados.csv</label>
            <input id="empleados" type="file" name="input" />
            <br>
            <label for="cargos">Archivo cargos.csv</label>
            <input id="cargos" type="file" name="input" />
            <br>
            <label for="skills">Archivo Empleados.csv</label>
            <input id="skills" type="file" name="input" />
            <br>
            <label for="turnos">Archivo turnos.csv</label>
            <input id="turnos" type="file" name="input" />
            <br>
            <label for="turnosNoP">Archivo turnosNoPermitidos.csv</label>
            <input id="turnosNoP" type="file" name="input" />
            <br>
            <label for="vacaciones">Archivo vacaciones.csv</label>
            <input id="vacaciones" type="file" name="input" />
            <br>
            <label for="capacitaciones">Archivo capacitaciones.csv</label>
            <input id="capacitaciones" type="file" name="input" />
            <br>
            <input type="submit" onclick="showBusy()" value="enviar"/>
        </form>
        </div>
        <div id="busyContainer" >
            <img src="busy2.gif" />
        </div>
    <script type="text/javascript">
    $("#checkBoxesDiv input").click(function(){
        alert("hola");
    });
    
    function showBusy(){
            document.getElementById("busyContainer").style.display="block";
            document.getElementById("cargaFormDiv").style.display="none";
    };
    
    </script>
    </body>
</html>