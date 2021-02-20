package ml.oscarmorton.frasescelebres.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import ml.oscarmorton.frasescelebres.R;
import ml.oscarmorton.frasescelebres.interfacess.listeners.IAutorListener;
import ml.oscarmorton.frasescelebres.interfacess.listeners.IFrasesListener;
import ml.oscarmorton.frasescelebres.model.Autor;
import ml.oscarmorton.frasescelebres.model.Frase;

public class AdaptorAutor extends RecyclerView.Adapter<AdaptorAutor.FrasesViewHolder> {
    private ArrayList<Autor> autores;
    private IAutorListener listener;

    public AdaptorAutor(ArrayList<Autor> datos, IAutorListener listener) {
        this.autores = datos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FrasesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.autor_item_container, parent, false);
        return new FrasesViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FrasesViewHolder holder, int position) {
        Autor autor = autores.get(position);
        holder.bindFrase(autor);

    }

    @Override
    public int getItemCount() {
        return autores.size();
    }


    public static class FrasesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvIdAutor;
        private TextView tvNombreAutor;
        private TextView tvProfesionAutor;
        private TextView tvMueteAutor;
        private TextView tvNacimientoAutor;

        private IAutorListener listener;



        public FrasesViewHolder(@NonNull View itemView, IAutorListener listener) {
            super(itemView);
            tvIdAutor = itemView.findViewById(R.id.tvIdAutor);
            tvNombreAutor = itemView.findViewById(R.id.tvNombreAutor);
            tvProfesionAutor = itemView.findViewById(R.id.tvProfesion);
            tvNacimientoAutor = itemView.findViewById(R.id.tvNacimiento);
            tvMueteAutor = itemView.findViewById(R.id.tvMuerte);

            this.listener = listener;
            itemView.setOnClickListener(this);

        }

        public void bindFrase(Autor autor){
            tvIdAutor.setText(String.valueOf(autor.getId()));
            tvNombreAutor.setText(String.valueOf(autor.getNombre()));
            tvProfesionAutor.setText(String.valueOf(autor.getProfesion()));
            tvNacimientoAutor.setText(String.valueOf(autor.getNacimiento()));
            tvMueteAutor.setText(String.valueOf(autor.getMuerte()));

        }

        @Override
        public void onClick(View v) {
            if(listener != null) {
                listener.onAutorSelected(getAdapterPosition());
            }
        }
    }
}
