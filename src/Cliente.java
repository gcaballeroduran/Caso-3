import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class Cliente extends Thread{
	private static final int PUERTO = 3400; //Puerto del repetidor
	private static final String SERVIDOR = "localhost";
	private int id;
	private String algoritmo;
	private SecretKey llave;
	
	
	public Cliente(int pid, String palgoritmo)
	{
		id=pid;
		algoritmo=palgoritmo;
		this.ejecucion();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}
	public void ejecucion(){
		long startTime = System.nanoTime();
		Socket socket = null;
		PrintWriter escritor = null;
		BufferedReader lector = null;
		try {
			
			try {
			
				socket = new Socket(SERVIDOR, PUERTO);
				escritor = new PrintWriter(socket.getOutputStream(), true);
				lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			if(algoritmo.equals("Simetrico")){
				System.out.println("CIFRADO SIMETRICO");
				FileOutputStream archivo;
				ObjectOutputStream oos;
				KeyGenerator keygen = KeyGenerator.getInstance("AES");
				keygen.init(128);
				llave = keygen.generateKey();
				
				
				new File("SimetricoCliente").delete();
				
				archivo = new FileOutputStream("SimetricoCliente");
				oos = new ObjectOutputStream(archivo);
				oos.writeObject(llave);
				
				archivo.close();
				oos.close();
				
				escritor.println(algoritmo);
				escritor.println("CLIENTE_"+id);
				String OK = lector.readLine();
				
				byte[] cifradoRepetidor = Simetrico.cifrar(llave, "0"+id);
				System.out.println("Pedido: 0"+id);
				String capsulaRepetidor = Encapsulamiento.Encapsular(cifradoRepetidor);
				
				escritor.println(capsulaRepetidor);
				String mensajeDeRepetidor = lector.readLine();
				
				byte[] mensajeDeRepetidorBytes = Encapsulamiento.Desencapsular(mensajeDeRepetidor);
				byte[] descifrado = Simetrico.descifrar(llave, mensajeDeRepetidorBytes);
				String respuestaFinal = new String(descifrado);
				
				long endTime = System.nanoTime();
				
				// Calcular tiempo 
				System.out.println("Mensaje se envia al repetidor "+( endTime -startTime));
				
				System.out.println("Mensaje de respuesta: " + respuestaFinal);
			}
			else if(algoritmo.equals("Asimetrico")){
				System.out.println("CIFRADO ASIMETRICO");
				FileOutputStream archivo;
				ObjectOutputStream oos;
				KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
				generator.initialize(1024);
				KeyPair keypair = generator.generateKeyPair();
				PublicKey publica = keypair.getPublic();
				PrivateKey privada = keypair.getPrivate();
				
				new File("PublicaCliente").delete();
				
				archivo = new FileOutputStream("PublicaCliente");
				oos = new ObjectOutputStream(archivo);
				oos.writeObject(publica);
				
				archivo.close();
				oos.close();
				
				escritor.println(algoritmo);
				escritor.println("CLIENTE_"+id);
				String OK = lector.readLine();
				
				FileInputStream input = new FileInputStream("PublicaRepetidor");
				ObjectInputStream ois = new ObjectInputStream(input);
				PublicKey publicaRepetidor = null;
				while(input.available() > 0)
				{
			        publicaRepetidor = (PublicKey) ois.readObject();
				}
				input.close();
				ois.close();
				
				byte[] cifradoRepetidor = Asimetrico.cifrar(publicaRepetidor, "0"+id);
				System.out.println("Pedido: 0"+id);
				String capsulaRepetidor = Encapsulamiento.Encapsular(cifradoRepetidor);
				
				escritor.println(capsulaRepetidor);
				String mensajeDeRepetidor = lector.readLine();
				
				byte[] mensajeDeRepetidorBytes = Encapsulamiento.Desencapsular(mensajeDeRepetidor);
				byte[] descifrado = Asimetrico.descifrar(privada, mensajeDeRepetidorBytes);
				String respuestaFinal = new String(descifrado);
				
				long endTime = System.nanoTime();
				// Calcular tiempo 
				System.out.println("Mensaje se envia al repetidor "+( endTime -startTime)+ " nanosegundos");
				
				System.out.println("Mensaje de respuesta: " + respuestaFinal);
				/*
				*/
			}
			else {
				System.out.println("Algoritmo no reconocido");
				System.exit(0);
			}
			
			escritor.close();
			lector.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
}
}