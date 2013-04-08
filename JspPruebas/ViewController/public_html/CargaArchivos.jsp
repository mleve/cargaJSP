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
    </body>
</html>