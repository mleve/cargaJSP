package view;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.Calendar;

import java.util.Date;

import java.util.Locale;

import javax.servlet.*;
import javax.servlet.http.*;

public class FileGenerator extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        String idCargo = null;
        
        try{
            idCargo = (String)request.getParameter("cargo");
        }
        catch(Exception e){
            idCargo=null;
        }
        if(idCargo != null){
        //Se llamo a este servlet pasando el argumento necesario
            //Crear la nueva carpeta
            String folderName="C:\\"+idCargo;
            new File(folderName).mkdir();
            //Carga de clases auxiliares:
            DbManager dbmg = new DbManager("dev","dev","xe");

            /*Generador de los archivos de input del programa
             * Si se requiere algun postProceso luego de obtener la informacion
             * desde la base de datos, se llama a un metodo que lo realize
             */ 
            
            for(int i =2; i<=7;i++){
                String fileName = getNextFileName(i);
                ResultSet table = null;
                CSVWriter writer = new CSVWriter(new FileWriter(folderName+"\\"+fileName+".csv"),';',' ');
                String sql = getNextSqlQuery(i,idCargo);
                //Pedir una tabla en forma de ResultSet
                table = dbmg.getTable(sql);
                if(i==2){
                    requerimientosPostProcess(table,writer,dbmg,sql);
                    continue;
                }
                else if(i==3){
                    capacitacionesPostProcess(table,writer);
                    continue;
                }
                else if(i==6){
                    empleadosPostProcess(table,writer);
                    continue;
                }
                else if(i==7){
                    vacacionesPostProcess(table,writer);
                    continue;
                }
                else{
                    //Consulta SQL ejecutada con exito
                    try {
                        writer.writeAll(table, true);
                    } catch (SQLException e) {
                        System.out.println("Algo malo paso al tratar de guardar datos en archivo");
                        e.printStackTrace();
                    }
                    writer.close();
                    try {
                        table.close();
                    } catch (SQLException e) {
                        e.printStackTrace();                   
                    }        

            }
        }
    }
    }

    private String getNextFileName(int i) {
        //Retorna el nombre para el archivo a generar
        switch(i){
        case 0:
            return "RequerimientosSkills";
        case 1:
            return "Turnos";
        case 2:
            return "Requerimientos";
        case 3:
            return "capacitaciones";
        case 4:
            return "DiasYTurnosProhibidos";
        case 5:
            return "DatosSkills";
        case 6:
            return "Empleados";
        case 7:
            return "Vacaciones";
        }           
        return "Cargos";
    }

    private String getNextSqlQuery(int i,String cargoId) {
       /*Retorna un Query adecuado para retornar la informacion necesaria 
        * para generar el archivo i
        * */
        switch(i){
        case 0:
            return "SELECT idskill,extract(day from fecha),idturno,requerimiento " + 
            "FROM demandaSkills " + 
            "WHERE idcargo='"+cargoId+"'";
        case 1:
            return "SELECT idTurno,nombreTurno FROM turnos";
        case 2:
            return "SELECT extract(day from fecha) dia, idturno turno ,requerimiento demanda "+
            "FROM capacity "+
            "WHERE idcargo='"+cargoId+"' "+
            "ORDER BY dia ASC, turno ASC ";
        case 3:
            return "SELECT empleados.rut,capacitaciones.fechaInicio,capacitaciones.fechatermino, turnos.tipoturno " + 
            "FROM capacitaciones,turnos, empleados " + 
            "WHERE capacitaciones.rut = empleados.rut AND empleados.idCargo = '"+cargoId+"' AND   " + 
            "cast(substr(capacitaciones.horainicio,0 ,2) AS int) >=  cast(substr(turnos.horaingreso,0,2) as int) AND  " + 
            "cast(substr(capacitaciones.horatermino,0 ,2) AS int) <= cast(substr(turnos.horasalida,0,2) as int)";
        case 4:
            return "SELECT datoshistoricos.rut, extract(day from dia), restriccionasignacion.idturno  " + 
            "FROM restriccionAsignacion, datosHistoricos,empleados " + 
            "WHERE empleados.rut = datoshistoricos.rut AND empleados.idCargo = '"+cargoId+"' AND " + 
            "datoshistoricos.idProhibido = restriccionasignacion.idProhibido AND " + 
            "restriccionAsignacion.idProhibido != 0";
        case 5:
            return "SELECT empleados.rut,skills.idskill " + 
            "FROM empleados, empleadosxSkill, skills " + 
            "WHERE empleados.idcargo='"+cargoId+"' AND empleadosxskill.rut=empleados.rut " + 
            " AND skills.idskill=empleadosxskill.idskill";
        case 6:
            return "SELECT datoshistoricos.rut,datosempleado.diastrabajados,datosempleado.diasdescansados, " + 
            "datosempleado.maxnoches,datosempleado.librespostsaliente,datosempleado.finesdesemanalibres " +   
            "FROM datosEmpleado,datosHistoricos, empleados " + 
            "WHERE empleados.rut = datoshistoricos.rut AND " + 
            " empleados.idcargo = '"+cargoId+"' AND datoshistoricos.iddatos =datosempleado.iddatos";
        case 7:
            return "SELECT empleados.rut,vacaciones.fechainicio,vacaciones.fechatermino " + 
            "FROM empleados, vacaciones  " + 
            "WHERE empleados.rut = vacaciones.rut AND  " + 
            "empleados.idCargo='"+cargoId+"'";
        }
        return null;
    }

    private void requerimientosPostProcess(ResultSet table, CSVWriter writer, DbManager dbmg,String sql ) {
        /*Ajusta los dias a la convencion de 42 dias por Rostering usado en el modelo:
         * Basicamente, se suma 6 a todos los dias de inicio. Luego se completa los 42 dias
         * copiando los dias que corresponderian en la semana , de los ultimos registros obtenidos
         * 
         */ 
        
        int rowDay =-1;
        try {
            while(table.next()){
            rowDay = table.getInt(1);
            rowDay=rowDay+6;
            String[] nextRow = {""+rowDay,
                                ""+table.getInt(2),
                                ""+table.getInt(3)};
            writer.writeNext(nextRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*Si luego de copiar el resultado, no se alcanzan los 42 dias del modelo,
         * se completa el input copiando los dias correspondiente con los resultados
         * de los mismos dias, pero de la ultima semana
         */
        if(rowDay<42){
            //Recupero la misma tabla anterior
            table=dbmg.getTable(sql);
            try{
                table.next();
                //Avanzo al dia desde el que voy a empezar a repetir
                while(table.getInt(1)<(rowDay-(7+6)))
                    table.next();
                //Repito datos hasta completar los 42 dias
                for(int k=rowDay;k<=42;k++){
                    int normalDay=k-(7+9);
                    while(normalDay==table.getInt(1)){
                        String[] nextRow ={""+k,
                                       table.getString(2),
                                       table.getString(3)};
                        writer.writeNext(nextRow);
                        table.next();
                        }
                    }
                writer.close();
                table.close();
            }
            catch(Exception e){
                e.printStackTrace();
                }

            }
        
    }

    private void vacacionesPostProcess(ResultSet table, CSVWriter writer) {
        /*Para cada resultado de la tabla (rut, fecha inicio, fecha termino 
         * Se escribe en el archivo 1 fila con el rut, dia (numero del mes real),
         * hasta el ultimo dia del mes actual
         **/
        try {
            while(table.next()){
                Calendar start = Calendar.getInstance();
                start.setTime(table.getDate(2));

                Calendar end = Calendar.getInstance();
                end.setTime(table.getDate(3));
                
                Calendar endMonth = Calendar.getInstance();
                endMonth.set(Calendar.MONTH, start.get(start.MONTH));
                endMonth.set(Calendar.DAY_OF_MONTH,start.getActualMaximum(Calendar.DAY_OF_MONTH));
                /*
                System.out.println("hola: "+start.MONTH+" "+start.getActualMaximum(Calendar.DAY_OF_MONTH));
                System.out.println(endMonth.getTimeInMillis());
*/
                SimpleDateFormat sdf = new SimpleDateFormat("EEE",Locale.ENGLISH);
                SimpleDateFormat dia = new SimpleDateFormat("dd");
                int dayOfWeek,habil;
                String dayWeek;
                while( !start.after(end) && !start.after(endMonth)){
                    Date targetDay = start.getTime();
                    // Do Work Here
                    dayWeek=sdf.format(start.getTime());
                    habil= -1;
                    if(dayWeek.equals("Mon") || dayWeek.equals("Tue") || dayWeek.equals("Wed")
                        || dayWeek.equals("Thu") || dayWeek.equals("Fri"))
                        habil=1;
                    else
                        habil=0;
                    int modelDay = Integer.parseInt(dia.format(start.getTime()));
                    modelDay = modelDay+7;
                    String[] nextRow = {table.getString(1),
                                        ""+modelDay,
                                        ""+habil};
                    writer.writeNext(nextRow);
                    start.add(Calendar.DATE, 1);
                }
            writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }

    private void capacitacionesPostProcess(ResultSet table, CSVWriter writer) {
        /*Para cada resultado de la tabla (rut, fecha inicio, fecha termino , turno)
         * Se escribe en el archivo 1 fila con el rut, dia (numero del mes real) y tipo de turno (A,T,N)
         * hasta el ultimo dia del mes actual
         * */
        try {
            while(table.next()){
                Calendar start = Calendar.getInstance();
                start.setTime(table.getDate(2));
                Calendar end = Calendar.getInstance();
                end.setTime(table.getDate(3));
                
                Calendar endMonth = Calendar.getInstance();
                endMonth.set(Calendar.MONTH, start.get(Calendar.MONTH));
                endMonth.set(Calendar.DAY_OF_MONTH,start.getActualMaximum(Calendar.DAY_OF_MONTH));
                
                SimpleDateFormat dia = new SimpleDateFormat("dd");
                int dayOfWeek,habil;
                String dayWeek;
                while( !start.after(end) && !start.after(endMonth)){
                    int modelDay = Integer.parseInt(dia.format(start.getTime()));
                    modelDay = modelDay+7;
                    String[] nextRow = {table.getString(1),
                                        ""+modelDay,
                                        table.getString(4)};
                    writer.writeNext(nextRow);
                    start.add(Calendar.DATE, 1);
                }
            }
        writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void empleadosPostProcess(ResultSet table, CSVWriter writer) {
        /*Se agrega, a los datos obtenidos de la base de datos, los 
         * parametros necesarios del modelo, que son calculados a partir 
         * del capacity por cargo
         */
        try {
            while(table.next()){
                    String[] nextRow = {table.getString(1),
                                        table.getString(2),
                                        table.getString(3),
                                        table.getString(4),
                                        table.getString(5),
                                        table.getString(6),
                                        "0","0","0","0","0","0",
                                        "0","0","0","0","0","0"};
                    writer.writeNext(nextRow);    
        }
        writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
