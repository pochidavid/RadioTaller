package lourdes8122.radiotaller.repository;

import android.os.StrictMode;
import android.widget.Toast;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import lourdes8122.radiotaller.MainActivity;


public class EnviarMail {

    // Propiedades del cliente de correo
    private static Session session;         // Sesion de correo
    private static Properties properties;   // Propiedades de la sesion
    private static Transport transport;     // Envio del correo
    private static MimeMessage mensaje;     // Mensaje que enviaremos

    // Credenciales de usuario
    private static String direccionCorreo = "radiotallerapp@gmail.com";   // Dirección de correo
    private static String contrasenyaCorreo = "Lourdes1858";                 // Contraseña

    // Correo al que enviaremos el mensaje
    private static String destintatarioCorreo = "pochidavid@gmail.com";

    public static void main() throws MessagingException {
                //enviarMensaje("Hola Dionis","Prueba cliente correo, buen fin de semana. Sergi Barola");
    }

    public static void enviarMensaje(String subject, String content) throws MessagingException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Ajustamos primero las properties
        properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.googlemail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        //Configuramos la sesión

        try {
            session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(direccionCorreo, contrasenyaCorreo);
                }
            });

            if(session!=null){
                javax.mail.Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(direccionCorreo));
                message.setSubject(subject);
                message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(destintatarioCorreo));
                message.setContent(content,"text/html; charset=utf-8");
                Transport.send(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        /*
        // Configuramos los valores de nuestro mensaje
        mensaje = new MimeMessage(session);
        mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(destintatarioCorreo));
        mensaje.setSubject(subject);
        mensaje.setContent(content, "text/html; charset=utf-8");

        // Configuramos como sera el envio del correo
        transport = session.getTransport("smtp");
        transport.connect("smtp.gmail.com", direccionCorreo, contrasenyaCorreo);
        transport.sendMessage(mensaje, mensaje.getAllRecipients());
        transport.close();

        */
        // Mostramos que el mensaje se ha enviado correctamente
        System.out.println(content);
        System.out.println("--------------------------");
        System.out.println("Mensaje enviado");
        System.out.println("---------------------------");
    }
}
