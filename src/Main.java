import java.io.FileNotFoundException;
import java.util.Scanner;
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
	private final static String ALGORITMO2 = "AES";
	private final static String ALGORITMO = "RSA";
	private final static String ARCHIVO1 = "simetrica";
	private final static String ARCHIVO2 = "publica";
	private final static String ARCHIVO3 = "privada";
	private static Scanner sc;
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException{
/*
		KeyGenerator keygen = KeyGenerator.getInstance(ALGORITMO2);
		keygen.init(128);
		SecretKey secretKey = keygen.generateKey();
		
		KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITMO);
		generator.initialize(1024);
		KeyPair keypair = generator.generateKeyPair();
		PublicKey publica = keypair.getPublic();
		PrivateKey privada = keypair.getPrivate();
*/
		/*
		FileOutputStream archivo;
		ObjectOutputStream oos;
		archivo = new FileOutputStream(ARCHIVO1);
		oos = new ObjectOutputStream(archivo);
		oos.writeObject(secretKey);
		
		archivo = new FileOutputStream(ARCHIVO2);
		oos = new ObjectOutputStream(archivo);
		oos.writeObject(publica);
		*/
//		
//		sc = new Scanner(System.in);
//		System.out.print("Ingrese el modo (Asimetrico o Simetrico): ");
//		String modo = sc.nextLine();
//		if(modo.equals("Asimetrico") || modo.equals("Simetrico")) {
//			System.out.println("Modo no encontrado");
//		}
		new Servidor().start();
		new Repetidor().start();
		
		for(int i=0 ; i<10 ; i++)
		{
			double proba = Math.random();
			String algo = "";
			if(proba < 0.5)
			{
				algo="Simetrico";
			}
			else {
				algo="Asimetrico";
			}
			new Cliente(i,"Asimetrico"); //Por ahora todos asimetricos CAMBIAR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		}
	}
}
