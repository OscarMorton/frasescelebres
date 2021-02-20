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
import ml.oscarmorton.frasescelebres.interfacess.listeners.ICategoriaListener;
import ml.oscarmorton.frasescelebres.interfacess.listeners.IFrasesListener;
import ml.oscarmorton.frasescelebres.model.Autor;
import ml.oscarmorton.frasescelebres.model.Categoria;
import ml.oscarmorton.frasescelebres.model.Frase;

public class AdaptorCategoria extends RecyclerView.Adapter<AdaptorCategoria.CategoriaViewHolder> {
    private ArrayList<Categoria> categorias;
    private ICategoriaListener listener;

    public AdaptorCategoria(ArrayList<Categoria> datos, ICategoriaListener listener) {
        this.categorias = datos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoria_item_container, parent, false);
        return new CategoriaViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);
        holder.bindFrase(categoria);

    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }


    public static class CategoriaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvIdCategoria;
        private TextView tvNombreCategoria;


        private ICategoriaListener listener;



        public CategoriaViewHolder(@NonNull View itemView, ICategoriaListener listener) {
            super(itemView);
            tvIdCategoria = itemView.findViewById(R.id.tvIdCategoria);
            tvNombreCategoria = itemView.findViewById(R.id.tvNombreCategoria);


            this.listener = listener;
            itemView.setOnClickListener(this);

        }

        public void bindFrase(Categoria categoria){
            tvIdCategoria.setText(String.valueOf(categoria.getId()));
            tvNombreCategoria.setText(String.valueOf(categoria.getNombre()));


        }

        @Override
        public void onClick(View v) {
            if(listener != null) {
                listener.onCategoriaSelected(getAdapterPosition());
            }
        }
    }
}
