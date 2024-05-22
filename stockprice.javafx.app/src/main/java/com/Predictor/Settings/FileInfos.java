package com.Predictor.Settings;

public class FileInfos {
	public int file_id;
	public String file_name;
	public String file_type;

	public FileInfos(int file_id, String file_name, String file_type) {
		this.file_id = file_id;
		this.file_name = file_name;
		this.file_type = file_type;
	}

	public int getFile_id() {
		return file_id;
	}

	public void setFile_id(int file_id) {
		this.file_id = file_id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	
	}

