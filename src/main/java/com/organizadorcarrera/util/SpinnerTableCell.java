package com.organizadorcarrera.util;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class SpinnerTableCell<S, T> extends TableCell<S, T> { // NOSONAR

	private final ObjectProperty<StringConverter<T>> converter = new SimpleObjectProperty<>(this, "converter");

	@SafeVarargs
	public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(final T... items) {
		return forTableColumn(null, items);
	}

	@SafeVarargs
	public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(final StringConverter<T> converter, final T... items) {
		return forTableColumn(converter, FXCollections.observableArrayList(items));
	}

	public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(final ObservableList<T> items) {
		return forTableColumn(null, items);
	}

	public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(final StringConverter<T> converter, final ObservableList<T> items) {
		return list -> new SpinnerTableCell<>(converter, items);
	}

	private final ObservableList<T> items;

	private Spinner<T> spinner;

	public SpinnerTableCell(StringConverter<T> converter, ObservableList<T> items) {
		this.items = items;
		setConverter(converter != null ? converter : defaultStringConverter());
		setEditable(true);
	}

	@SuppressWarnings("unchecked")
	private StringConverter<T> defaultStringConverter() {
		return new StringConverter<>() {
			@Override
			public String toString(Object t) {
				return t == null ? null : t.toString();
			}

			@Override
			public T fromString(String string) {
				return (T) string;
			}
		};
	}

	public final ObjectProperty<StringConverter<T>> converterProperty() {
		return converter;
	}

	public final void setConverter(StringConverter<T> value) {
		converterProperty().set(value);
	}

	public final StringConverter<T> getConverter() {
		return converterProperty().get();
	}

	@Override
	public void startEdit() {
		if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
			return;
		}

		if (spinner == null) {
			createSpinner(converterProperty());
		}

		if (!isEmpty()) {
			super.startEdit();

			spinner.getValueFactory().setValue(getValue());

			setOnKeyPressed(event -> {
				if (event.getCode() == KeyCode.ENTER)
					Platform.runLater(() -> commitEdit(spinner.getValue()));
			});

			setText(null);
			setGraphic(spinner);
		}
	}

	private T getValue() {
		T item = getItem();
		return this.items.contains(item) ? item : this.items.get(0);
	}

	private void createSpinner(ObjectProperty<StringConverter<T>> converter) {
		spinner = new Spinner<>();
		spinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(items));
		spinner.setMaxWidth(Double.MAX_VALUE);
		spinner.getValueFactory().converterProperty().bind(converter);
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setText(getConverter().toString(getItem()));
		setGraphic(null);
	}

	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			if (isEditing()) {
				setText(null);
				setGraphic(spinner);
			} else {
				setText(getItemText(getConverter()));
				setGraphic(null);
			}
		}
	}

	private String getItemText(StringConverter<T> converter) {
		return converter == null ? getItemString() : converter.toString(getItem());
	}

	private String getItemString() {
		return getItem() == null ? "" : getItem().toString();
	}

}
