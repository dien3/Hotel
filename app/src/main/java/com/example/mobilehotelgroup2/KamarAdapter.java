package com.example.mobilehotelgroup2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
//fungi kamar adapter ini adalah adapter untuk recyclerview atau penampung dari recylerview
public class KamarAdapter extends RecyclerView.Adapter<KamarAdapter.KamarViewHolder>{
        List<Kamar> kamar;
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

public KamarAdapter(List<Kamar> kamar) {
        this.kamar = kamar;
        }
@Override
public KamarViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_kamar, viewGroup, false);
        KamarViewHolder kamarViewHolder = new KamarViewHolder(v);
        return kamarViewHolder;
        }
@Override
public void onBindViewHolder(KamarViewHolder kamarViewHolder, int i) {
        kamarViewHolder.kamarKodeHotel.setText(kamar.get(i).getKodeHotel());
        kamarViewHolder.kamarTypeKamar.setText(kamar.get(i).getTypeKamar());
        kamarViewHolder.kamarTglCheckIn.setText(kamar.get(i).getCheckin());
        kamarViewHolder.kamarTglCheckOut.setText(kamar.get(i).getCheckOut());
        kamarViewHolder.kamarHargaPerMalam.setText(formatRupiah.format((double)kamar.get(i).getHargaPerMalam()));
        }
@Override
public int getItemCount() {
        return kamar.size();
        }
public Kamar getItem(int position) {
        return kamar.get(position);
        }
@Override
public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        }

public static class KamarViewHolder extends RecyclerView.ViewHolder {
    CardView cv;
    TextView kamarKodeHotel;
    TextView kamarTypeKamar;
    TextView kamarTglCheckIn;
    TextView kamarTglCheckOut;
    TextView kamarHargaPerMalam;
    KamarViewHolder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cv);
        kamarKodeHotel = (TextView) itemView.findViewById(R.id.textViewKodeHotel);
        kamarTypeKamar = (TextView) itemView.findViewById(R.id.textViewTipeKamar);
        kamarTglCheckIn = (TextView) itemView.findViewById(R.id.textViewCekin);
        kamarTglCheckOut = (TextView) itemView.findViewById(R.id.textViewRowCekout);
        kamarHargaPerMalam = (TextView) itemView.findViewById(R.id.textViewHarga);
    }
}
}