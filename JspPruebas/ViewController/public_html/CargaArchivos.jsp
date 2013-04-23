<!DOCTYPE HTML>
<!--<%@ page contentType="text/html;charset=windows-1252"%>-->
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>Carga de Archivos</title>
        
        <link rel="stylesheet" href="resources/css/ezmark.css" media="all">
        <link href="resources/css/styles.css" rel="stylesheet" type="text/css">
        <script language="JavaScript" src="resources/js/jquery-1.9.1.min.js"></script>

        <!-- styles y jquery de checkboxs -->
        
        <script type="text/javascript" language="Javascript" src="resources/js/jquery.ezmark.min.js"></script>
        <!-- -->
        <style type="text/css">
            #busyContainer{
                display:none;
                position:absolute;
                top:100px;
                left:100px;
            }        
        </style>
        <script type="text/javascript">
        $(function() {
                $('input[type="checkbox"]').ezMark(); 
                
                // check all functionality
                $('#checkAllBtn').click(function(e) {
                        $('input[name^="item"]').each(function() {
                                $(this).attr({"checked":"checked"});
                                $(this).trigger('change');
                        });
                        return false;
                });
        
        
                // uncheck all functionality
                $('#uncheckAllBtn').click(function(e) {
                        $('input[name^="item"]').each(function() {
                                $(this).removeAttr('checked');
                                $(this).trigger('change');
                        });
                        return false;
                });
                
        });
        
        </script>
        <script type="text/javascript">

        $(function(){
                $('.dropdown').mouseenter(function(){
                        $('.sublinks').stop(false, true).hide();
                
                        var submenu = $(this).parent().next();
        
                        submenu.css({
                                position:'absolute',
                                top: $(this).offset().top + $(this).height() + 'px',
                                left: $(this).offset().left + 'px',
                                zIndex:1000
                        });
                        
                        submenu.stop().slideDown(300);
                        
                        submenu.mouseleave(function(){
                                $(this).slideUp(300);
                        });
                });
        });
        </script>
        <script type="text/javascript">
        $("#checkBoxesDiv input").click(function(){
            if($($(this).val()).css("display")=="none")
                $($(this).val()).css("display","");
            else
                $($(this).val()).css("display","none");
        });
        
        function showBusy(){
                document.getElementById("busyContainer").style.display="block";
                document.getElementById("cargaFormDiv").style.display="none";
        };
        
        </script>
    </head>
    <body>
    <div class="container">
      <header>
        <a href="menu.html"><img src="resources/images/header.jpg" alt="Andes Airport" width="900" height="90" id="logo" style="background: #42413C;; display:block;" /></a>
      </header>
      
      <div  align="center" class="div_header" >
	
        <ul>
            <li>
                <label for="mes">Cargo: </label>
                <select id="mes">
                    <option value="R01">JEFE TURNO RAMPA</option>
                    <option value="R03">MONITOR VUELO</option>
                    <option value="R04">MONITOR TOWING</option>
                    <option value="R05">INSTRUCTOR</option>
                    <option value="R06">OPERARIO AIRE ACONDICIONADO</option>
                    <option value="R10">OPERARIO RAMPA</option>
                    <option value="R11">COORDINADOR SERVICIOS A TERCEROS</option>
                    <option value="R20">TRACTORISTA RAMPA</option>
                    <option value="R21">OPERADOR BOMBA COMBUSTIBLE</option>
                    <option value="R41">OPERARIO RAMPA PT</option>
                    <option value="R23">CARGOLOADERISTA</option>
                    <option value="R02">JEFE TURNO OPCO</option>
                    <option value="R07">CONVEYOR</option>
                    <option value="R24">CONDUCTOR CAMION SERVICIO</option>
                    <option value="R40">TRACTORISTA CARGA</option>
                    <option value="R12">CONDUCTOR CARRUSEL</option>
                    <option value="R14">TRACTORISTA ULD</option>
                    <option value="R22">MONITOR OPCO</option>
                    <option value="C03">MONITOR VUELO CARGA</option>
                    <option value="C10">OPERARIO CARGA</option>
                    <option value="E01">SUPERVISOR EQUIPAJE</option>
                    <option value="E02">COORDINADOR EQUIPAJE</option>
                    <option value="E13">OPERARIO EQUIPAJE</option>
                    <option value="E04">MONITOR COUNTER</option>
                    <option value="E06">MONITOR CINTA</option>
                    <option value="E07">MONITOR TRANSFERENCIA</option>
                    <option value="E10">OPERARIO COUNTER</option>
                    <option value="E12">OPERARIO EQUIPAJE PT</option>
                    <option value="E20">TRACTORISTA EQUIPAJE</option>
                    <option value="E05">COORDINADOR COUNTER</option>
                    <option value="O11">SUPERVISOR SERVICIO A TERCEROS</option>
                    <option value="O16">ADMINISTRACION</option>
                    <option value="T01">JEFE DE TALLER</option>
                    <option value="T02">JEFE BODEGA</option>
                    <option value="T03">ENCARGADO COMPRAS</option>
                    <option value="T04">ADMINISTRATIVO TALLER</option>
                    <option value="T05">SUPERVISOR TALLER</option>
                    <option value="T06">BODEGUERO</option>
                    <option value="T07">OPERADOR BOMBA COMBUSTIBLE</option>
                    <option value="T10">EXTRUCTURISTA TALLER</option>
                    <option value="T13">PINTOR</option>
                    <option value="T11">EXTRUCTURISTA DOLLIES</option>
                    <option value="T12">VULCANIZADOR</option>
                    <option value="T24">ELECTRICO</option>
                    <option value="T20">MECANICO CAJA CAMBIOS</option>
                    <option value="T21">MECANICO MOTORES</option>
                    <option value="T22">MECANICO HIDRAULICO</option>
                    <option value="T23">MECANICO GENERAL</option>
                    <option value="T30">AYUDANTE MECANICO</option>
                    <option value="CR10">OPERARIO</option>
                    <option value="CR03">MONITORES</option>
                </select>
            </li>
        </ul>
        
        <ul>
            <li>
                <label for="mes">Mes: </label>
                <select id="mes">
                  <option value="04">Abril</option>
                  <option value="05">Mayo</option>
                  <option value="06">Junio</option>
                  <option value="07">Julio</option>
                </select>
            </li>
        </ul>
        <ul>
            <li>
                <label for="anno">A&ntilde;o: </label>
                <select id="anno">
                  <option value="2012">2012</option>
                  <option value="2013">2013</option>
                </select>
            </li>
        </ul>
        
        
        <ul>
		<li><a href="menu.html" class="dropdown">Volver</a></li>		
	</ul>
      </div>
     <br clear="all"/>
      
      <article class="content">
            <div id="cargaFormDiv">
            <h1>Seleccione que archivos subira </h1>
            <div id="checkBoxesDiv">
                <input type="checkbox" name="vehicle" value="#divEmpl">empleados.csv<br>
                <input type="checkbox" name="vehicle" value="#divCargos">cargos.csv</br> 
                <input type="checkbox" name="vehicle" value="#divSkills">skills.csv</br>
                <input type="checkbox" name="vehicle" value="#divTurnos">turnos.csv</br>
                <input type="checkbox" name="vehicle" value="#divTurnosNoP">turnosNoPermitidos.csv</br>
                <input type="checkbox" name="vehicle" value="#divVacaciones">vacaciones.csv</br>
                <input type="checkbox" name="vehicle" value="#divCoAsignaciones">capacitaciones.csv</br>
                <input type="checkbox" name="vehicle" value="#divAsigFijadas">asignacionesFijadas.csv</br>
                <input type="checkbox" name="vehicle" value="#divCapacity">capacity.csv</br>
                <input type="checkbox" name="vehicle" value="#divDemandaHora">demandaHora.csv</br>
                <input type="checkbox" name="vehicle" value="#divDemandaSkills">demandaSkills.csv</br>
            </div>
            
           
            <h1>Ingreso de Archivos </h1>
            <form method="POST" enctype="multipart/form-data" action="filereceiver">
                <div id="divEmpl" style="display:none">
                <label for="empleados">Archivo Empleados.csv</label>
                <input id="empleados" type="file" name="input" />
                <br>
                </div>
                <div id="divCargos" style="display:none">
                <label for="cargos">Archivo cargos.csv</label>
                <input id="cargos" type="file" name="input" />
                <br>
                </div>
                <div id="divSkills" style="display:none">
                <label for="skills">Archivo Skills.csv</label>
                <input id="skills" type="file" name="input" />
                <br>
                </div>
                 <div id="divTurnos" style="display:none">
                <label for="turnos">Archivo turnos.csv</label>
                <input id="turnos" type="file" name="input" />
                <br>
                </div>
                 <div id="divTurnosNoP" style="display:none">
                <label for="turnosNoP">Archivo turnosNoPermitidos.csv</label>
                <input id="turnosNoP" type="file" name="input" />
                <br>
                </div>
                 <div id="divVacaciones" style="display:none">
                <label for="vacaciones">Archivo vacaciones.csv</label>
                <input id="vacaciones" type="file" name="input" />
                <br>
                </div>
                 <div id="divCapacitaciones" style="display:none">
                <label for="capacitaciones">Archivo capacitaciones.csv</label>
                <input id="capacitaciones" type="file" name="input" />
                <br>
                </div>
                 <div id="divCoAsignaciones" style="display:none">
                <label for="coAsignaciones">Archivo coAsignaciones.csv</label>
                <input id="coAsignaciones" type="file" name="input" />
                <br>
                </div>
                 <div id="divAsigFijadas" style="display:none">
                <label for="AsigFijadas">Archivo AsignacionesFijadas.csv</label>
                <input id="AsigFijadas" type="file" name="input" />
                <br>
                </div>
                <div id="divCapacity" style="display:none">
                <label for="capacity">Archivo capacity.csv</label>
                <input id="capacity" type="file" name="input" />
                <br>
                </div>
                <div id="divDemandaHora" style="display:none">
                <label for="demandaHora">Archivo demandaHora.csv</label>
                <input id="demandaHora" type="file" name="input" />
                <br>
                </div>
                <div id="divDemandaSkills" style="display:none">
                <label for="demandaSkills">Archivo demandaSkills.csv</label>
                <input id="demandaSkills" type="file" name="input" />
                <br>
                </div>
                <input type="submit" onclick="showBusy()" value="enviar"/>
            </form>
            </div>
            <div id="busyContainer" >
                <img src="busy2.gif" />
            </div>
       <script type="text/javascript">
        $("#checkBoxesDiv input").click(function(){
            if($($(this).val()).css("display")=="none")
                $($(this).val()).css("display","");
            else
                $($(this).val()).css("display","none");
        });
        
        function showBusy(){
                document.getElementById("busyContainer").style.display="block";
                document.getElementById("cargaFormDiv").style.display="none";
        };
        
        </script>
        </article>
    </div>
     <footer>
    <p>sistema rostering andes v1.0</p>
</footer>
    </body>
</html>