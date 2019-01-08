package com.hust.model.data;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataSeries {

	public Map<String, ObservableList<Number>> series = new HashMap<>();

	public void registerSeries(String name) {
		series.put(name, FXCollections.<Number>observableArrayList());
	}

	public ObservableList<Number> getSeries(String name) {
		return series.get(name);
	}
}
