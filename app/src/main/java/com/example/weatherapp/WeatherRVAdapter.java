package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder> {
    private Context context;
    ArrayList<WeatherRVModel> WeatherRVModelArrayList;

    public WeatherRVAdapter(Context context, ArrayList<WeatherRVModel> weatherRVModelArrayList) {
        this.context = context;
        WeatherRVModelArrayList = weatherRVModelArrayList;
    }

    @NonNull
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHolder holder, int position) {

        WeatherRVModel model = WeatherRVModelArrayList.get(position);
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");

        try{
            Date t = input.parse(model.getTime());
            holder.time.setText(output.format(t));
        }catch(ParseException e){
            e.printStackTrace();

        }
        holder.temp.setText(model.getTemp()+"°C");
        holder.wind.setText(model.getWindspeed()+"Km/h");


        Picasso.get().load("http".concat(model.getIcon())).into(holder.condition);
    }

    @Override
    public int getItemCount() {
        return WeatherRVModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView wind,temp,time;
        private ImageView condition;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            wind = itemView.findViewById(R.id.windspeed);
            temp = itemView.findViewById(R.id.idtemp);
            time = itemView.findViewById(R.id.idtime);
            condition = itemView.findViewById(R.id.imagecondition);

        }

    }
}
