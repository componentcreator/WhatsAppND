package net.escosoft.whatsappnd.fragmentos;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import net.escosoft.whatsappnd.Aplicacion;
import net.escosoft.whatsappnd.R;
import net.escosoft.whatsappnd.actividades.ActividadPrincipal;
import net.escosoft.whatsappnd.adaptadores.WhatsAppChatsAdapter;
import net.escosoft.whatsappnd.modelo.WhatsAppChat;
import net.escosoft.whatsappnd.util.BDHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Victor on 24/12/13.
 */
public class WhatsAppChats extends Fragment implements View.OnClickListener {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListView chat_group;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WhatsAppChats newInstance(int sectionNumber) {
        WhatsAppChats fragment = new WhatsAppChats();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public WhatsAppChats() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_whatsapp_chats, container, false);
        Button import_button = (Button) rootView.findViewById(R.id.import_button);
        import_button.setOnClickListener(this);
        chat_group = (ListView) rootView.findViewById(R.id.grupos);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((ActividadPrincipal) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onClick(View v) {
        String databaseName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/msgstore.db";
        BDHelper.setDatabaseName(databaseName);

        List<WhatsAppChat> grupos = null;
        try {
            grupos = WhatsAppChat.getGrupos();
            WhatsAppChatsAdapter groupAdapter = new WhatsAppChatsAdapter(Aplicacion.getContexto(), 0, grupos);
            chat_group.setAdapter(groupAdapter);
            chat_group.setVisibility(View.VISIBLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

