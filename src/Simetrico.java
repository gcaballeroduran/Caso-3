import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class Simetrico {

private final static String PADDING = "AES/ECB/PKCS5Padding";

public static byte[] cifrar(SecretKey llave, String texto) {
	byte[] textoCifrado;

	long inicial = System.nanoTime();

	try {
		Cipher cifrador = Cipher.getInstance(PADDING);
		byte[] textoClaro = texto.getBytes();

		cifrador.init(Cipher.ENCRYPT_MODE, llave);
		textoCifrado = cifrador.doFinal(textoClaro);
		

		long fin = System.nanoTime();
		System.out.println(fin-inicial);

		return textoCifrado;

	} catch (Exception e) {
		System.out.println("Excepcion: " + e.getMessage());
		return null;
	}
}

public static byte [] descifrar(SecretKey llave, byte[] texto) {
	byte[] textoClaro;
	long inicial = System.nanoTime();
	try {
		Cipher cifrador = Cipher.getInstance(PADDING);
		cifrador.init(Cipher.DECRYPT_MODE, llave);
		textoClaro = cifrador.doFinal(texto);
		
	} catch (Exception e) {
		System.out.println("Excepcion: " + e.getMessage());
		return null;
	}
	long fin = System.nanoTime();
System.out.println(fin-inicial);
return textoClaro;
		
}
}