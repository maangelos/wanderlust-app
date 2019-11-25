package br.ufrpe.wanderlustapp.musicaLocal.gui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.ufrpe.wanderlustapp.R;
import br.ufrpe.wanderlustapp.musicaLocal.dominio.MusicaLocal;
import br.ufrpe.wanderlustapp.musicaLocal.gui.ListaMusicasActivity;
import br.ufrpe.wanderlustapp.musicaLocal.gui.OnItemClickListener;

public class ListMusicasAdapter extends RecyclerView.Adapter<ListMusicasAdapter.MusicaViewHolder>{

    private final Context context;
    private final List<MusicaLocal> musicas;
    private OnItemClickListener onItemClickListener;
    private ListaMusicasActivity listaMusicas = new ListaMusicasActivity();

    public ListMusicasAdapter(Context context,List<MusicaLocal> musicas) {
        this.context = context;
        this.musicas = musicas;
    }

    public List<MusicaLocal> getList(){
        return this.musicas;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ListMusicasAdapter.MusicaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.item_musica, parent, false);
        return new MusicaViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicaViewHolder holder, int position) {
        MusicaLocal musica = musicas.get(position);
        holder.vincula(musica);
    }

    @Override
    public int getItemCount() {
        return musicas.size();
    }


    public void adiciona(MusicaLocal musicaLocal){
        musicas.add(musicaLocal);
        notifyDataSetChanged();
    }

    class MusicaViewHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final TextView descricao;
        private MusicaLocal musicaLocal;

        public MusicaViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_musica_nome);
            descricao = itemView.findViewById(R.id.item_musica_descricao);
        }

        public void vincula(MusicaLocal musicaLocal) {
            this.musicaLocal = musicaLocal;
            titulo.setText(this.musicaLocal.getNome());
            descricao.setText(this.musicaLocal.getDescricao());
            }
        }

    }

