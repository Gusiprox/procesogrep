package es.etg.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Main {

    public static final String COMANDO_A_USAR = "ls";
    public static final String FILTRO = "host*";
    public static final String DIRECTORIO = "//etc";

    public static final String MSG_CORRECTO = "La lista filtrada es: ";
    public static final String MSG_ERROR = "Se ha producido un error al ejecutar el comando";
    
    public static final String SALTO_DE_LINEA = "\n";

    public static final int SALIDA_SYSTEM = 34;

    public static void main(String[] args) {

        final String[] COMANDO_ENVIADO = {COMANDO_A_USAR, DIRECTORIO};
        String salidaEnviado[];

        final String[] COMANDO_GREP = {"grep", FILTRO};
        String salidaGrep[];

        final int SALIDA_NORMAL = 0; 
        final int SALIDA_FALLIDA = 1; 


        salidaEnviado = ejecCommand(COMANDO_ENVIADO, null);
        if (salidaEnviado.length != 1) {

            String[] datos = {MSG_ERROR, salidaEnviado[SALIDA_NORMAL], salidaEnviado[SALIDA_FALLIDA]};
            System.out.println(crearSalida(datos));

            System.exit(SALIDA_SYSTEM);
        }

        salidaGrep = ejecCommand(COMANDO_GREP, salidaEnviado);

        if (salidaEnviado.length != 1) {

            String[] datos = {MSG_ERROR, salidaGrep[SALIDA_NORMAL], salidaGrep[SALIDA_FALLIDA]};
            System.out.println(crearSalida(datos));

            System.exit(SALIDA_SYSTEM);
        }

        System.out.println(crearSalida(new String[]{MSG_CORRECTO, salidaGrep[SALIDA_NORMAL]}));

    }

    private static String[] ejecCommand(String[] comando, String[] writeInProcess){
        
        final String ERROR_VALUE = "Error al ejecutar el comando";
        
        try {
            Process process = Runtime.getRuntime().exec(comando);
            String output;
            String errOutput;

            if (writeInProcess != null) write(process.getOutputStream(), writeInProcess);

            errOutput = read(process.getErrorStream());
            output = read(process.getInputStream());

            int exitVal = process.waitFor();
            if (exitVal == 0) {

                return new String[] {output};

            } else {

                return new String[] {output, errOutput};
            }

        } catch (IOException | InterruptedException e) {

            return new String[]{ERROR_VALUE, e.toString()};

        }
    }

    private static String read(InputStream is) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            //List<String> output = new ArrayList<>();
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append(SALTO_DE_LINEA);
            }

            return output.toString();

        }
    }

    private static void write(OutputStream os, String[] mensaje) throws IOException{

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {

            for (String line : mensaje) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();

        }
    }

    private static String crearSalida(String[] datos){

        StringBuilder salida = new StringBuilder();

        for (String string : datos) {
            salida.append(string).append(SALTO_DE_LINEA);
        }

        return salida.toString();

    }
}
