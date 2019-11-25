package br.ufrpe.wanderlustapp.musicaLocal.gui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import br.ufrpe.wanderlustapp.R;
import br.ufrpe.wanderlustapp.cidade.dominio.Cidade;
import br.ufrpe.wanderlustapp.cidade.negocio.CidadeServices;
import br.ufrpe.wanderlustapp.infra.Sessao;
import br.ufrpe.wanderlustapp.musicaLocal.dominio.MusicaLocal;
import br.ufrpe.wanderlustapp.pais.dominio.Pais;
import br.ufrpe.wanderlustapp.pais.negocio.PaisServices;

public class CadastraMusicasActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR_INSERE = "Inserir banda/cantor(a)";
    CidadeServices cidadeServices = new CidadeServices(this);
    PaisServices paisServices = new PaisServices(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_musicas);
        setTitle(TITULO_APPBAR_INSERE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_musica_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_formulario_musica_ic_salva){
            MusicaLocal musicaLocal = criaMusicaLocal();
            if(verficaCampos()) {
                Sessao.instance.setMusicaLocal(musicaLocal);
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private MusicaLocal criaMusicaLocal() {
        MusicaLocal musicaLocal = new MusicaLocal();
        if (verficaCampos()){
            preencheAtributosMusica(musicaLocal);
        }
        return musicaLocal;
    }

    private boolean verficaCampos(){
        EditText nome = findViewById(R.id.formulario_musica_nome);
        EditText descricao = findViewById(R.id.formulario_musica_descricao);
        return nome.length() > 0 && descricao.length() > 0;
    }

    private void preencheAtributosMusica(MusicaLocal musicaLocal) {
        EditText nome = findViewById(R.id.formulario_musica_nome);
        EditText descricao = findViewById(R.id.formulario_musica_descricao);
        musicaLocal.setNome(nome.getText().toString());
        musicaLocal.setDescricao(descricao.getText().toString());
        musicaLocal.setCidade(createCidadePadrao());
    }

    private Cidade createCidadePadrao() {
        Pais pais = new Pais();
        pais.setNome("Brasil");
        paisServices.cadastrar(pais);
        Cidade cidade = new Cidade();
        cidade.setNome("Recife");
        cidade.setPais(pais);
        cidadeServices.cadastrar(cidade);
        return cidade;
    }


}
