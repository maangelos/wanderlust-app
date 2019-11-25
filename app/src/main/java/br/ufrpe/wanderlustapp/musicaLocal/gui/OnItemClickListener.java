package br.ufrpe.wanderlustapp.musicaLocal.gui;

import br.ufrpe.wanderlustapp.musicaLocal.dominio.MusicaLocal;

public interface OnItemClickListener {

    void onItemClick(MusicaLocal musicaLocal, int posicao);
    void onItemClick(MusicaLocal musicaLocal, int posicao, boolean isChecked);
    void onItemClick(MusicaLocal musicaLocal, int posicao, boolean likeChecked, boolean dislikeChecked);
}