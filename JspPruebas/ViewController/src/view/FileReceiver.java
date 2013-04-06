
package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

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
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>FileReceiver</title></head>");
        out.println("<body>");
        out.println("<p>The servlet has received a GET. This is the reply.</p>");
        out.println("</body></html>");
        out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        
        try {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                    String fieldname = item.getFieldName();
                    String fieldvalue = item.getString();
                    // ... (do your job here)
                } else {
                    // Process form file field (input type="file").
                    String fieldname = item.getFieldName();
                    //String filename = FilenameUtils.getName(item.getName());
                    InputStream filecontent = item.getInputStream();
                    
                    // ... (do your job here)
                    /*
                    BufferedReader bf = new BufferedReader(InputStream);
                    String line = bf.readLine();
                    */
                    System.out.println(fieldname);
                }
            }
        } catch (Exception e) {
            throw new ServletException("Cannot parse multipart request.", e);
        }
        //System.out.println(request);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>FileReceiver</title></head>");
        out.println("<body>");
        out.println("<p>The servlet has received a POST. This is the reply.</p>");
        out.println("</body></html>");
        out.close();
    }
}
