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
            for(int i =0; i<=3;i++){
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
            " WHERE cargos_id_cargo='"+cargoId+"' AND fecha >= '"+minDate+"' AND fecha <= '"+maxDate+"' " +
            "ORDER BY \"dia\" ASC";
            */
            return "SELECT skills_id_skill,extract(day from fecha),turnos_idturno,requerimiento " + 
            "FROM demandaSkills " + 
            "WHERE id_cargo='"+cargoId+"'";
        case 1:
            return "SELECT idTurno,nombreTurno FROM turnos";
        case 2:
            return "SELECT extract(day from fecha) dia,idturno turno ,requerimiento demanda " + 
            "FROM capacity " + 
            "WHERE id_cargo='"+cargoId+"'";
        case 3:
            return "SELECT datoshistoricos.empleados_rut,extract(day from capacitaciones.fecha), " + 
            " turnos.tipoturno " + 
            "FROM capacitaciones,datosHistoricos, turnos, empleados " + 
            "WHERE empleados.cargos_id= datoshistoricos.empleados_rut AND " +  
            "cast(substr(capacitaciones.horainicio,0 ,2) AS int) >=  cast(substr(turnos.horaingreso,0,2) as int) AND " + 
            "cast(substr(capacitaciones.horatermino,0 ,2) AS int) <= cast(substr(turnos.horasalida,0,2) as int)";
        case 4:
            return "SELECT datoshistoricos.empleados_rut, extract(day from dia), restriccionasignacion.idturno " + 
            "FROM restriccionAsignacion, datosHistoricos";
        case 5:
            return "SELECT empleados.rut,skills.id_skill " + 
            "FROM empleados, empleadosxSkill, skills " + 
            "WHERE empleados.cargos_id='"+cargoId+"' AND empleadosxskill.empleados_rut=empleados.rut " + 
            " AND skills.id_skill=skills.id_skill";
        case 6:
            return "SELECT datoshistoricos.empleados_rut,datosempleado.diastrabajados,datosempleado.diasdescansados " + 
            ",datosempleado.maxnoches,datosempleado.librespostsaliente,datosempleado.finesdesemanalibres, " + 
            " datosempleado.libresseguidos,datosempleado.promedioa,datosempleado.mina,datosempleado.maxa, " + 
            " datosempleado.promediot,datosempleado.mint,datosempleado.maxt,datosempleado.promedion, " + 
            " datosempleado.minn, datosempleado.maxn, datosempleado.maxquiebres,datosempleado.domingoslibres " + 
            "FROM datosEmpleado,datosHistoricos, empleados " + 
            "WHERE empleados.rut = datoshistoricos.empleados_rut AND " + 
            " empleados.cargos_id = '"+cargoId+"' AND datoshistoricos.id_datos =datosempleado.id_datos";
        case 7:
            return "SELECT empleados.rut,vacaciones.fechainicio,vacaciones.fechatermino " + 
            "FROM empleados, vacaciones " + 
            "WHERE empleados.rut = vacaciones.empleados_rut";
        }
    }
}
