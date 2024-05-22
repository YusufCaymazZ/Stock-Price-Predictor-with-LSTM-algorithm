package com.Predictor.Settings;

import org.apache.poi.ss.usermodel.*;
import com.PredictorMysql.Util.DatabaseConnection;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class FileUploader {
	public FileUploader() {
		connection = DatabaseConnection.getConnection();
	}

	Connection connection = null;
	PreparedStatement sorguIfadesi = null;
	ResultSet getirilen = null;
	static String sql = "INSERT INTO files (kullanici_id, type, file_name) VALUES (?, ?, ?)";;

	@SuppressWarnings("null")
	public static int getUserId(Connection connection, String username, String password) {
		connection = DatabaseConnection.getConnection();
		String query = "SELECT kullanici_id FROM user WHERE usrname  = ? AND pswrd = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return resultSet.getInt("kullanici_id");
			} else {
				return -1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	public static ObservableList<FileIn> readCsv(String path) {
		ObservableList<FileIn> data = FXCollections.observableArrayList();
        String line = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            if ((line = br.readLine()) != null) {
                // İlk satırı oku ve atla, bu başlık satırıdır.
            }
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length > 5) {
                    String date = dateFormat.format(dateFormat.parse(fields[0])); // Tarih formatını ayarla
                    double close = Double.parseDouble(fields[1].replace("$", "").trim()); // $ işaretini kaldır
                    long volume = Long.parseLong(fields[2].replace("$", "").trim()); // $ işaretini kaldır
                    double open = Double.parseDouble(fields[3].replace("$", "").trim()); // $ işaretini kaldır
                    double high = Double.parseDouble(fields[4].replace("$", "").trim()); // $ işaretini kaldır
                    double low = Double.parseDouble(fields[5].replace("$", "").trim()); // $ işaretini kaldır

                    FileIn fileIn = new FileIn(date, close, volume, open, high, low);
                    data.add(fileIn);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


	/*public static void processExcelFile(File file, Connection connection, int kullaniciId, String fileName)
			throws IOException, SQLException {
		Workbook workbook = WorkbookFactory.create(file);
		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.rowIterator();

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row.getRowNum() == 0)
				continue; // skip header row

			Date date = row.getCell(0).getDateCellValue();
			int open = parseInteger(getCellValue(row.getCell(1)));
			int close = parseInteger(getCellValue(row.getCell(2)));
			int high = parseInteger(getCellValue(row.getCell(3)));
			int low = parseInteger(getCellValue(row.getCell(4)));
			int volume = parseInteger(getCellValue(row.getCell(5)));

			insertRecord(connection, kullaniciId, ".xlsx", fileName);
		}

		workbook.close();
	}*/

	public static String getCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			return String.valueOf(cell.getNumericCellValue());
		default:
			return "";
		}
	}

	public static int parseInteger(String value) {
		return Integer.parseInt(value.replaceAll("[^\\d]", ""));
	}

	public static Date parseDate(String value) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		return sdf.parse(value);
	}

	public static void insertRecord(Connection connection, int kullaniciId, String type, String fileName) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, kullaniciId);
		preparedStatement.setString(2, type);
		preparedStatement.setString(3, fileName);
		preparedStatement.executeUpdate();
	}

	public static String getFileExtension(File file) {
		String name = file.getName();
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return ""; // empty extension
		}
		return name.substring(lastIndexOf + 1);
	}

	public static String getFileNameWithoutExtension(File file) {
		String name = file.getName();
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return name; // No extension
		}
		return name.substring(0, lastIndexOf);
	}
}
