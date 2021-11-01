import java.io.DataOutputStream;
import java.io.IOException;

import javax.crypto.SecretKey;

// REFERENCIAS: https://www.programarya.com/Cursos-Avanzados/Java/Sockets
public class Cliente extends Principal{
	private String modo;
	
    public Cliente(String pmodo) throws IOException{

    	super("cliente");
    	modo = pmodo;
    	
    } 

    public void startClient() //Método para iniciar el cliente
    {
        try
        {
            //Flujo de datos hacia el servidor
            salidaServidor = new DataOutputStream(cs.getOutputStream());

            //Se enviarán dos mensajes
            for (int i = 0; i < 2; i++)
            {
                //Se escribe en el servidor usando su flujo de datos
                salidaServidor.writeUTF("Este es el mensaje número " + (i+1) + "\n");
            }

            cs.close();//Fin de la conexión

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}