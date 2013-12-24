package net.escosoft.whatsappnd.modelo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.escosoft.whatsappnd.util.BDHelper;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase para modelizar un chat de WhatsApp
 */
public class WhatsAppChat {

    private static final String NOMBRE = "subject";
    private static final String ID = "_id";
    private static final String REMOTE_JID = "key_remote_jid";
    private static final String BD_TABLE = "chat_list";

    private int id;
    private String jid_remoto;
    private String nombre;

    public WhatsAppChat(int id, String jid_remoto, String nombre) {
        this.id = id;
        this.jid_remoto = jid_remoto;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public static ArrayList<WhatsAppChat> getGrupos() throws SQLException {
        SQLiteDatabase db = BDHelper.openBDHelper();
        Cursor cursor = db.query(BD_TABLE, new String[]{ID, REMOTE_JID, NOMBRE}, NOMBRE + " NOT LIKE ''", null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ArrayList<WhatsAppChat> grupos = new ArrayList<WhatsAppChat>();

        do {
            grupos.add(new WhatsAppChat(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
        } while (cursor.moveToNext());

        return grupos;

    }
}
