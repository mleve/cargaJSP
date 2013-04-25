<!DOCTYPE HTML>
<!-- <%@ page contentType="text/html;charset=windows-1252"%> -->
<html>
    <head>
    <title>Exportar Cargo</title>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <link rel="stylesheet" href="resources/css/ezmark.css" media="all">
        <link href="resources/css/styles.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="resources/js/jquery-1.9.1.min.js"></script>
    </head>
    <body>
    <div class="container">
      <header>
        <a href="menu.html"><img src="resources/images/header.jpg" alt="Andes Airport" width="900" height="90" id="logo" style="background: #42413C;; display:block;" /></a>
      </header>
      
     <div  align="center" class="div_header" >        
        <ul>
		<li><a href="menu.html" class="dropdown">Volver</a></li>		
	</ul>
      </div>
     <br clear="all"/>

     <article class="content">
     
     
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
                    alert("Generado con exito");
                });                
            });
        </script>
       </article>
       <footer>
    <p>sistema rostering andes v1.0</p>
    </footer></div>
    </body>
</html>