package com.example.user.organizer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.organizer.fragment.AboutFieldShowFieldCatalogDialog;

import java.util.ArrayList;
import java.util.List;


//-------Адаптера для вывода(просмотра) каталога полей--------------
public class ShowFieldCatalogRecyclerAdapter extends
        RecyclerView.Adapter<ShowFieldCatalogRecyclerAdapter.ViewHolder>{

    //поля класса ShowAllEventsRecyclerAdapter
    private LayoutInflater inflater;
    Field field = new Field();
    List<Field> fieldList = new ArrayList<>(); //коллекция событий
    DBUtilities dbUtilities;
    Context context;
    String idAuthUser;         //Авторизированный пользователь

    String id;
    String city_id;
    String name;
    String phone;
    String light_status;
    String coating_id;
    String shower_status;
    String roof_status;
    String geo_long;
    String geo_lat;
    String address;
    String user_id;

    AboutFieldShowFieldCatalogDialog aboutFieldShowFieldCatalogDialog =
            new AboutFieldShowFieldCatalogDialog(); // диалог подроднее о поле

    //параметр для вызова диалога "about"
    final String ID_ABOUT_DIALOG = "aboutFieldShowFieldCatalogDialog";

    //конструктор
    public ShowFieldCatalogRecyclerAdapter(Context context, List<Field> fieldList, String idAuthUser) {
        this.inflater = LayoutInflater.from(context);
        this.fieldList = fieldList;
        this.context = context;
        this.idAuthUser = idAuthUser;
        dbUtilities = new DBUtilities(context);
    } // ShowFieldCatalogRecyclerAdapter


    //создаем новую разметку(View) путем указания разметки
    @Override
    public ShowFieldCatalogRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_adapter_show_field_show, parent, false);
        return new ViewHolder(view);
    }

    //привязываем элементы разметки к переменным объекта(в данном случае к курсору)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Field fieldShow = fieldList.get(position);

        // получение данных
        holder.tvCityShFiCaReAd.setText(fieldShow.city_id);             //город
        holder.tvNameShFiCaReAd.setText("\""+fieldShow.name+"\"");      //поле
        //опредиляемся с цветом CardView взависимости от роли пользователя в собитии
        holder.cvMainShFiCaReAd.setCardBackgroundColor(
                (fieldShow.user_id.equals(idAuthUser))
                        ?context.getResources().getColor(R.color.colorMyColorGold)
                        :context.getResources().getColor(R.color.colorMyColorGreen)
        );
    } // onBindViewHolder

    //получаем количество элементов объекта(курсора)
    @Override
    public int getItemCount() { return fieldList.size(); }

    //Создаем класс ViewHolder с помощью которого мы получаем ссылку на каждый элемент
    //отдельного пункта списка и подключаем слушателя события нажатия меню
    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView tvCityShFiCaReAd, tvNameShFiCaReAd;
        ImageView ivInfoShFiCaReAd;
        CardView cvMainShFiCaReAd;

        ViewHolder(View view){
            super(view);

            tvCityShFiCaReAd = view.findViewById(R.id.tvCityShFiCaReAd);
            tvNameShFiCaReAd = view.findViewById(R.id.tvNameShFiCaReAd);
            ivInfoShFiCaReAd = view.findViewById(R.id.ivInfoShFiCaReAd);
            cvMainShFiCaReAd = view.findViewById(R.id.cvMainShFiCaReAd);

            //слушатель события нажатия инфо-значка
            ivInfoShFiCaReAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //получаем данные о нажатом событии
                    field = fieldList.get(getAdapterPosition());
                    id = field.id;
                    city_id = field.city_id;
                    name = field.name;
                    phone = field.phone;
                    light_status = field.light_status;
                    coating_id = field.coating_id;
                    shower_status = field.shower_status;
                    roof_status = field.roof_status;
                    geo_long = field.geo_long;
                    geo_lat = field.geo_lat;
                    address = field.address;
                    user_id = field.user_id;

                    aboutField();
                }//onClick
            });//setOnClickListener

            //onTouch
            cvMainShFiCaReAd.setOnTouchListener((v, event) -> {
                //опредиляемся с цветом CardView взависимости от роли пользователя в собитии
                int color = fieldList.get(getAdapterPosition()).user_id.equals(idAuthUser)
                        ?context.getResources().getColor(R.color.colorMyColorGold)
                        :context.getResources().getColor(R.color.colorMyColorGreen);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        cvMainShFiCaReAd.setCardBackgroundColor(
                                context.getResources().getColor(R.color.colorMyColorWhite)
                        );
                        break;
                    case MotionEvent.ACTION_UP:
                        cvMainShFiCaReAd.setCardBackgroundColor(color);
                        break;
                    default:
                        cvMainShFiCaReAd.setCardBackgroundColor(color);
                        break;
                }//switch
                return false;
            });//setOnTouchListener

            //onLongClick
            cvMainShFiCaReAd.setOnLongClickListener(v -> {
                //получаем данные о выбранном поле
                field = fieldList.get(getAdapterPosition());
                //если в данном событии авторизированный пользователь создатель поля
                if(field.user_id.equals(idAuthUser)) {
//                    eventId = event.eventId;
//                    eventCityName = event.cityName;
//                    eventFieldName = event.fieldName;
//                    eventDate = event.eventData;
//                    showEventDate = dateShowFormat(eventDate);
//                    eventTime = event.eventTime;
//                    eventDuration = event.eventDuration;
//                    eventPrice = event.eventPrice;
//                    eventPhone = event.eventPhone;
//                    eventUserId = event.eventUserId;
//
//                    //выбран пункт редактировать события
//                    editEvent();
                }else {
                    Toast.makeText(context, "Нет прав для редактирования!", Toast.LENGTH_SHORT).show();
                }//if-else
                return true;
            });//setOnLongClickListener

        } // ViewHolder

        //Вызов диалога для подробной информации о поле
        private void aboutField() {
            String message = fullInfoAboutField();
            Bundle args = new Bundle();    // объект для передачи параметров в диалог
            args.putString("message", message);
            aboutFieldShowFieldCatalogDialog.setArguments(args);

            // отображение диалогового окна
            aboutFieldShowFieldCatalogDialog.show(((AppCompatActivity)context).
                    getSupportFragmentManager(), ID_ABOUT_DIALOG);
        }//aboutField

        //строка с полной информацией о поле
        private String fullInfoAboutField() {
            return String.format("Поле находится в городе %s\n" +
                            "Называется %s \n" +
                            "Номер телефона %s\n" +
                            "Освещение %s\n" +
                            "Покрытие %s\n" +
                            "Душ %s\n" +
                            "Крыша %s\n"+
                            "Геопозиция долгота %s\n" +
                            "Геопозиция широта %s\n" +
                            "Адресс %s",
                    city_id,
                    name,
                    phone,
                    light_status.equals("0") ? "нет" : "есть",
                    coating_id,
                    shower_status.equals("0") ? "нет" : "есть",
                    roof_status.equals("0") ? "нет" : "есть",
                    geo_long,
                    geo_lat,
                    address);
        }//fullInfoAboutField
    } // class ViewHolder

}//ShowFieldCatalogRecyclerAdapter
