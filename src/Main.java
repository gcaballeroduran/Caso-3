import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Main {
	private final static String PADDING = "AES/ECB/PKCS5Padding";
	private final static String ALGORITMO = "RSA";
	private final static String ARCHIVO1 = "simetrica";
	private final static String ARCHIVO2 = "publica";
	private final static String ARCHIVO3 = "privada";
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException{

		KeyGenerator keygen = KeyGenerator.getInstance(PADDING);
		//keygen.init(128);
		SecretKey secretKey = keygen.generateKey();
		
		KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITMO);
		generator.initialize(1024);
		KeyPair keypair = generator.generateKeyPair();
		PublicKey publica = keypair.getPublic();
		PrivateKey privada = keypair.getPrivate();
		
		FileOutputStream archivo;
		ObjectOutputStream oos;
		
		archivo = new FileOutputStream(ARCHIVO1);
		oos = new ObjectOutputStream(archivo);
		oos.writeObject(secretKey);
		
		archivo = new FileOutputStream(ARCHIVO2);
		oos = new ObjectOutputStream(archivo);
		oos.writeObject(publica);
		
		archivo = new FileOutputStream(ARCHIVO3);
		oos = new ObjectOutputStream(archivo);
		oos.writeObject(privada);
	}
}
