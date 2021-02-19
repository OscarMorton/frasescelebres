package ml.oscarmorton.frasescelebres.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import ml.oscarmorton.frasescelebres.R;
import ml.oscarmorton.frasescelebres.listeners.IFrasesListener;
import ml.oscarmorton.frasescelebres.model.Frase;

public class AdaptorFrases extends RecyclerView.Adapter<AdaptorFrases.FrasesViewHolder> {
    private ArrayList<Frase> frases;
    private IFrasesListener listener;

    public AdaptorFrases(ArrayList<Frase> datos, IFrasesListener listener) {
        this.frases = datos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FrasesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.frase_item_conteiner, parent, false);

        return new FrasesViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FrasesViewHolder holder, int position) {
        Frase frase = frases.get(position);
        holder.bindFrase(frase);

    }

    @Override
    public int getItemCount() {
        return frases.size();
    }


    public static class FrasesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvIdFrase;
        private TextView tvTextFrase;
        private TextView tvNombreAutor;
        private TextView tvNombreCategoria;
        private IFrasesListener listener;



        public FrasesViewHolder(@NonNull View itemView, IFrasesListener listener) {
            super(itemView);
            tvIdFrase = itemView.findViewById(R.id.tvIdFrase);
            tvTextFrase = itemView.findViewById(R.id.tvTextFrase);
            tvNombreAutor = itemView.findViewById(R.id.tvNombreAutor);
            tvNombreCategoria = itemView.findViewById(R.id.tvNombreCategoria);

        }

        public void bindFrase(Frase frase){
            tvIdFrase.setText(String.valueOf(frase.getId()));
            tvTextFrase.setText(String.valueOf(frase.getTexto()));
            tvNombreAutor.setText(String.valueOf(frase.getAutor().getNombre()));

            tvNombreCategoria.setText(String.valueOf(frase.getCategoria().getNombre()));
        }

        @Override
        public void onClick(View v) {
            if(listener != null) {
                listener.onFraseSelected(getAdapterPosition());
            }
        }
    }
}