package com.PredictorMysql.Util;

/*
 * THIS IS MY DATABASE CONNECTION FOR MY PROJECT
 * I'VE CREATED THIS BECAUSE OF I DO NOT WANT TO WRITE CONNECTION 
 * CODES IN ALL OF CLASS.
 * 
 * 
 * 
 * DATABASE IS IN THE PROJECT PATH (stockpriceprediction.sql)
 * I DON'T KNOW HOW IS WORKS IN DIFFERENT PC BUT 
 * MY CONNECTION WHICH I USE IN MY LOCALHOST 
 * HAS WORKED AT DOWN.
 * 
 */
import java.sql.*;
import java.sql.DriverManager;

public class DatabaseConnection {
	static Connection databaseLink = null;
	public static Connection getConnection() {
		String databaseName = "pricepredictor";
		String databaseUser = "root";
		
		String url = "jdbc:mysql://localhost/" + databaseName;
		try {
			databaseLink = DriverManager.getConnection(url,databaseUser,"");
			return databaseLink;
		}catch(Exception e){
				//TODO: Handle Exception
				System.out.println(e.getMessage().toString());
				return null;
		}
		
	}
}


