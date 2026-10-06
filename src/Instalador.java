public class Instalador implements Runnable {
    private Descarga descargaMeditacion;
    private Descarga descargaMantras;

    public Instalador(Descarga descargaMeditacion, Descarga descargaMantras) {
        this.descargaMeditacion = descargaMeditacion;
        this.descargaMantras = descargaMantras;
    }

    public void run() {
        try {
            // Espera a que terminen únicamente meditacion.mp4 y mantras.mp3
            if (descargaMeditacion != null) {
                descargaMeditacion.join();
            }
            if (descargaMantras != null) {
                descargaMantras.join();
            }

            // Una vez finalizadas ambas, procede a instalar
            System.out.println("[Instalador] Meditación y mantras listos: instalando...");
            // Simula el tiempo que tarda la instalación
            Thread.sleep(1000);
            System.out.println("[Instalador] Instalación terminada");

        } catch (InterruptedException e) {
            System.err.println("[Instalador] El proceso de instalación fue interrumpido.");
        }
    }
}