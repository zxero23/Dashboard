package org.ccpm.dashboard.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class Utilidad {

	private static final String[] UNIDADES = { "", "uno ", "dos ", "tres ", "cuatro ", "cinco ", "seis ", "siete ",
			"ocho ", "nueve " };
	private static final String[] DECENAS = { "diez ", "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis ",
			"diecisiete ", "dieciocho ", "diecinueve", "veinte ", "treinta ", "cuarenta ", "cincuenta ", "sesenta ",
			"setenta ", "ochenta ", "noventa " };
	private static final String[] CENTENAS = { "", "ciento ", "doscientos ", "trecientos ", "cuatrocientos ",
			"quinientos ", "seiscientos ", "setecientos ", "ochocientos ", "novecientos " };

	public static String convertir(String numero, boolean mayusculas) {
		String literal;
		String parte_decimal;
		numero = numero.replace(".", ",");

		if (numero.indexOf(",") == -1) {
			numero = numero + ",00";
		}
		if (Pattern.matches("\\d{1,9},\\d{1,2}", numero)) {
			String Num[] = numero.split(",");
			parte_decimal = "con " + Num[1] + "/100 .-";
			if (Integer.parseInt(Num[0]) == 0) {
				literal = "cero ";
			} else if (Integer.parseInt(Num[0]) > 999999) {
				literal = getMillones(Num[0]);
			} else if (Integer.parseInt(Num[0]) > 999) {
				literal = getMiles(Num[0]);
			} else if (Integer.parseInt(Num[0]) > 99) {
				literal = getCentenas(Num[0]);
			} else if (Integer.parseInt(Num[0]) > 9) {
				literal = getDecenas(Num[0]);
			} else {
				literal = getUnidades(Num[0]);
			}
			if (mayusculas) {
				return (literal + parte_decimal).toUpperCase();
			} else {
				return (literal + parte_decimal);
			}
		} else {
			return null;
		}
	}

	private static String getUnidades(String numero) {
		return UNIDADES[Integer.parseInt(numero.substring(numero.length() - 1))];
	}

	private static String getDecenas(String num) {
		int n = Integer.parseInt(num);
		if (n < 10) {
			return getUnidades(num);
		} else if (n > 19) {
			String u = getUnidades(num);
			if (u.equals("")) {
				return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8];
			} else {
				return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8] + "y " + u;
			}
		} else {
			return DECENAS[n - 10];
		}
	}

	private static String getCentenas(String num) {
		if (Integer.parseInt(num) > 99) {
			if (Integer.parseInt(num) == 100) {
				return " cien ";
			} else {
				return CENTENAS[Integer.parseInt(num.substring(0, 1))] + getDecenas(num.substring(1));
			}
		} else {
			return getDecenas(Integer.parseInt(num) + "");
		}
	}

	private static String getMiles(String numero) {
		String c = numero.substring(numero.length() - 3);
		String m = numero.substring(0, numero.length() - 3);
		String n;
		if (Integer.parseInt(m) > 0) {
			n = getCentenas(m);
			return n + "mil " + getCentenas(c);
		} else {
			return "" + getCentenas(c);
		}

	}

	private static String getMillones(String numero) {
		String miles = numero.substring(numero.length() - 6);
		String millon = numero.substring(0, numero.length() - 6);
		String n;
		if (millon.length() > 1) {
			n = getCentenas(millon) + "millones ";
		} else {
			n = getUnidades(millon) + "millon ";
		}
		return n + getMiles(miles);
	}

	public static String textoSinBlancos(String sTexto) {
		StringBuilder sCadenaSinBlancos = new StringBuilder("");
		StringTokenizer stTexto = new StringTokenizer(sTexto);
		while (stTexto.hasMoreElements()) {
			sCadenaSinBlancos.append(stTexto.nextElement());
		}
		return sCadenaSinBlancos.toString();
	}

	public static String dateString(Date fecha) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String resultado = sdf.format(fecha);
		return resultado;
	}

	public static String dateStringSQLQuery(Date fecha) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String resultado = sdf.format(fecha);
		return resultado;
	}

	public static String obtenerAnio(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String resultado = sdf.format(date);
		return resultado;
	}

	public static Date sumarRestarDiasFecha(Date fecha, int dias) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.DATE, dias);
		return calendar.getTime();
	}

	public static int getDiasHabiles(Calendar fechaInicial, Calendar fechaFinal) {
		int diffDays = 0;
		while (fechaInicial.before(fechaFinal) || fechaInicial.equals(fechaFinal)) {
			if (fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
					|| fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
				fechaInicial.add(Calendar.DATE, 1);
				diffDays++;
			}
		}
		return diffDays;
	}

	public static Calendar DateToCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static int getMaxElemento(double[][] dTable, int pN, int pM) {
		double maxValor = dTable[0][0];
		int i = 0, j = 0;
		for (int sn = 0; sn < pN; sn++) {
			for (int sm = 0; sm < pM; sm++) {
				if (dTable[sm][sn] > maxValor) {
					maxValor = dTable[sm][sn];
					i = sm;
					j = sn;
				}
			}
		}
		dTable[i][j] = 0.0;
		return j;
	}

	public static void CrearTablaDhondt(double[][] dTable, int pN, int pM) {
		String row = "";
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		for (int n = 0; n < pN; n++) {
			for (int m = 0; m < pM; m++) {
				row = row + df.format(dTable[m][n]) + ", ";
			}
			row = row + "\n";
		}
	}

	public static BigDecimal calcularPorcetaje(int div, int divid) {
		Long divisor = (long) div;
		Long dividendo = (long) divid;
		double div1 = (double) divisor;
		double div2 = (double) dividendo;
		Double res = (div2 / div1) * 100;
		BigDecimal aux = new BigDecimal("0");
		if (div1 != 0) {
			aux = new BigDecimal(String.valueOf(res)).setScale(2, RoundingMode.HALF_EVEN);
		}

		return aux;
	}

	public static String generateRandomPassword(int len)
    {
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
 
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
 
        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance
 
        for (int i = 0; i < len; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
 
        return sb.toString();
    }
}
