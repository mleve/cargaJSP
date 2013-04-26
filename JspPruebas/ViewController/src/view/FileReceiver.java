
package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.sql.Connection;

import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileReceiver extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
    
        try {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    // Process form file field (input type="file").
                    
                    /*Se Genera un BufferedReader a partir de cada archivo subido,
                     *luego uploader lo guarda en la base de datos
                     * */
                    InputStream filecontent = item.getInputStream();
                    InputStreamReader fileAux = new InputStreamReader(filecontent);
                    if(fileAux.ready()){
                    BufferedReader bf = new BufferedReader(fileAux);
                    FileUploader uploader = new FileUploader();
                    Connection con = new DbManager().getDb("dev","dev","orcl");
                    uploader.uploadFileToDb(bf, con);
                    }
                }
            }
        } catch (Exception e) {
            throw new ServletException("Cannot parse multipart request.", e);
        }
        //System.out.println(request);
 
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML>" +
                    "<html>"  +
                    "<head>" +
                    " <title>Subida Exitosa</title> \n" + 
                    " <META HTTP-EQUIV=\"REFRESH\" CONTENT=\"0;URL=CargaArchivos.jsp\"> \n" + 
                    "<link href=\"resources/css/styles.css\" rel=\"stylesheet\" type=\"text/css\">\n" + 
                    "\n" + 
                    "<!--[if lt IE 9]>\n" + 
                    "<script src=\"http://html5shiv.googlecode.com/svn/trunk/html5.js\"></script>\n" + 
                    "<![endif]-->\n" + 
                    "\n" + 
                    "</head>\n" + 
                    "\n" + 
                    "<body onload='alert(\"Subida de Archivo exitosa, redirigiendo\");>\n" + 
                    "\n" + 
                    "<div class=\"container\">\n" + 
                    "  <header>\n" + 
                    "    <a href=\"menu.html\"><img src=\"resources/images/header.jpg\" alt=\"Andes Airport\" width=\"900\" height=\"90\" id=\"logo\" style=\"background: #42413C;; display:block;\" /></a>\n" + 
                    "  </header>\n" + 
                    "\n" + 
                    "<div  align=\"center\" class=\"div_header\" >\n" + 
                    "</div>\n" + 
                    "  <br clear=\"all\"/>\n" + 
                    "  \n" + 
                    "  <article class=\"content\">\n" + 
                    "    <h1>Sistema Rostering Andes</h1>    \n" + 
                    "    <section>\n" + 
                    "    </section>\n" + 
                    "    <!-- end .content --></article>\n" + 
                    "  <footer>\n" + 
                    "    <p>sistema rostering andes v1.0</p>\n" + 
                    "</footer>\n" + 
                    "  <!-- end .container --></div>\n" + 
                    "</body>\n" + 
                    "</html>");
        out.close();
    }
}