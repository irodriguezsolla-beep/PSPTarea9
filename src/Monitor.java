public class Monitor implements Runnable {
    private Descarga[] descargas;

    // Recibe el array de descargas
    public Monitor(Descarga[] descargas) {
        this.descargas = descargas;
    }
    //run del hilo Monitor
    public void run() {
        boolean hayActivas = true;
        // bucle que hayActivas seguirá activo hasta que hayActivas sea false
        while (hayActivas) {
            int activas = 0;
            // Contamos cuántas descargas siguen ejecutándose mediante un bucle que recorre la lista de descargas
            for (Descarga descarga : descargas) {
                // si descarga es diferente anulo y además el proceso está vivo suma 1 a activas
                if (descarga != null && descarga.isAlive()) {
                    activas++;
                }
            }
            //si activas es mayor a 0  imprime
            if (activas > 0) {
                System.out.println("[MONITOR] Descargas en curso: " + activas);
            } else {
                // Si no queda ninguna activa, salimos del bucle
                System.out.println("[Monitor] No queda ninguna descarga en curso");
                hayActivas = false;
            }

            try {
                // Pausa de 500 ms entre cada revisión
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // Si se interrumpe el monitor, se interrumpe el ciclo
                break;
            }


        }

    }
}
