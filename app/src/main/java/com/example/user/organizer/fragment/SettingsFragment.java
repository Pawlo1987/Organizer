package com.example.user.organizer.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.organizer.DBUtilities;
import com.example.user.organizer.DownloadImageTask;
import com.example.user.organizer.MainActivity;
import com.example.user.organizer.PartialRegexInputFilter;
import com.example.user.organizer.R;
import com.example.user.organizer.SelectLogoActivity;
import com.example.user.organizer.User;
import com.example.user.organizer.inteface.SettingsInterface;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

//-----------Фрагмент вызова настроек--------------------------
public class SettingsFragment extends Fragment implements SettingsInterface{

    View view;
    // коды для идентификации активностей при получении результата
    public final int REQ_SELECT_LOGO = 1001;

    //Параметр -- "ИМЯ КЛЮЧА"
    public static final String PAR_HEAD = "head";

    //параметр для вызова диалога "changeColumnDialog"
    final String ID_CHANGE_COLUMN_DIALOG = "changeColumnDialog";
    //параметр для вызова диалога "deleteProfileDialog"
    final String ID_DELETE_PROFILE_DIALOG = "deleteProfileDialog";

    ChangeColumnDialog changeColumnDialog =
            new ChangeColumnDialog(); // диалог подтверждения изменения данных
    DeleteProfileDialog deleteProfileDialog =
            new DeleteProfileDialog(); // диалог подтверждения изменения данных

    EditText etChangeNameSeFr;      //поле ввода нового имени
    EditText etChangeLoginSeFr;     //поле ввода нового логина
    EditText etChangePasSeFr;       //поле ввода нового пароля
    EditText etChangeEmailSeFr;     //поле ввода нового имейла
    EditText etChangePhoneSeFr;     //поле ввода нового номера телефона
    Spinner spChangeCitySeFr;      //spinner для нового города пользователя

    List<String> spListCity;             // Данные для спинера выбора города

    DBUtilities dbUtilities;
    Context context;
    User user;

    String idAuthUser;                 //авторизированный пользователь

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

