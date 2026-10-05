import java.util.Scanner;

public class GestorDescargas {

    public static void main(String[] args) {
        //Escaner para leer la entrada por consola
        Scanner scanner = new Scanner(System.in);
        // Lista vacía de los nombres de de los archivos
        String[] nombresArchivos = null;

        //Bucle while que se ejecutara hasta que la lista de nombres de archivos sea diferente a null
        while(nombresArchivos == null){
            System.out.println("¿Cuántos archivos quieres descargar? (deja en blanco o pulsa Enter para descargar los 4 por defecto):");
            //Lee toda la línea de texto que el usuario escribe y quita los espacios del inicio y el final.
            String Cantidad = scanner.nextLine().trim();

            //Si la cadena Cantidad no está vacía entonces es .
            //isEmpty() sirve para comprobar si una cadena de texto, colección o estructura de datos está vacía
            if (!Cantidad.isEmpty()){
                try{
                    //Convierte el texto ingresado a un número entero
                    int cantidad = Integer.parseInt(Cantidad);

                    // Verifica que el número ingresado sea mayor que cero
                    if(cantidad > 0){
                        // Inicializa el array de nombres con el tamaño especificado por el usuario
                        nombresArchivos = new String[cantidad];
                        // Pide el nombre de cada archivo uno por uno en un bucle
                        for (int i = 0; i < cantidad; i++) {
                            System.out.println("Introduce el nombre del archivo " + (i + 1) + ":");
                            // Lee el nombre ingresado y elimina los espacios en blanco sobrantes
                            nombresArchivos[i] = scanner.nextLine().trim();
                        }
                    }else{
                        // Muestra un mensaje si el usuario introdujo un número menor o igual a cero
                        System.out.println("Error: El número tiene que ser mayor a 0.");
                    }
                }catch(NumberFormatException error){
                    // Captura la excepción si el texto ingresado no se puede convertir a un número entero.
                    System.out.println("Error: Por favor, introduce un número entero válido.");
                }
            }else{
                //Nombres de los archivos a descargar predefinidos
                nombresArchivos = new String[]{
                        "cuarzos.png",
                        "meditacion.mp4",
                        "horoscopo.pdf",
                        "mantras.mp3"
                };

            }

        }




        //Contar la cantidad de archivos que se van a descargar
        int totalDescargas = nombresArchivos.length;
        //Creación de la lista de objetos descarga con los espacios suficientes de de nombres de archivos
        Descarga[] descargas = new Descarga[totalDescargas];
        //Creacion de los objetos dentro de la lista
        for (int i = 0; i < totalDescargas; i++) {
            descargas[i] = new Descarga(nombresArchivos[i]);
        }

        // Medición de tiempo real con reloj del sistema
        long tiempoInicioReal = System.currentTimeMillis();

        //Arrancar todos los hilos
        for (Descarga descarga : descargas) {
            descarga.start();
        }

        //Esperar a que terminen todos los hilos (join)
        for (Descarga descarga : descargas) {
            try {
                descarga.join();
            } catch (InterruptedException e) {
                System.err.println("El hilo principal fue interrumpido: " + e.getMessage());
            }
        }

        long tiempoFinReal = System.currentTimeMillis();
        long tiempoRealTotal = tiempoFinReal - tiempoInicioReal;

        //Calcular la suma de los tiempos de cada descarga (secuencial)
        int tiempoSumaSecuencial = 0;
        for (Descarga descarga : descargas) {
            tiempoSumaSecuencial += descarga.getTiempoTotal();
        }

        //Imprimir resultados finales
        System.out.println("Todas las descargas han terminado.");
        System.out.println("Tiempo real transcurrido (en paralelo): " + tiempoRealTotal + " ms");
        System.out.println("Tiempo estimado si fuera una detrás de otra (secuencial): " + tiempoSumaSecuencial + " ms");
    }
}