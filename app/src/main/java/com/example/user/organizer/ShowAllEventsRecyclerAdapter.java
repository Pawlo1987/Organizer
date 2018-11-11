package com.example.user.organizer;

import android.content.Context;
import android.hardware.Sensor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

//------------------ Адаптор для элемента sensor -----------
public class ShowAllEventsRecyclerAdapter extends RecyclerView.Adapter<ShowAllEventsRecyclerAdapter.ViewHolder>{

    //поля класса SensorAdapter
    private LayoutInflater inflater;
    private List<Sensor> sensors;

    //конструктор
    ShowAllEventsRecyclerAdapter(Context context, List<Sensor> sensors) {
        this.inflater = LayoutInflater.from(context);
        this.sensors = sensors;
    } // SensorAdapter

    //создаем новую разметку(View) путем указания разметки
    @Override
    public ShowAllEventsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_adapter_show_all_events, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Sensor sensor = sensors.get(position); // переходим в курсоре на текущую позицию

        // получение данных
//        holder.tvNameAdapt.setText(sensor.getName().toString());
//        holder.tvTypeAdapt.setText(String.valueOf(sensor.getType()));
//        holder.tvVendorAdapt.setText(sensor.getVendor().toString());
//        holder.tvVersionAdapt.setText(String.valueOf(sensor.getVersion()));
//        holder.tvMaxValAdapt.setText(String.format("%.2f", sensor.getMaximumRange()));
//        holder.tvResolutionAdapt.setText(String.format("%.2f", sensor.getResolution()));

    } // onBindViewHolder

    @Override
    public int getItemCount() { return sensors.size(); }


    public class ViewHolder extends RecyclerView.ViewHolder {
//        final TextView tvNameAdapt, tvTypeAdapt, tvVendorAdapt,
//                tvVersionAdapt, tvMaxValAdapt, tvResolutionAdapt;

        ViewHolder(View view){
            super(view);

//            tvNameAdapt = (TextView) view.findViewById(R.id.tvNameAdapt);
//            tvTypeAdapt = (TextView) view.findViewById(R.id.tvTypeAdapt);
//            tvVendorAdapt = (TextView) view.findViewById(R.id.tvVendorAdapt);
//            tvVersionAdapt = (TextView) view.findViewById(R.id.tvVersionAdapt);
//            tvMaxValAdapt = (TextView) view.findViewById(R.id.tvMaxValAdapt);
//            tvResolutionAdapt = (TextView) view.findViewById(R.id.tvResolutionAdapt);
        } // ViewHolder
    } // class ViewHolder
}//SensorAdapter
