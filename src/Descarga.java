import java.util.Random;
// Clase que simula la descarga de un archivo mediante un hilo Thread.
public class Descarga extends Thread {

    private String nombreArchivo;
    private Random random;
    private int tiempoTotal;

    //Constructor de la clase Descarga, inicializa el nombre del archivo a descargar, el objeto Random y establece el contador del tiempo total a 0.
    public Descarga(String nombreArchivo) {
        super("Descarga-" + nombreArchivo);
        this.nombreArchivo = nombreArchivo;
        this.random = new Random();
        this.tiempoTotal = 0;
    }

    //Metodo del hilo que se ejecuta al llamar a .start(),recorre un bucle de 10 iteraciones, generando un tiempo de espera aleatorio  100 ms o 500 ms en cada paso, acumulando el tiempo total y pausando la ejecución con Thread.sleep().
    public void run() {
        System.out.println("Iniciando descarga de " + nombreArchivo + "...");

        for (int i = 1; i <= 10; i++) {
            int valor = random.nextInt(2); // 0 o 1
            int tiempoEspera;
            if (valor == 0) {
                tiempoEspera = 100;
            } else {
                tiempoEspera = 500;
            }
            tiempoTotal += tiempoEspera;
            try {
                Thread.sleep(tiempoEspera);
            } catch (InterruptedException e) {
                return;
            }
            System.out.println("[" + nombreArchivo + "] " + (i * 10) + "%");
        }
        System.out.println("[" + nombreArchivo + "] ¡Completada! en " + tiempoTotal + "ms");

    }

    //Devuelve el tiempo total acumulado que duró la descarga en milisegundos.
    public int getTiempoTotal() {
        return tiempoTotal;
    }
    //Devuelve el nombre del archivo
    public String getNombreArchivo() {
        return this.nombreArchivo;
    }
}
