package br.ufrpe.wanderlustapp.musicaLocal.gui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.ufrpe.wanderlustapp.R;
import br.ufrpe.wanderlustapp.infra.Sessao;
import br.ufrpe.wanderlustapp.musicaLocal.dominio.MusicaLocal;
import br.ufrpe.wanderlustapp.musicaLocal.gui.adapter.ListMusicasAdapter;
import br.ufrpe.wanderlustapp.musicaLocal.negocio.MusicaLocalServices;

public class ListaMusicasActivity extends AppCompatActivity {

    MusicaLocalServices musicaLocalServices = new MusicaLocalServices(this);
    public static final String TITULO_APPBAR_LISTA = "Lista de bandas/cantor(a)";
    private ListMusicasAdapter adapter;
    private int posicaoEnviada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_musicas);
        setTitle(TITULO_APPBAR_LISTA);
        configuraRecyclerview();
        configuraBtnInsereMusica();
    }

    private void configuraBtnInsereMusica() {
        TextView btnInsereMusica = findViewById(R.id.lista_musicas_insere_musica);
        btnInsereMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListaMusicasActivity.this, CadastraMusicasActivity.class));
            }
        });
    }

    private void insereMusica(MusicaLocal musicaLocal) {
        try {
            musicaLocalServices.cadastrar(musicaLocal);
            adapter.adiciona(musicaLocal);
            Toast.makeText(getApplicationContext(), "Banda/cantor(a) cadastrado", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Banda/cantor(a) já cadastrado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        MusicaLocal musicaLocal = Sessao.instance.getMusicaLocal();
        if (musicaLocal != null){
            if (musicaLocal.getId() == 0){
                insereMusica(musicaLocal);
                Sessao.instance.resetMusica();
            }
        }
        super.onResume();
    }

    private List<MusicaLocal> geraLista(){
        return musicaLocalServices.getLista();
    }

    private void setAdapter (RecyclerView recyclerView){
        adapter = new ListMusicasAdapter(this,geraLista());
        recyclerView.setAdapter(adapter);

    }


    private void configuraRecyclerview() {
        RecyclerView listaMusicas = findViewById(R.id.lista_musicas_recyclerview);
        setAdapter(listaMusicas);
    }

}
