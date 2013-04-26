package view;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.StringTokenizer;


public class FileUploader {

	/*Clase para subir archivos de inputs, en formato .csv y 
	 * codificacion ANSI a una base de datos.
	 * Se asume que el formato de los archivos es el siguiente:
	 * Las filas que empiezan con un # son comentarios y seran ignoradas al momento
	 * de la insercion, las primeras 3 filas contienen, comentadas, los datos de la 
	 * tabla donde guardar la información, el nombre de sus campos, y el tipo de estos,
	 * se aceptan 3 tipos: STRING, INT, DATE.
	 * Ejemplo:
	 * #NOMBRE_TABLA
	 * #CAMPO1;CAMPO2;CAMPO3...
	 * #TIPO_CAMPO1;TIPO_CAMPO2;TIPO_CAMPO3...
	 * DATOS;DATOS;DATOS
	 * DATOS;DATOS;DATOS
	 * ...
	 * 
	 * */
	public FileUploader(){}

   @Deprecated
    public void UploadFileToDb(String filename,Connection con){
		FileReader fr = null;
		BufferedReader reader = null;
		StringTokenizer separator = null;
		try {
		fr = new FileReader(filename);			
		reader = new BufferedReader(fr);
		String actualLine;
		//Nombre de la tabla:
		String tableName = reader.readLine();
		tableName = tableName.substring(1);
		//Nombre de los campos:
		actualLine = reader.readLine();
		actualLine = actualLine.substring(1);
		separator = new StringTokenizer(actualLine,";");
		int columns = separator.countTokens();
		String[] colNames = new String[columns];
		for(int i=0;i<columns;i++){
			colNames[i] = separator.nextToken();
			
		}
		PreparedStatement SQL = prepareSQL(con,colNames,tableName);
		//Tipos de campos:
		actualLine = reader.readLine();
		actualLine = actualLine.substring(1);
		separator = new StringTokenizer(actualLine,";");
		String[] colTypes = new String[columns];
		for(int i=0;i<columns;i++){
			colTypes[i] = separator.nextToken();
		}

		
		/*
		System.out.println("nombre Tabla: "+tableName);
		for(int i=0; i<colNames.length;i++){
			System.out.print(colNames[i]+"\t");
		}
		System.out.println("");
		
		*/
		//Datos de la tabla
		String[] dataRow = null;		
		while ((actualLine = reader.readLine()) != null)   {
			//Saltarse Lineas de comentarios:
			if(actualLine.startsWith("#") || actualLine.startsWith(";"))
				continue;
			
			separator = new StringTokenizer(actualLine,";");
			dataRow = new String[columns];
			for(int i=0;i<columns;i++){
				dataRow[i] = separator.nextToken();
				//System.out.print(dataRow[i]+"\t");
			}
			//System.out.println("");
			
			saveRecord(SQL,colTypes,dataRow);
		}
		} catch (Exception e) {
		// TODO Auto-generated catch block
		System.out.println("Error al abrir el archivo de input");
		e.printStackTrace();
		}
		try {
			reader.close();
			fr.close();
			con.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
        
    public void uploadFileToDb(BufferedReader reader,Connection con){

            StringTokenizer separator = null;
            try {
            String actualLine;
            //Nombre de la tabla:
            String tableName = reader.readLine();
            tableName = tableName.substring(1);
            //Nombre de los campos:
            actualLine = reader.readLine();
            actualLine = actualLine.substring(1);
            separator = new StringTokenizer(actualLine,";");
            int columns = separator.countTokens();
            String[] colNames = new String[columns];
            for(int i=0;i<columns;i++){
                    colNames[i] = separator.nextToken();               
            }
            PreparedStatement SQL = prepareSQL(con,colNames,tableName);
            //Tipos de campos:
            actualLine = reader.readLine();
            actualLine = actualLine.substring(1);
            separator = new StringTokenizer(actualLine,";");
            String[] colTypes = new String[columns];
            for(int i=0;i<columns;i++){
                    colTypes[i] = separator.nextToken();
            }
            //Datos de la tabla
            String[] dataRow = null;                
            while ((actualLine = reader.readLine()) != null)   {
                    //Saltarse Lineas de comentarios y vacias:
                    if(actualLine.startsWith("#") || actualLine.startsWith(";"))
                            continue;
                    separator = new StringTokenizer(actualLine,";");
                    dataRow = new String[separator.countTokens()];
                    for(int i=0;i<dataRow.length;i++){
                        try{
                            dataRow[i] = separator.nextToken();
                        } 
                        catch(Exception e){
                            //NoSuchElementException => dato vacio
                            dataRow[i] = "";    
                        }
                    }
                    saveRecord(SQL,colTypes,dataRow);
                    //Agrego datos historicos por defecto
                    if(tableName.equals("Empleados"))
                        createHistoricRecord(dataRow,con);                     
            }
            } catch (Exception e) {
            System.out.println("Error al abrir el archivo de input");
            e.printStackTrace();
            }
            try {
                    reader.close();
                    con.close();
            } catch (Exception e) {
                    e.printStackTrace();
            }
            
            
            
    }

	private PreparedStatement prepareSQL(Connection con,String[] colNames, String tableName) {
		/*Genera la consulta SQL insert que traspasara los datos del archivo a una tabla
                 * */
		String insertSQL = String.format("insert into %s (", tableName);
		for(int i=0;i<colNames.length;i++){
			insertSQL = insertSQL+colNames[i]+",";
		}
		insertSQL = insertSQL.substring(0, insertSQL.length()-1)+")";
		insertSQL = insertSQL.concat(" values(");
		
		for(int i=0;i<colNames.length;i++){
			insertSQL = insertSQL+"?,";
		}
		
		insertSQL = insertSQL.substring(0, insertSQL.length()-1)+")";
		PreparedStatement st= null;
		try {
			st = con.prepareStatement(insertSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return st;
	}

	private void saveRecord(PreparedStatement SQL,String[] colTypes, String[] dataRow) {
		/*Tipos de entrada de inputs:
		 * String
		 * int
		 * Date
		 * 
		 * */
		
		for(int i=0;i<colTypes.length;i++)
			colTypes[i]=colTypes[i].toLowerCase();
		try {
			//Dependiendo del caso, indicamos que tipo de datos setear
			for(int i=0; i<colTypes.length;i++){
				if(colTypes[i].equals("int"))
					SQL.setInt(i+1, Integer.parseInt(dataRow[i]));
				else if(colTypes[i].equals("date")){
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                        java.util.Date aux=formatter.parse(dataRow[i]);
					java.sql.Date sqlDate = new java.sql.Date(aux.getTime());
					SQL.setDate(i+1, sqlDate);
				}
                                else{
			            SQL.setString(i+1, dataRow[i]);
                                }
			}
			SQL.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

    private void createHistoricRecord(String[] empData, Connection con) {
        /*Crea un registro historico por defecto, de acuerdo al tipo de contrato del empleado,
        * Que es el ultimo dato del archivo
        */
        PreparedStatement st;

        try {
            st = con.prepareStatement("insert into datosHistoricos values (?,?,?,?,?,?)");
            st.setString(1, empData[0]);
            int typeContract= Integer.parseInt(empData[4]);
            st.setInt(2, typeContract);
            st.setInt(3,0);
            st.setInt(4,0);
        
            java.util.Date today = new java.util.Date();             
            Calendar calendar = Calendar.getInstance();  
            calendar.setTime(today);  
            calendar.set(Calendar.DAY_OF_MONTH, 1);  
            java.util.Date firstDayOfMonth = calendar.getTime();  
            Date aDate = new Date(firstDayOfMonth.getTime());
            st.setDate(5, aDate);
            st.setInt(6, 1);
            
            st.executeUpdate();
            st.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }
}

