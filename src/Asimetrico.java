import java.security.Key;

import javax.crypto.Cipher;

public class Asimetrico {

	private final static String ALGORITMO = "RSA";
	
	public static byte[] cifrar(Key llave, String texto) {
		byte[] textoCifrado;
			
		try {
			Cipher cifrador = Cipher.getInstance(ALGORITMO);
			byte[] textoClaro = texto.getBytes();
			
			cifrador.init(Cipher.ENCRYPT_MODE, llave);
			textoCifrado = cifrador.doFinal(textoClaro);
			
			return textoCifrado;
		} catch (Exception e) {
			System.out.println("Excepcion: " + e.getMessage());
			return null;
		}
	}
	
	public static byte[] descifrar(Key llave, byte[] texto) {
		byte[] textoClaro;
			
		try {
			Cipher cifrador = Cipher.getInstance(ALGORITMO);
			cifrador.init(Cipher.DECRYPT_MODE, llave);
			textoClaro = cifrador.doFinal(texto);
		} catch (Exception e) {
			System.out.println("Excepcion: " + e.getMessage());
			return null;
		}
		return textoClaro;
	}
	
	
}
