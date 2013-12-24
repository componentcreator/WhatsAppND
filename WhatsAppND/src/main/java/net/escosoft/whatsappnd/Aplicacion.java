package net.escosoft.whatsappnd;

import android.app.Application;
import android.content.Context;

/**
 * Clase para inicializar la aplicación añadiendo una librerias
 */
public class Aplicacion extends Application {

    private static Context contexto;

    @Override
    public void onCreate() {
        super.onCreate();
        contexto = getApplicationContext();
    }

    public static Context getContexto(){
        return contexto;
    }
}
