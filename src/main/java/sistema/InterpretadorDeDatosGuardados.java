package sistema;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import enumerados.Estado;
import enumerados.Periodo;
import excepciones.ArchivoException;
import excepciones.FormatoDeLineaException;
import excepciones.MateriaInvalidaException;
import listado.Listado;
import listado.Materia;
import util.LangResource;

public class InterpretadorDeDatosGuardados implements InterpretadorDeArchivos {

	private int numeroDeLinea;

	@Override
	public Listado generarListado(String ruta) throws ArchivoException {
		Listado.obtenerListado();
		numeroDeLinea = 0;
		try (var buffer = new BufferedReader(
				new InputStreamReader(new FileInputStream(ruta), StandardCharsets.UTF_8))) {
			agregarMaterias(buffer);
			obtenerCorrelativas(buffer);
		} catch (FileNotFoundException e) {
			throw new ArchivoException(LangResource.getString("ArchivoNoEncontrado") + ruta);
		} catch (IOException e) {
			Listado.borrarListado();
			throw new ArchivoException(e.getMessage(), e.getCause());
		}
		return Listado.obtenerListado();
	}

	private String validarLinea(String lineaActual, String mensaje) throws FormatoDeLineaException {
		if (lineaActual == null || lineaActual.strip().isEmpty()) {
			throw new FormatoDeLineaException(mensaje);
		}
		return lineaActual.strip();
	}

	private void agregarMaterias(BufferedReader buffer) throws IOException, ArchivoException {
		var cantidadDeMaterias = obtenerCantidadDeMaterias(buffer, LangResource.getString("NumeroEsperadoLinea"));
		var listado = Listado.obtenerListado();
		for (var i = 0; i < cantidadDeMaterias; i++) {
			try {
				listado.agregarMateria(crearMateria(buffer));
			} catch (FormatoDeLineaException e) {
				System.err.println("Linea " + numeroDeLinea + ", " + e.getMessage());
			}
		}
	}

	private int obtenerCantidadDeMaterias(BufferedReader buffer, String alerta) throws IOException, ArchivoException {
		try {
			numeroDeLinea++;
			return Integer.parseInt(validarLinea(buffer.readLine(), alerta));
		} catch (NumberFormatException e) {
			throw new FormatoDeLineaException(alerta);
		}
	}

	private Materia crearMateria(BufferedReader buffer) throws IOException, ArchivoException {
		numeroDeLinea++;
		var linea = validarLinea(buffer.readLine(), LangResource.getString("DatosMateriaEsperados"));
		if (!linea.matches("^\\d+/[A-Za-zÀ-ÿ,. ]+/\\d/([1-2A-Z][a-z]+[ ]+){0,1}[A-Z][a-z]+/\\d{1,2}/[A-Za-z ]{0,}$")) {
			throw new FormatoDeLineaException(formatearMensajeExcepcion(LangResource.getString("FormatoInvalido")));
		}
		String[] datos = linea.split("/");
		var numeroDeMateria = obtenerNumero(datos[0]);
		var nombre = datos[1];
		var anio = obtenerNumero(datos[2]);
		var periodo = obtenerPeriodo(datos[3]);
		var materia = new Materia(numeroDeMateria, nombre, anio, periodo);
		materia.setCalificacion(obtenerNumero(datos[4]));
		materia.setEstado(obtenerEstadoDeMateria(datos[5]));
		return materia;
	}

	private Periodo obtenerPeriodo(String dato) {
		dato = dato.replaceAll("[ ]+", " ");
		if (dato.equals(LangResource.getString("PeriodoPrimerCuatrimestre")))
			return Periodo.PRIMER_CUATRIMESTRE;

		if (dato.equals(LangResource.getString("PeriodoSegundoCuatrimestre")))
			return Periodo.SEGUNDO_CUATRIMESTRE;

		return Periodo.ANUAL;
	}

	private Estado obtenerEstadoDeMateria(String dato) {
		if (dato.equals(LangResource.getString("EstadoAprobado")))
			return Estado.APROBADA;

		if (dato.equals(LangResource.getString("EstadoRegularizado")))
			return Estado.REGULARIZADA;

		return Estado.NO_CURSADA;
	}

	private int obtenerNumero(String dato) {
		return Integer.parseInt(dato);
	}

	private void obtenerCorrelativas(BufferedReader buffer) throws IOException {
		numeroDeLinea++;
		var linea = buffer.readLine();
		if (linea != null && linea.strip().matches("\\d+")) {
			var cantidad = Integer.parseInt(linea);
			for (var i = 0; i < cantidad; i++) {
				agregarCorrelativa(buffer);
			}
		}
	}

	private void agregarCorrelativa(BufferedReader buffer) throws IOException {
		numeroDeLinea++;
		var linea = buffer.readLine();
		var listado = Listado.obtenerListado();
		if (linea != null && linea.strip().matches("^\\d+: *[\\d+/]+ *$")) {
			String[] datos = linea.split(": *");
			try {
				String[] correlativas = datos[1].split("/");
				var numeros = new int[correlativas.length];
				for (var i = 0; i < numeros.length; i++) {
					numeros[i] = Integer.parseInt(correlativas[i].strip());
				}
				listado.agregarCorrelativas(Integer.parseInt(datos[0]), numeros);
			} catch (MateriaInvalidaException e) {
				System.err.println(e.getMessage());
			}
		} else
			System.err.println("Linea " + numeroDeLinea + ", se esperaban los numeros de materias correlativas");
	}

	private String formatearMensajeExcepcion(String mensaje) {
		return String.format(LangResource.getString("LineaInvalida"), this.numeroDeLinea, mensaje);
	}
}