        //получаем данныеоб авторизированом пользователе
        user = dbUtilities.getListUser(idAuthUser).get(0);

    }//onAttachToContext

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Показать картинку
        new DownloadImageTask( view.findViewById(R.id.ivProfileLogoSeFr))
                .execute("http://strahovanie.dn.ua/football_db/logo/logo_" + user.getLogo() + ".png");

        FloatingActionButton fabMain = getActivity().findViewById(R.id.fabMain);
        fabMain.setVisibility(View.GONE);
        // привязка поля ввода нового имени
        etChangeNameSeFr = view.findViewById(R.id.etChangeNameSeFr);
        etChangeNameSeFr.setText(user.getName());
        // привязка поля ввода нового логина
        etChangeLoginSeFr = view.findViewById(R.id.etChangeLoginSeFr);
        etChangeLoginSeFr.setText(user.getLogin());
        // привязка поля ввода нового пароля
        etChangePasSeFr = view.findViewById(R.id.etChangePasSeFr);
        etChangePasSeFr.setText(user.getPassword());
        // привязка поля ввода нового имейла
        etChangeEmailSeFr = view.findViewById(R.id.etChangeEmailSeFr);
        etChangeEmailSeFr.setText(user.getEmail());
        // привязка поля ввода нового номера телефона
        etChangePhoneSeFr = view.findViewById(R.id.etChangePhoneSeFr);
        etChangePhoneSeFr.setText(user.getPhone());
        // привязка поля ввода нового города пользователя
        spChangeCitySeFr = view.findViewById(R.id.spChangeCitySeFr);

        //фильтр для пароля
        etChangePasSeFr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                dbUtilities.inputFilterForListener(etChangePasSeFr,
                        10,
                        MainActivity.FILTER_STR
                );
            }//afterTextChanged
        });

        //применяем регулярное выражения для правельности e-mail
        dbUtilities.inputFilterForEmail(etChangeEmailSeFr);

        //применяем регулярное выражения для правельности ввода номера телефона
        dbUtilities.inputFilterForPhoneNumber(etChangePhoneSeFr);
        //для появления и исчезновения первой скобки при наборе телефоного номера
        //при наведениии фокуса на поле ввода номера
        etChangePhoneSeFr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus&&(etChangePhoneSeFr.length()==0||etChangePhoneSeFr.length()==1))etChangePhoneSeFr.setText("");
                if(hasFocus&&etChangePhoneSeFr.length()==0)etChangePhoneSeFr.setText("(");
            }
        });

        //инициализация коллекции для спинера
        spListCity = new ArrayList<>();

        //заполнить spListCity данные для отображения в Spinner
        spListCity = dbUtilities.getStrListTableFromDB("cities", "name");

        spChangeCitySeFr.setAdapter(buildSpinnerStr(spListCity));

        //город который сейчас присвоен пользователю
        String oldUserCity = dbUtilities.searchValueInColumn("users","id","city_id",idAuthUser);

        //Устанавливаем спиннер на позицию oldUserCity
        spChangeCitySeFr.setSelection( spListCity.indexOf(
                dbUtilities.searchValueInColumn("cities", "id", "name",oldUserCity)
        ));//setSelection

        //кнопка вызова процедуры смены логотипа пользователя
        Button btnChangeLogoSeFr = view.findViewById(R.id.btnChangeLogoSeFr);
        //кнопка вызова процедуры смены имени пользователя
        Button btnChangeNameSeFr = view.findViewById(R.id.btnChangeNameSeFr);
        //кнопка вызова процедуры смены логина пользователя
        Button btnChangeLoginSeFr = view.findViewById(R.id.btnChangeLoginSeFr);
        //кнопка вызова процедуры смены пароля пользователя
        Button btnChangePasSeFr = view.findViewById(R.id.btnChangePasSeFr);
        //кнопка вызова процедуры смены имейла пользователя
        Button btnChangeEmailSeFr = view.findViewById(R.id.btnChangeEmailSeFr);
        //кнопка вызова процедуры смены телефоного номера пользователя
        Button btnChangePhoneSeFr = view.findViewById(R.id.btnChangePhoneSeFr);
        //кнопка вызова процедуры смены города пользователя
        Button btnChangeCitySeFr = view.findViewById(R.id.btnChangeCitySeFr);
        //кнопка вызова процедуры удаления аккаунта пользователя
        Button btnDeleteProfileSeFr = view.findViewById(R.id.btnDeleteProfileSeFr);

        btnChangeLogoSeFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectLogoActivity.class);
                intent.putExtra("idAuthUser", idAuthUser);
                intent.putExtra("table", "users");    //для указания в какой таблице менять логотип
                startActivityForResult(intent, REQ_SELECT_LOGO);
            }//onClick
        });//setOnClickListener

        btnChangeNameSeFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etChangeNameSeFr.getText().toString().equals("") ) {
                    Toast.makeText(context, "Пустое поле!", Toast.LENGTH_SHORT).show();
                } else if (etChangeNameSeFr.getText().toString().equals(user.getName()) ) {
                    Toast.makeText(context, "Данные не изменились!", Toast.LENGTH_SHORT).show();
                } else {
                    // Точка вызова отображение диалогового окна
                    callDialogChangeColumn(
                            context,
                            "name",
                            etChangeNameSeFr.getText().toString(),
                            etChangeNameSeFr.getText().toString(),
                            idAuthUser
                    );
                }//if-if-else
            }//onClick
        });//setOnClickListener

        btnChangeLoginSeFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etChangeLoginSeFr.getText().toString().equals("") ) {
                    Toast.makeText(context, "Пустое поле!", Toast.LENGTH_SHORT).show();
                } else if(etChangeLoginSeFr.getText().toString().equals(user.getLogin()) ) {
                    Toast.makeText(context, "Данные не изменились!", Toast.LENGTH_SHORT).show();
                } else if(!dbUtilities.getIdByValue("users", "login",
                        etChangeLoginSeFr.getText().toString()).equals("")){
                    Toast.makeText(context, "Такой логин существует!", Toast.LENGTH_SHORT).show();
                }else{
                    // Точка вызова отображение диалогового окна
                    callDialogChangeColumn(
                            context,
                            "login",
                            etChangeLoginSeFr.getText().toString(),
                            etChangeLoginSeFr.getText().toString(),
                            idAuthUser
                    );
                }//if-if-else
            }//onClick
        });//setOnClickListener

        btnChangePasSeFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etChangePasSeFr.getText().toString().equals("") ) {
                    Toast.makeText(context, "Пустое поле!", Toast.LENGTH_SHORT).show();
                } else if (etChangePasSeFr.getText().toString().equals(user.getPassword()) ) {
                    Toast.makeText(context, "Данные не изменились!", Toast.LENGTH_SHORT).show();
                } else {
                    // Точка вызова отображение диалогового окна
                    callDialogChangeColumn(
                            context,
                            "password",
                            etChangePasSeFr.getText().toString(),
                            etChangePasSeFr.getText().toString(),
                            idAuthUser
                    );
                }//if-if-else
            }//onClick
        });//setOnClickListener

        btnChangeEmailSeFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //проверка корректности введенного email
                boolean testEmailCorrect = dbUtilities.inputCorrectEmail(etChangeEmailSeFr);

                if (etChangeEmailSeFr.getText().toString().equals("")
                        || !testEmailCorrect) {
                    Toast.makeText(context, "Ошибка или пустое поле!", Toast.LENGTH_SHORT).show();
                } else if (etChangeEmailSeFr.getText().toString().equals(user.getEmail()) ) {
                    Toast.makeText(context, "Данные не изменились!", Toast.LENGTH_SHORT).show();
                } else if(!dbUtilities.getIdByValue("users", "email",
                        etChangeEmailSeFr.getText().toString()).equals("")){
                    Toast.makeText(context, "Такой e-mail существует!", Toast.LENGTH_SHORT).show();
                }else{
                    // Точка вызова отображение диалогового окна
                    callDialogChangeColumn(
                            context,
                            "email",
                            etChangeEmailSeFr.getText().toString(),
                            etChangeEmailSeFr.getText().toString(),
                            idAuthUser
                    );
                }//if-if-else
            }//onClick
        });//setOnClickListener

        btnChangePhoneSeFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etChangePhoneSeFr.length()<14) {
                    Toast.makeText(context, "Ошибка или пустое поле!", Toast.LENGTH_SHORT).show();
                } else if (etChangePhoneSeFr.getText().toString().equals(user.getPhone()) ) {
                    Toast.makeText(context, "Данные не изменились!", Toast.LENGTH_SHORT).show();
                } else {
                    // Точка вызова отображение диалогового окна
                    callDialogChangeColumn(
                            context,
                            "phone",
                            etChangePhoneSeFr.getText().toString(),
                            etChangePhoneSeFr.getText().toString(),
                            idAuthUser
                    );
                }//if-if-else
            }//onClick
        });//setOnClickListener

        btnChangeCitySeFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //получаем данные о городе для записи (city_id)
                String city_id = dbUtilities.getIdByValue("cities", "name",
                        spChangeCitySeFr.getSelectedItem().toString()    //Объект спинера(название города)
                );
                if (city_id.equals(user.getCity_id()) ) {
                    Toast.makeText(context, "Данные не изменились!", Toast.LENGTH_SHORT).show();
                } else {
                    // Точка вызова отображение диалогового окна
                    callDialogChangeColumn(
                            context,
                            "city_id",
                            city_id,
                            spChangeCitySeFr.getSelectedItem().toString(),
                            idAuthUser
                    );
                }//if-else
            }//onClick
        });//setOnClickListener

        btnDeleteProfileSeFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Точка вызова отображение диалогового окна
                callDialogDeleteProfile(
                        context,
                        idAuthUser
                );
            }//onClick
        });//setOnClickListener

        return view;
    } // onCreateView

    //строим Spinner
    private ArrayAdapter buildSpinnerStr(List<String> list) {
        ArrayAdapter<String> spinnerAdapter;

        //создание адаптера для спинера
        spinnerAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_item,
                list
        );//spinnerAdapter

        // назначение адапетра для списка
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return spinnerAdapter;
    }//buildCitySpinner

    //-----------------------Метод для приема результатов из активностей----------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
        }//RESULT_CANCELED

        // обработка результатов по активностям
        if (resultCode == RESULT_OK) {
            // Показать картинку
            new DownloadImageTask( view.findViewById(R.id.ivProfileLogoSeFr))
                    .execute("http://strahovanie.dn.ua/football_db/logo/logo_" + user.getLogo() + ".png");
        }//RESULT_OK
    }//onActivityResult


    @Override
    public void callDialogChangeColumn(Context context, String column, String value,
                                       String dataMessage, String eventId) {
        Bundle args = new Bundle();    // объект для передачи параметров в диалог
        args.putString("column", column);
        args.putString("value", value);
        args.putString("dataMessage", dataMessage);
        args.putString("user_id", idAuthUser);
        changeColumnDialog.setArguments(args);

        // Точка вызова отображение диалогового окна
        changeColumnDialog.show( ((AppCompatActivity)context).
                getSupportFragmentManager(), ID_CHANGE_COLUMN_DIALOG);
    }//callDialogChangeColumn

    @Override
    public void callDialogDeleteProfile(Context context, String eventId) {
        Bundle args = new Bundle();    // объект для передачи параметров в диалог
        args.putString("user_id", idAuthUser);
        deleteProfileDialog.setArguments(args);

        // Точка вызова отображение диалогового окна
        deleteProfileDialog.show( ((AppCompatActivity)context).
                getSupportFragmentManager(), ID_DELETE_PROFILE_DIALOG);
    }//callDialogDeleteProfile

}//SettingsFragment
