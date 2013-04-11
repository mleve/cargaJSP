package view;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.ResultSet;

import java.sql.SQLException;

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
        String cargoName = null;
        
        try{
            cargoName = (String)request.getParameter("cargo");
        }
        catch(Exception e){
            cargoName=null;
        }
        if(cargoName != null){
        //Se llamo a este servlet pasando el argumento necesario
            //Crear la nueva carpeta
            String folderName="C:\\"+cargoName;
            new File(folderName).mkdir();
            //Carga de clases auxiliares:
            DbManager dbmg = new DbManager("dev","dev","xe");
            
            /*Aqui, en una ciclo o algo, se debe iterar para crear los
             * 12 archivos necesarios:
             * */
            for(int i =0; i<1;i++){
                String fileName = getNextFileName(i);
                ResultSet table = null;
                CSVWriter writer = new CSVWriter(new FileWriter(folderName+"\\"+fileName+".csv"),';',' ');
                String sql = getNextSqlQuery(i);
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
        return "Cargos";
    }

    private String getNextSqlQuery(int i) {
        //Hacer un switch
        return "SELECT * FROM CARGOS";
    }
}
