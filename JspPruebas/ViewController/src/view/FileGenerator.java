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
            for(int i =0; i<=3;i++){
                String fileName = getNextFileName(i);
                ResultSet table = null;
                CSVWriter writer = new CSVWriter(new FileWriter(folderName+"\\"+fileName+".csv"),';',' ');
                String sql = getNextSqlQuery(i,idCargo);
                //Pedir una tabla en forma de ResultSet
                table = dbmg.getTable(sql);
                if(table != null){
                    //Consulta SQL ejecutada con exito
                    try {
                        writer.writeAll(table, true);
                    } catch (SQLException e) {
                        System.out.println("Algo malo paso al tratar de guardar datos en archivo");
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
            return "requerimientosSkills";
        case 1:
            return "turnos";
        case 2:
            return "requerimientos";
        case 3:
            return "capacitaciones";
        case 4:
            return "diasYTurnosProhibidos";
        }
                   
        return "Cargos";
    }

    private String getNextSqlQuery(int i,String cargoId) {
        //Hacer un switch
        switch(i){
        case 0:
            SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yy");
            String actualDate=sp.format(Calendar.getInstance().getTime());
            Calendar.getInstance().getTime();     
            String minDate="01"+actualDate.substring(2);
            int maxDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
            String maxDate=""+maxDay+actualDate.substring(2);
            return "SELECT skills_id_skill, extract(day from fecha) \"dia\",turnos_idturno, requerimiento FROM demandaskills" +
            " WHERE cargos_id_cargo='"+cargoId+"' AND fecha >= '"+minDate+"' AND fecha <= '"+maxDate+"' " +
            "ORDER BY \"dia\" ASC";
        case 1:
            return "SELECT idTurno,nombreTurno FROM turnos";
        case 2:
            return "SELECT  extract(day from fecha) \"dia\", turnos_idTurno, requerimiento FROM capacity " + 
            "WHERE cargos_id_cargo='"+cargoId+"' ORDER BY \"dia\" ASC";
        case 3:
            return "SELECT capacitaciones.empleados_rut rut,extract(day from fechainicio) dia,turnos.idturno turno " + 
            " FROM capacitaciones,turnos, empleados " + 
            "WHERE cast(substr(capacitaciones.horainicio,0 ,2) AS int) >=  " + 
            "      cast(substr(turnos.horaingreso,0,2) as int) " + 
            "AND cast(substr(capacitaciones.horatermino,0 ,2) AS int) <=  " + 
            "    cast(substr(turnos.horasalida,0,2) as int) " + 
            "AND capacitaciones.empleados_rut = empleados.rut " + 
            "AND empleados.cargos_id = '"+cargoId+"' ";
        case 4:
            return "SELECT turnosprohibidos.empleados_rut, turnosprohibidos.diainicio, turnosprohibidos.diatermino " + 
            "FROM turnosprohibidos, empleados " + 
            "WHERE turnosprohibidos.empleados_rut=empleados.rut AND " + 
            "empleados.cargos_id='"+cargoId+"' ";
        }
        return "SELECT * FROM CARGOS";
    }
}
