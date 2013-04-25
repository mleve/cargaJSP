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
            DbManager dbmg = new DbManager("dev","dev","orcl");
            
            /*Aqui, en una ciclo o algo, se debe iterar para crear los
             * 12 archivos necesarios:
             * */
            for(int i =2; i<=7;i++){
                String fileName = getNextFileName(i);
                ResultSet table = null;
                CSVWriter writer = new CSVWriter(new FileWriter(folderName+"\\"+fileName+".csv"),';',' ');
                String sql = getNextSqlQuery(i,idCargo);
                //Pedir una tabla en forma de ResultSet
                table = dbmg.getTable(sql);
                if(i==2){
                    postProcess(table,writer,dbmg,sql);
                    continue;
                }
                else if(i==3){
                    postProcess3(table,writer);
                    continue;
                }
                else if(i==6){
                    postProcess6(table,writer);
                    continue;
                }
                else if(i==7){
                    postProcess7(table,writer);
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
        //Hacer un switch
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
        //Hacer un switch
        switch(i){
        case 0:
            /*
            SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yy");
            String actualDate=sp.format(Calendar.getInstance().getTime());
            Calendar.getInstance().getTime();     
            String minDate="01"+actualDate.substring(2);
            int maxDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
            String maxDate=""+maxDay+actualDate.substring(2);
            return "SELECT skills_id_skill, extract(day from fecha) \"dia\",turnos_idturno, requerimiento FROM demandaskills" +
            " WHERE cargos_='"+cargoId+"' AND fecha >= '"+minDate+"' AND fecha <= '"+maxDate+"' " +
            "ORDER BY \"dia\" ASC";
            */
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
        return "lol";
    }

    private void postProcess(ResultSet table, CSVWriter writer, DbManager dbmg,String sql ) {
        //Ajustar al dia de inicio y grabar
        int rowDay =-1;
        try {
            while(table.next()){
            rowDay = table.getInt(1);
            rowDay=rowDay+9;
            String[] nextRow = {""+rowDay,
                                ""+table.getInt(2),
                                ""+table.getInt(3)};
            writer.writeNext(nextRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Si no se alcanzan los 42 dias, copiar ...algo
        if(rowDay<42){
            //Recupero la misma tabla anterior
            table=dbmg.getTable(sql);
            try{
                table.next();
                //Avanzo al dia desde el que voy a empezar a repetir
                while(table.getInt(1)<(rowDay-(7+9)))
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
                e.printStackTrace();;
                }

            }
        
    }

    private void postProcess7(ResultSet table, CSVWriter writer) {
        //Preproceso de vacaciones
        try {
            while(table.next()){
                Calendar start = Calendar.getInstance();
                start.setTime(table.getDate(2));

                Calendar end = Calendar.getInstance();
                end.setTime(table.getDate(3));
                SimpleDateFormat sdf = new SimpleDateFormat("EEE",Locale.ENGLISH);
                SimpleDateFormat dia = new SimpleDateFormat("dd");
                int dayOfWeek,habil;
                String dayWeek;
                while( !start.after(end)){
                    Date targetDay = start.getTime();
                    // Do Work Here
                    dayWeek=sdf.format(start.getTime());
                    habil= -1;
                    if(dayWeek.equals("Mon") || dayWeek.equals("Tue") || dayWeek.equals("Wed")
                        || dayWeek.equals("Thu") || dayWeek.equals("Fri"))
                        habil=1;
                    else
                        habil=0;
                    String[] nextRow = {table.getString(1),
                                        dia.format(start.getTime()),
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

    private void postProcess3(ResultSet table, CSVWriter writer) {
        //Preproceso de vacaciones
        try {
            while(table.next()){
                Calendar start = Calendar.getInstance();
                start.setTime(table.getDate(2));

                Calendar end = Calendar.getInstance();
                end.setTime(table.getDate(3));
                //SimpleDateFormat sdf = new SimpleDateFormat("EEE",Locale.ENGLISH);
                SimpleDateFormat dia = new SimpleDateFormat("dd");
                int dayOfWeek,habil;
                String dayWeek;
                while( !start.after(end)){
                    Date targetDay = start.getTime();
                    // Do Work Here
                    String[] nextRow = {table.getString(1),
                                        dia.format(start.getTime()),
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

    private void postProcess6(ResultSet table, CSVWriter writer) {
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
