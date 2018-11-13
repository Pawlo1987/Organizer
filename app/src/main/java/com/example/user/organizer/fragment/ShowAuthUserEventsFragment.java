package com.example.user.organizer.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.R;
import com.example.user.organizer.ShowAllEventsRecyclerAdapter;
import com.example.user.organizer.ShowAuthUserEventsRecyclerAdapter;
import com.example.user.organizer.inteface.AuthUserEventsInterface;

import java.util.ArrayList;
import java.util.List;

//-----------Фрагмент выводит активные события авторизированного пользователя---------------------
public class ShowAuthUserEventsFragment extends Fragment
implements AuthUserEventsInterface {
    RecyclerView rvMainShAuUsEvAc;

    // адаптер для отображения recyclerView
    ShowAuthUserEventsRecyclerAdapter showAuthUserEventsRecyclerAdapter;
    DBUtilities dbUtilities;

    int spPos;                      //позиция спинера
    List<String> spListCity;             // Данные для спинера выбора города
    Spinner spCityShAuUsEvAc;           //основной спинер выбора города
    Context context;
    String idAuthUser;

    private static final int REQUEST_POS = 1;

    //параметр для вызова диалога "about"
    final String ID_ABOUT_DIALOG = "aboutEventShowAllEventDialog";
    //параметр для вызова диалога "leave"
    final String ID_LEAVE_DIALOG = "leaveEventShowAuthUserEventDialog";
    //параметр для вызова диалога "delete"
    final String ID_DELETE_DIALOG = "deleteEventShowAuthUserEventDialog";

    AboutEventShowAllEventDialog aboutEventShowAllEventDialog =
            new AboutEventShowAllEventDialog(); // диалог подтверждения выхода из приложения

    LeaveEventDialog leaveEventDialog =
            new LeaveEventDialog(); // диалог подтверждения покинуть событие

    DeleteEventDialog deleteEventDialog =
            new DeleteEventDialog(); // диалог подтверждения удалить событие

    // Метод onAttach() вызывается в начале жизненного цикла фрагмента, и именно здесь
    // мы можем получить контекст фрагмента, в качестве которого выступает класс MainActivity.
    //onAttach(Context) не вызовется до API 23 версии вместо этого будет вызван onAttach(Activity),
    //коий устарел с 23 API
    //Так что вызовем onAttachToContext
    //https://ru.stackoverflow.com/questions/507008/%D0%9D%D0%B5-%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%B0%D0%B5%D1%82-onattach
    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }//onAttach

    //устарел с 23 API
    //Так что вызовем onAttachToContext
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }//if
    }//onAttach

    //Вызовется в момент присоединения фрагмента к активити
    protected void onAttachToContext(Context context) {
        //здесь всегда есть контекст и метод всегда вызовется.
        //тут можно кастовать контест к активити.
        //но лучше к реализуемому ею интерфейсу
        //чтоб не проверять из какого пакета активити в каждом из случаев
        this.context = context;
        dbUtilities = new DBUtilities(context);

        // прочитать данные, переданные из активности (из точки вызова)
        idAuthUser = getArguments().getString("idAuthUser");
    }//onAttachToContext

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_show_auth_user_events,  container, false);
        FloatingActionButton fabMain = getActivity().findViewById(R.id.fabMain);
        fabMain.setVisibility(View.VISIBLE);

        // RecyclerView для отображения таблицы users БД
        rvMainShAuUsEvAc = result.findViewById(R.id.rvMainShAuUsEvAc);

        //привязка ресурсов к объектам
        spCityShAuUsEvAc = getActivity().findViewById(R.id.spCityMain);
        //инициализация коллекции для спинера
        spListCity = new ArrayList<>();
        //обращаемся к базе для получения списка имен городов
        spListCity = dbUtilities.getStrListTableFromDB("cities", "name");

        //Слушатель для позиции спинера и фильтрации RecyclerView по изменению позиции
        spCityShAuUsEvAc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spPos = position;
                //проверка если выбран пункт "Все города"
                if(spPos == (spListCity.size()))buildUserRecyclerView("");
                else {
                    String cityId = dbUtilities.getIdByValue("cities", "name",
                            spCityShAuUsEvAc.getItemAtPosition(spPos).toString());
                    //строим новый адаптер RecyclerView
                    buildUserRecyclerView(cityId);
                }//if
            }//onItemSelected
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}//onNothingSelected
        });

        //проверка если выбран пункт "Все города"
        if(spCityShAuUsEvAc.getSelectedItemPosition() == (spListCity.size()))buildUserRecyclerView("");
        else {
            String cityId = dbUtilities.getIdByValue("cities", "name",
                    spCityShAuUsEvAc.getItemAtPosition(spCityShAuUsEvAc.getSelectedItemPosition()).toString());
            //строим новый адаптер RecyclerView
            buildUserRecyclerView(cityId);
        }

        //кнопка обовления списка recyclerView
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList();
            }
        });

        return result;
    } // onCreateView

    //Строим RecyclerView
    private void buildUserRecyclerView(String cityId) {
        // создаем адаптер, передаем в него курсор
        // параметр this в данном случае используется для передачи интерефеса AuthUserEventsInterface
        // в адаптер
        showAuthUserEventsRecyclerAdapter
                = new ShowAuthUserEventsRecyclerAdapter(this, context, idAuthUser, cityId);
        //привязываем адаптер к recycler объекту
        rvMainShAuUsEvAc.setAdapter(showAuthUserEventsRecyclerAdapter);
    }//buildUserRecyclerView

    //обновляем recyclerview
    private void refreshList() {
        showAuthUserEventsRecyclerAdapter.updateEventList();
        //привязываем адаптер к recycler объекту
        rvMainShAuUsEvAc.setAdapter(showAuthUserEventsRecyclerAdapter);
    }//refreshList

    // точка выхода из DialogFragment при положительных
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_POS:
                    showAuthUserEventsRecyclerAdapter.updateEventList();
                    //привязываем адаптер к recycler объекту
                    rvMainShAuUsEvAc.setAdapter(showAuthUserEventsRecyclerAdapter);
                    break;
            }//switch
        }//if
    }//onActivityResult

    //Метод имплеметируемые интерфейсом AuthUserEventsInterface
    @Override
    public void callDialogLeaveDialog(Context context, String message, String eventId) {
            Bundle args = new Bundle();    // объект для передачи параметров в диалог
            args.putString("message", message);
            args.putString("event_id", eventId);
            args.putString("user_id", idAuthUser);
            leaveEventDialog.setArguments(args);

        // Возврат результата выполнения из DialogFragment во Fragment минуя Activity
        // ссылка (https://habrahabr.ru/post/259805/)
        leaveEventDialog.setTargetFragment(this, REQUEST_POS);
        // Точка вызова отображение диалогового окна
        leaveEventDialog.show(getFragmentManager(), ID_LEAVE_DIALOG);
    }//callDialogLeaveDialog

    //Метод имплеметируемые интерфейсом AuthUserEventsInterface
    @Override
    public void callDialogDeleteDialog(Context context, String message, String eventId) {
        Bundle args = new Bundle();    // объект для передачи параметров в диалог
        args.putString("message", message);
        args.putString("event_id", eventId);
        args.putString("user_id", idAuthUser);
        deleteEventDialog.setArguments(args);

        // Возврат результата выполнения из DialogFragment во Fragment минуя Activity
        // ссылка (https://habrahabr.ru/post/259805/)
        deleteEventDialog.setTargetFragment(this, REQUEST_POS);
        // Точка вызова отображение диалогового окна
        deleteEventDialog.show(getFragmentManager(), ID_DELETE_DIALOG);
    }//callDialogDeleteDialog

    //Метод имплеметируемые интерфейсом AuthUserEventsInterface
    @Override
    public void callDialogAboutDialog(Context context, String message) {
        Bundle args = new Bundle();    // объект для передачи параметров в диалог
        args.putString("message", message);
        aboutEventShowAllEventDialog.setArguments(args);
         // отображение диалогового окна
         aboutEventShowAllEventDialog.show(((AppCompatActivity)context).
                getSupportFragmentManager(), ID_ABOUT_DIALOG);
    }//callDialogAboutDialog
}//ShowAuthUserEventsFragment
