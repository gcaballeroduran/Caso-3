import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class Repetidor extends Thread{


	private static final int PUERTO = 3401; //Puerto del servidor
	private static final int PUERTO2 = 3400; //Puerto del repetidor
	private static final String SERVIDOR = "localhost";
	
	@Override
	public void run() {
		try {
			ServerSocket ss = null;
			boolean seguir = true;
			
			try {
				ss = new ServerSocket(PUERTO2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			while(seguir)
			{
				Socket socketCliente = ss.accept();
				KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
				generator.initialize(1024);
				KeyPair keypair = generator.generateKeyPair();
				PublicKey publica = keypair.getPublic();
				PrivateKey privada = keypair.getPrivate();
				
				new File("PublicaRepetidor").delete();

				// ASIMETRICO
				
				FileOutputStream archivo;
				ObjectOutputStream oos;
				archivo = new FileOutputStream("PublicaRepetidor");
				oos = new ObjectOutputStream(archivo);
				oos.writeObject(publica);
				
				archivo.close();
				oos.close();
				
				/// SIMETRICO
				
				new File("SimetricoRepetidor").delete();

				FileOutputStream archivoSimetrica;
				ObjectOutputStream ooss;
				archivoSimetrica = new FileOutputStream("SimetricoRepetidor");
				ooss = new ObjectOutputStream(archivoSimetrica);
				
				
				KeyGenerator keygen = KeyGenerator.getInstance("AES");
				keygen.init(128);
				SecretKey secretKey = keygen.generateKey();
				
			
				ooss.writeObject(secretKey);
				
				archivoSimetrica.close();
				ooss.close();
				
				try {
					PrintWriter escritorCliente = new PrintWriter(socketCliente.getOutputStream(), true);
					BufferedReader lectorCliente = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
					
					Socket socketServidor = null;
					PrintWriter escritorServidor = null;
					BufferedReader lectorServidor = null;
					
					try {
						socketServidor = new Socket(SERVIDOR, PUERTO);
						escritorServidor = new PrintWriter(socketServidor.getOutputStream(), true);
						lectorServidor = new BufferedReader(new InputStreamReader(socketServidor.getInputStream()));
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					
					String algoritmo = lectorCliente.readLine();
					escritorServidor.println(algoritmo);
					
					if(algoritmo.equals("Simetrico"))
					{
						String cliente = lectorCliente.readLine();
						escritorCliente.println("OK");
						
						String capsulaCliente = lectorCliente.readLine();
						byte[] AsimCliente = Encapsulamiento.Desencapsular(capsulaCliente);
						byte[] mensajeClienteByte = Simetrico.descifrar(secretKey, AsimCliente);
						String mensajeCliente = new String(mensajeClienteByte);
						
						FileInputStream input = new FileInputStream("SimetricoServidor");
						ObjectInputStream ois = new ObjectInputStream(input);
						SecretKey secretKeyServidor = null;
						while(input.available() > 0)
						{
							secretKeyServidor = (SecretKey) ois.readObject();
						}
						input.close();
						ois.close();

						System.out.println(mensajeCliente);
						byte[] cifradoServidor = Simetrico.cifrar(secretKeyServidor, mensajeCliente);
						String capsulaServidor = Encapsulamiento.Encapsular(cifradoServidor);
						
						escritorServidor.println(capsulaServidor);
						
						String CapsulaServidor = lectorServidor.readLine();
						
						byte[] AsimDeServidor = Encapsulamiento.Desencapsular(CapsulaServidor);
						byte[] mensajeDeServidorBytes = Simetrico.descifrar(secretKey, AsimDeServidor);
						String mensajeDeServidor = new String(mensajeDeServidorBytes);
						
						
						FileInputStream input2 = new FileInputStream("SimetricoCliente");
						ObjectInputStream ois2 = new ObjectInputStream(input2);
						SecretKey secretKeyCliente = null;
						while(input2.available() > 0)
						{
							secretKeyCliente = (SecretKey) ois2.readObject();
						}
						input2.close();
						ois2.close();
						
						byte[] mensajeAClienteBytes = Simetrico.cifrar(secretKeyCliente, mensajeDeServidor);
						String CapsulaACliente = Encapsulamiento.Encapsular(mensajeAClienteBytes);
						escritorCliente.println(CapsulaACliente);
					}
					else {

						String cliente = lectorCliente.readLine();
						escritorCliente.println("OK");
						
						String capsulaCliente = lectorCliente.readLine();
						byte[] AsimCliente = Encapsulamiento.Desencapsular(capsulaCliente);
						byte[] mensajeClienteByte = Asimetrico.descifrar(privada, AsimCliente);
						String mensajeCliente = new String(mensajeClienteByte);
						
						FileInputStream input = new FileInputStream("PublicaServidor");
						ObjectInputStream ois = new ObjectInputStream(input);
						PublicKey publicaServidor = null;
						while(input.available() > 0)
						{
							publicaServidor = (PublicKey) ois.readObject();
						}
						input.close();
						ois.close();

						System.out.println(mensajeCliente);
						byte[] cifradoServidor = Asimetrico.cifrar(publicaServidor, mensajeCliente);
						String capsulaServidor = Encapsulamiento.Encapsular(cifradoServidor);
						
						escritorServidor.println(capsulaServidor);
						
						String CapsulaServidor = lectorServidor.readLine();
						
						byte[] AsimDeServidor = Encapsulamiento.Desencapsular(CapsulaServidor);
						byte[] mensajeDeServidorBytes = Asimetrico.descifrar(privada, AsimDeServidor);
						String mensajeDeServidor = new String(mensajeDeServidorBytes);
						
						FileInputStream input2 = new FileInputStream("PublicaCliente");
						ObjectInputStream ois2 = new ObjectInputStream(input2);
						PublicKey publicaCliente = null;
						while(input2.available() > 0)
						{
							publicaCliente = (PublicKey) ois2.readObject();
						}
						input2.close();
						ois2.close();
						
						byte[] mensajeAClienteBytes = Asimetrico.cifrar(publicaCliente, mensajeDeServidor);
						String CapsulaACliente = Encapsulamiento.Encapsular(mensajeAClienteBytes);
						escritorCliente.println(CapsulaACliente);
						
						
						/*
						String capsula = lectorCliente.readLine();
						byte[] capsula2 = Encapsulamiento.Desencapsular(capsula);
						
						
						FileInputStream input = new FileInputStream("Prueba");
						ObjectInputStream ois = new ObjectInputStream(input);
						PublicKey publicaC = null;
						while(input.available() > 0)
						{
					        publicaC = (PublicKey) ois.readObject();
						}
						input.close();
						ois.close();
				        
				        byte[] texto = Asimetrico.descifrar(publicaC, capsula2);
				        
				        String descifrado = new String(texto);
				        
				        System.out.println("Me lleg� del usuario " + descifrado);
				        */
					}
					
					escritorCliente.close();
					lectorCliente.close();
					socketCliente.close();
					escritorServidor.close();
					lectorServidor.close();
					socketServidor.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			} catch (Exception e) {
			e.printStackTrace();
			}
	}
}
