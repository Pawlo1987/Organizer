package com.example.user.organizer.fragment;

//-----------Фрагмент выводит все активные события--------------------------

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.R;
import com.example.user.organizer.ShowAllEventsRecyclerAdapter;
import com.example.user.organizer.inteface.CallDialogsAllEvents;

public class ShowAllEventsFragment extends Fragment
        implements CallDialogsAllEvents {

    RecyclerView rvMainShAlEvAc;

    // адаптер для отображения recyclerView
    ShowAllEventsRecyclerAdapter showAllEventsRecyclerAdapter;
    DBUtilities dbUtilities;
    Context context;

    String idAuthUser;                 //авторизированный пользователь

    AboutEventShowAllEventDialog aboutEventShowAllEventDialog =
            new AboutEventShowAllEventDialog(); // диалог подтверждения выхода из приложения

    TakePartShowAllEventDialog takePartShowAllEventDialog =
            new TakePartShowAllEventDialog(); // диалог подтверждения выхода из приложения

    final String ID_ABOUT_DIALOG = "aboutEventShowAllEventDialog";  //параметр для вызова диалога "about"
    final String ID_TAKEPART_DIALOG = "takePartShowAllEventDialog";  //параметр для вызова диалога "takePart"

    // Метод onAttach() вызывается в начале жизненного цикла фрагмента, и именно здесь
    // мы можем получить контекст фрагмента, в качестве которого выступает класс MainActivity.
    @Override
    public void onAttach(Context context) {
        this.context = context;
        dbUtilities = new DBUtilities(context);

        // прочитать данные, переданные из активности (из точки вызова)
        idAuthUser = getArguments().getString("idAuthUser");

        // создаем адаптер, передаем в него курсор
        showAllEventsRecyclerAdapter = new ShowAllEventsRecyclerAdapter(this, context, idAuthUser);

        super.onAttach(context);
    } // onAttach

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_show_all_event, container, false);
        // RecyclerView для отображения таблицы users БД
        rvMainShAlEvAc = result.findViewById(R.id.rvMainShAlEvAc);

        //привязываем адаптер к recycler объекту
        rvMainShAlEvAc.setAdapter(showAllEventsRecyclerAdapter);

        return result;
    } // onCreateView

    @Override
    public void aboutDialog(Context context, String message) {
        Bundle args = new Bundle();    // объект для передачи параметров в диалог
        args.putString("message", message);
        aboutEventShowAllEventDialog.setArguments(args);

        // отображение диалогового окна
        aboutEventShowAllEventDialog.show(((AppCompatActivity)context).
                getSupportFragmentManager(), ID_ABOUT_DIALOG);
    }//aboutDialog

    @Override
    public void takePart(Context context, String eventId, boolean userTakeInPart, String message) {
        Bundle args = new Bundle();    // объект для передачи параметров в диалог
        args.putString("message", message);
        args.putBoolean("userTakeInPart", userTakeInPart);
        args.putString("event_id", eventId);
        args.putString("user_id", idAuthUser);
        takePartShowAllEventDialog.setArguments(args);

        // отображение диалогового окна
        takePartShowAllEventDialog.show(((AppCompatActivity)context).
                getSupportFragmentManager(), ID_TAKEPART_DIALOG);
    }//takePart
}
