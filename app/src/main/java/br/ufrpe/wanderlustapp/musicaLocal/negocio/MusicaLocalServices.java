package br.ufrpe.wanderlustapp.musicaLocal.negocio;

import android.content.Context;

import java.util.List;

import br.ufrpe.wanderlustapp.musicaLocal.dominio.MusicaLocal;
import br.ufrpe.wanderlustapp.musicaLocal.persistencia.MusicaLocalDAO;

public class MusicaLocalServices {

    private MusicaLocalDAO musicaLocalDAO;

    public MusicaLocalServices(Context context) { musicaLocalDAO = new MusicaLocalDAO(context); }

    public void cadastrar(MusicaLocal musicaLocal) throws Exception {
        if (musicaLocalDAO.getMusicaLocalByNome(musicaLocal.getNome()) != null){
            throw new Exception();
        }
        long idMusica = musicaLocalDAO.cadastrar(musicaLocal);
        musicaLocal.setId(idMusica);
    }

    public List<MusicaLocal> getLista(){
        return musicaLocalDAO.getListMusicaLocal();
    }

}
