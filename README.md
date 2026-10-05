# Tarea 09
## Nivel 1-3
### Nivel 1
#### Clase Descarga
Para simular la descarga concurrente de un archivo, creé la clase `Descarga` heredando de `Thread` e implementando un constructor que le asigna un nombre personalizado al hilo mediante `super()`, además de inicializar el generador de números aleatorios `Random` y el contador `tiempoTotal`. La lógica principal la programé dentro del método sobrescrito `run()`, donde utilicé un bucle para simular el avance del archivo en 10 tramos progresivos del 10% al 100%; en cada paso del bucle, simulo las variaciones y latencias de la red asignando aleatoriamente una pausa de 100 ms o 500 ms mediante `Thread.sleep()`. Durante este proceso, voy acumulando cada intervalo de tiempo en el atributo `tiempoTotal`, imprimo el porcentaje actual por pantalla y gestiono la excepción `InterruptedException` para cancelar la ejecución de forma limpia si el hilo es interrumpido. Finalmente, al concluir el ciclo muestro el mensaje de finalización e incluyo los métodos `getTiempoTotal()` y `getNombreArchivo()` para que el hilo principal pueda consultar la duración acumulada y los metadatos de la descarga una vez terminada.
![Descarga.png](Fotos/Descarga.png)

Código
```java
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

```
#### Clase GestorDescargas
En el programa principal `GestorDescargas`, creé un array con los nombres de los cuatro archivos que quería bajar e instancié un objeto de mi clase `Descarga` para cada uno de ellos. Luego, usé un bucle para arrancar todos los hilos al mismo tiempo llamando al método `.start()`, justo después de guardar la hora de inicio con `System.currentTimeMillis()`. Para asegurarme de que el programa no terminara antes de tiempo, puse un segundo bucle utilizando `.join()` con su correspondiente `try-catch`, lo que obliga al hilo principal a esperar a que cada descarga finalice. Una vez que todas terminaron, calculé el tiempo total real que tomó el proceso y recorrí las descargas con `getTiempoTotal()` para sumar sus tiempos individuales, imprimiendo al final los mensajes con la comparativa entre el tiempo real en paralelo y lo que habría tardado en modo secuencial.
![GestorDescargas.png](Fotos/GestorDescargas.png)
```java
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
```

<u>Preguntas a la IA en el nivel 1:</u>
¿Cómo puedo calcular el tiempo que tarda en ejecutarse un trozo de código en Java?
Para medir la duración de un bloque de código en Java se utiliza el método estático System.currentTimeMillis(), el cual devuelve el tiempo actual en milisegundos. El procedimiento consiste en guardar el valor inicial en una variable de tipo long justo antes de empezar la tarea que se quiere evaluar, capturar nuevamente el tiempo de la misma forma al finalizar el proceso, y restar ambos valores (tiempoFinal - tiempoInicial) para obtener la duración exacta transcurrida en milisegundos.



##### Pruebas del Nivel 1:
| **Ejecución** | **Descarga más lenta** | **Tiempo real(ms)** | **Suma** |
|:---:|:----------------------:|:-------------------:|:--------:|
|1|         3400ms         |       4206 ms       | 14800 ms |
|2|         2600ms         |       3410 ms       | 12000 ms |
|3|         2200ms         |       4607 ms       | 12400 ms |
* ¿Por qué el tiempo real es mucho menor que la suma?

  El tiempo real es mucho menor porque todas las descargas se hacen a la vez y al mismo tiempo gracias a los hilos, así que el programa solo tarda lo que le lleve terminar a la más lenta; en cambio, la suma calcula lo que tardaría si tuvieras que esperar a que termine una para recién empezar la siguiente.

* ¿Qué pasa si hacéis `start()` y `join()` dentro del mismo bucle?
  Si ejecutamos `start()` y `join()` en el mismo bucle, el programa se vuelve totalmente secuencial: el hilo principal inicia la primera descarga y se frena a esperar a que termine antes de pasar a la siguiente, haciendo que se procesen una detrás de otra y elevando el tiempo real a la suma de todas

Código:
```java
public class GestorDescargas {

    public static void main(String[] args) {
        String[] archivos = {
                "meditacion.mp4",
                "documental.mkv",
                "musica.mp3",
                "tutorial.pdf"
        };

        Descarga[] descargas = new Descarga[archivos.length];

        for (int i = 0; i < archivos.length; i++) {
            descargas[i] = new Descarga(archivos[i]);
        }

        long tiempoInicioReal = System.currentTimeMillis();

        //  start() y join() dentro del mismo bucle
        for (Descarga descarga : descargas) {
            try {
                descarga.start(); // Inicia el hilo
                descarga.join();  // Obliga al main a esperar a que termine ANTES de pasar al siguiente hilo
            } catch (InterruptedException e) {
                System.err.println("El hilo principal fue interrumpido: " + e.getMessage());
            }
        }

        long tiempoFinReal = System.currentTimeMillis();
        long tiempoRealTotal = tiempoFinReal - tiempoInicioReal;

        int tiempoSumaSecuencial = 0;
        for (Descarga descarga : descargas) {
            tiempoSumaSecuencial += descarga.getTiempoTotal();
        }

        System.out.println("Todas las descargas han terminado.");
        System.out.println("Tiempo real transcurrido (en paralelo simulado): " + tiempoRealTotal + " ms");
        System.out.println("Tiempo estimado si fuera una detrás de otra (secuencial): " + tiempoSumaSecuencial + " ms");
    }
}
```
Resultado
```terminaloutput
Iniciando descarga de meditacion.mp4...
[meditacion.mp4] 10%
[meditacion.mp4] 20%
[meditacion.mp4] 30%
[meditacion.mp4] 40%
[meditacion.mp4] 50%
[meditacion.mp4] 60%
[meditacion.mp4] 70%
[meditacion.mp4] 80%
[meditacion.mp4] 90%
[meditacion.mp4] 100%
[meditacion.mp4] ¡Completada! en 3800ms
Iniciando descarga de documental.mkv...
[documental.mkv] 10%
[documental.mkv] 20%
[documental.mkv] 30%
[documental.mkv] 40%
[documental.mkv] 50%
[documental.mkv] 60%
[documental.mkv] 70%
[documental.mkv] 80%
[documental.mkv] 90%
[documental.mkv] 100%
[documental.mkv] ¡Completada! en 3800ms
Iniciando descarga de musica.mp3...
[musica.mp3] 10%
[musica.mp3] 20%
[musica.mp3] 30%
[musica.mp3] 40%
[musica.mp3] 50%
[musica.mp3] 60%
[musica.mp3] 70%
[musica.mp3] 80%
[musica.mp3] 90%
[musica.mp3] 100%
[musica.mp3] ¡Completada! en 2600ms
Iniciando descarga de tutorial.pdf...
[tutorial.pdf] 10%
[tutorial.pdf] 20%
[tutorial.pdf] 30%
[tutorial.pdf] 40%
[tutorial.pdf] 50%
[tutorial.pdf] 60%
[tutorial.pdf] 70%
[tutorial.pdf] 80%
[tutorial.pdf] 90%
[tutorial.pdf] 100%
[tutorial.pdf] ¡Completada! en 3000ms

Todas las descargas han terminado.
Tiempo real transcurrido (en paralelo simulado): 13229 ms
Tiempo estimado si fuera una detrás de otra (secuencial): 13200 ms

Process finished with exit code 0
```




