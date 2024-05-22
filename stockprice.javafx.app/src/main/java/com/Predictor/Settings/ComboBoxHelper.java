package com.Predictor.Settings;

import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

//BU SINIF COMBO BOX İLE ITEM EKLEME VE ALMA İŞLEMLERİNİ HIZLICA YAPMAK İÇİN KULLANILACAK

public class ComboBoxHelper {
	public static void setComboBoxStringItems(ComboBox<String> comboBox, String... items) {
		ObservableList<String> itemList = FXCollections.observableArrayList(items);
		comboBox.setItems(itemList);
	}

	public static void setComboBoxIntegerItems(ComboBox<Integer> comboBox, Integer... items) {
		ObservableList<Integer> itemList = FXCollections.observableArrayList(items);
		comboBox.setItems(itemList);
	}
	
	public static <T> List<T> getComboBoxItems(ComboBox<T> comboBox) {
        return comboBox.getItems().stream().collect(Collectors.toList());
    }
}