public class GestorDescargas {

    public static void main(String[] args) {
        //Nombres de los archivos a descargar
        String[] archivos = {
                "meditacion.mp4",
                "documental.mkv",
                "musica.mp3",
                "tutorial.pdf"
        };

        Descarga[] descargas = new Descarga[archivos.length];

        //Creación de las 4 descargas
        for (int i = 0; i < archivos.length; i++) {
            descargas[i] = new Descarga(archivos[i]);
        }

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