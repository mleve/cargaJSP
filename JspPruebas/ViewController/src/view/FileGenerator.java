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
            
            /*Aqui, en una ciclo o algo, se debe iterar para crear los
             * 12 archivos necesarios:
             * */
            for(int i =5; i<=6;i++){
                String fileName = getNextFileName(i);
                ResultSet table = null;
                CSVWriter writer = new CSVWriter(new FileWriter(folderName+"\\"+fileName+".csv"),';',' ');
                String sql = getNextSqlQuery(i,idCargo);
                //Pedir una tabla en forma de ResultSet
                table = dbmg.getTable(sql);
                if(i==7 && table!=null){
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
                                dayWeek=sdf.format(start);
                                habil= -1;
                                if(dayWeek.equals("Mon") || dayWeek.equals("Tue") || dayWeek.equals("Wed")
                                    || dayWeek.equals("Thu") || dayWeek.equals("Fri"))
                                    habil=1;
                                else
                                    habil=0;
                                String[] nextRow = {table.getString(1),
                                                    dia.format(start),
                                                    ""+habil};
                                writer.writeNext(nextRow);
                                start.add(Calendar.DATE, 1);
                            }

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                else if(table!=null && i==2){
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
                else if(table != null){
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
        /*
        out.println("<html>");
        out.println("<head><title>FileGenerator</title></head>");
        out.println("<body>");
        out.println("<p>The servlet has received a GET. This is the reply.</p>");
        out.println("</body></html>");
        out.close();

        */
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
            return "SELECT datoshistoricos.rut,extract(day from capacitaciones.fecha), " + 
            " turnos.tipoturno " + 
            "FROM capacitaciones,datosHistoricos, turnos, empleados " + 
            "WHERE empleados.cargos_id= datoshistoricos.empleados_rut AND " +  
            "cast(substr(capacitaciones.horainicio,0 ,2) AS int) >=  cast(substr(turnos.horaingreso,0,2) as int) AND " + 
            "cast(substr(capacitaciones.horatermino,0 ,2) AS int) <= cast(substr(turnos.horasalida,0,2) as int)";
        case 4:
            return "SELECT datoshistoricos.rut, extract(day from dia), restriccionasignacion.idturno " + 
            "FROM restriccionAsignacion, datosHistoricos";
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
            "FROM empleados, vacaciones " + 
            "WHERE empleados.rut = vacaciones.empleados_rut";
        }
        return "lol";
    }
}
