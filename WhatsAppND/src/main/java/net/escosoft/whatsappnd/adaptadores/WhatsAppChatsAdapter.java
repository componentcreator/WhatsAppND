package net.escosoft.whatsappnd.adaptadores;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.escosoft.whatsappnd.R;
import net.escosoft.whatsappnd.modelo.WhatsAppChat;

import java.util.List;

/**
 * Created by Victor on 24/12/13.
 */
public class WhatsAppChatsAdapter extends ArrayAdapter<WhatsAppChat> {

    private SparseArray<View> view_cache;
    private List<WhatsAppChat> chats;
    private Context contexto;

    public WhatsAppChatsAdapter(Context context, int resource, List<WhatsAppChat> data) {
        super(context, resource);
        chats = data;
        contexto = context;
        view_cache = new SparseArray<View>();
    }

    @Override
    public int getCount() {
        return chats.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (view_cache.get(position) == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.whatsapp_chat_row, parent, false);

            WhatsAppChat chat = chats.get(position);
            TextView chat_name = (TextView) row.findViewById(R.id.chat_name);

            chat_name.setText(chat.getNombre());

            view_cache.put(position, row);
        }

        return view_cache.get(position);
    }
}
