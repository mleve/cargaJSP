<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=windows-1252"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <script type="text/javascript" src="javascript/jquery-1.9.1.min.js"></script>
    </head>
    <body>
        <label for="nombreCargo">Ingrese el cargo:</label>
        <input type="text" id="nombreCargo" name="cargo" />
        
        <button id="create" value="Crear" >Crear</button>
        <script type="text/javascript">
            $("#create").click(function(){
                $.ajax({
                type: "GET",
                url: "filegenerator",
                data: {cargo: $("#nombreCargo").val()}
                }).done(function(){
                    alert("volvi");
                });                
            });
        </script>
    </body>
</html>