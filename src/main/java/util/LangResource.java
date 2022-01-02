package util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LangResource {

	private static LangResource instancia;
	private ResourceBundle resourceBundle;

	private LangResource() {
		obtenerBundle();
	}

	private void obtenerBundle() {
		try {
			this.resourceBundle = ResourceBundle.getBundle("lang.string", Locale.getDefault());
		} catch (MissingResourceException e) {
			this.resourceBundle = ResourceBundle.getBundle("lang.string", new Locale("en"));
		}
	}

	public static String getString(String clave) {
		checkearInstancia();
		return instancia.resourceBundle.getString(clave);
	}

	public static ResourceBundle getResourceBundle() {
		checkearInstancia();
		return instancia.resourceBundle;
	}

	private static void checkearInstancia() {
		if (instancia == null)
			instancia = new LangResource();
	}
}
