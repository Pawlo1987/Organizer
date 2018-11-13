package com.example.user.organizer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

//-------Адаптер для вывода(просмотра) новости и реклама --------------
public class AdvertisingAndInformationRecyclerAdapter extends RecyclerView.Adapter<AdvertisingAndInformationRecyclerAdapter.ViewHolder> {

    //поля класса advertisingAndInformationRecyclerAdapter
    private LayoutInflater inflater;
    DBUtilities dbUtilities;
    String idAuthUser;
    Context context;
    Note note;
    List<Note> notes;

    //конструктор
    public AdvertisingAndInformationRecyclerAdapter(Context context, List<Note> notes, String idAuthUser) {
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
        this.context = context;
        this.idAuthUser = idAuthUser;
        dbUtilities = new DBUtilities(context);
    } // advertisingAndInformationRecyclerAdapter

    //создаем новую разметку(View) путем указания разметки
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_adapter_advertising_and_informationa, parent, false);
        return new ViewHolder(view);
    }

    //привязываем элементы разметки к переменным объекта(в данном случае к курсору)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        note = notes.get(position); // переходим в курсоре на текущую позицию

//        holder.ivLogoAdAvInReAd.setText(note.getNoteHead());     //лого
        new DownloadImageTask(holder.ivLogoAdAvInReAd)
                .execute("http://strahovanie.dn.ua/football_db/logo/logo_" + note.getNoteLogo() + ".png");
        holder.tvHeadAdAvInReAd.setText(Html.fromHtml("<u>" + note.getNoteHead() + "</u>"));     //заголовок
        holder.tvDateAdAvInReAd.setText(note.getNoteDate());     //дата
        holder.tvCityAdAvInReAd.setText(note.getNoteCityName()); //город
        holder.tvMessageAdAvInReAd.setText(note.getNoteMessage());     //сообшение
        //настраиваем EditText не редактируемые
        holder.tvHeadAdAvInReAd.setKeyListener(null);
        holder.tvMessageAdAvInReAd.setKeyListener(null);

        //Получаем размер установленного шрифта
        switch (note.getNoteTextSizeMessage()) {
            case "0":  holder.tvMessageAdAvInReAd.setTextSize(14); break;
            case "1":  holder.tvMessageAdAvInReAd.setTextSize(16); break;
            case "2":  holder.tvMessageAdAvInReAd.setTextSize(18); break;
            case "3":  holder.tvMessageAdAvInReAd.setTextSize(20); break;
        }//switch

        //Получаем размер установленного типа шрифта
        switch (note.getNoteTextStyleMessage()) {
            case "0": holder.tvMessageAdAvInReAd.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL)); break;
            case "1": holder.tvMessageAdAvInReAd.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD)); break;
            case "2": holder.tvMessageAdAvInReAd.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC)); break;
            case "3": holder.tvMessageAdAvInReAd.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC)); break;
        }//switch

        //опредиляемся с цветом CardView взависимости прав для редактирования поста
        if(note.getNoteUserId().equals(idAuthUser))
            holder.cvMainAdAvInReAd.setCardBackgroundColor(
                    context.getResources().getColor(R.color.colorMyColorGold));
        else holder.cvMainAdAvInReAd.setCardBackgroundColor(
                context.getResources().getColor(R.color.colorMyColorGrey));

    } // onBindViewHolder

    //получаем количество элементов объекта(курсора)
    @Override
    public int getItemCount() { return notes.size(); }

    //Создаем класс ViewHolder с помощью которого мы получаем ссылку на каждый элемент
    //отдельного пункта списка
    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvHeadAdAvInReAd, tvDateAdAvInReAd, tvCityAdAvInReAd, tvMessageAdAvInReAd;
              ImageView ivLogoAdAvInReAd;
              CardView cvMainAdAvInReAd;

        @SuppressLint("WrongViewCast")
        ViewHolder(View view){
            super(view);

            ivLogoAdAvInReAd = view.findViewById(R.id.ivLogoAdAvInReAd);
            tvHeadAdAvInReAd = view.findViewById(R.id.tvHeadAdAvInReAd);
            tvDateAdAvInReAd = view.findViewById(R.id.tvDateAdAvInReAd);
            tvCityAdAvInReAd = view.findViewById(R.id.tvCityAdAvInReAd);
            tvMessageAdAvInReAd = view.findViewById(R.id.tvMessageAdAvInReAd);
            cvMainAdAvInReAd = view.findViewById(R.id.cvMainAdAvInReAd);

            cvMainAdAvInReAd.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //получаем данные о нажатом событии
                    Note note = notes.get(getAdapterPosition());

                    if(note.getNoteUserId().equals(idAuthUser)) {
                        //редактируем рекламный пост
                        editAdvertising(note);
                    }else Toast.makeText(context, "Выберите ваш рекламный пост!", Toast.LENGTH_SHORT).show();
                    return true;
                }//onLongClick
            });//setOnLongClickListener
        } // ViewHolder
    } // class ViewHolder

    private void editAdvertising(Note note) {
        Intent intent = new Intent(context, EditNewsNoteActivity.class);
        intent.putExtra("idAuthUser", idAuthUser);
        intent.putExtra("note", note);
        context.startActivity(intent);
    }//editAdvertising
}//advertisingAndInformationRecyclerAdapter
