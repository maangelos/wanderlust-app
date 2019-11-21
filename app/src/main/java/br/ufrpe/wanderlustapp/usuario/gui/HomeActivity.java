package br.ufrpe.wanderlustapp.usuario.gui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.ufrpe.wanderlustapp.R;
import br.ufrpe.wanderlustapp.infra.Sessao;
import br.ufrpe.wanderlustapp.pessoaPrato.dominio.PessoaPrato;
import br.ufrpe.wanderlustapp.pessoaPrato.negocio.PessoaPratoServices;
import br.ufrpe.wanderlustapp.pratoTipico.dominio.PratoTipico;
import br.ufrpe.wanderlustapp.pratoTipico.gui.DetalhesPratoActivity;
import br.ufrpe.wanderlustapp.pratoTipico.gui.ListaPratosActivity;
import br.ufrpe.wanderlustapp.pratoTipico.gui.ListaPratosAvaliacao;
import br.ufrpe.wanderlustapp.pratoTipico.gui.ListaPratosFavoritos;
import br.ufrpe.wanderlustapp.pratoTipico.gui.OnItemClickListener;
import br.ufrpe.wanderlustapp.pratoTipico.gui.adapter.ListaPratosAvaliacaoAdapter;
import br.ufrpe.wanderlustapp.pratoTipico.negocio.PratoTipicoServices;
import br.ufrpe.wanderlustapp.usuario.dominio.Usuario;
import br.ufrpe.wanderlustapp.usuario.gui.adapter.ListaPratosRecomendadosAdapter;

public class HomeActivity extends AppCompatActivity {
    PratoTipicoServices pratoTipicoServices = new PratoTipicoServices(this);
    PessoaPratoServices pessoaPratoServices = new PessoaPratoServices(this);
    PessoaPrato pessoaPrato = new PessoaPrato();
    private ListaPratosRecomendadosAdapter adapter;
    private Usuario usuario  = Sessao.instance.getUsuario();
    RecyclerView recyclerView;
    ArrayList<String> Tela;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    RecyclerViewAdapter RecyclerViewHorizontalAdapter;
    LinearLayoutManager HorizontalLayout ;
    View ChildView ;
    int RecyclerViewItemPosition ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        configuraRecyclerviewSlopeOne();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview1);
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);
        AddItemsToRecyclerViewArrayList();
        RecyclerViewHorizontalAdapter = new RecyclerViewAdapter(Tela);
        HorizontalLayout = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView.setAdapter(RecyclerViewHorizontalAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(HomeActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {
                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(ChildView);
                    defineIntent();
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }
    protected void onResume() {
        super.onResume();
        Sessao.instance.resetPrato();
        Sessao.instance.resetImagem();
    }

    private void defineIntent() {
        if(RecyclerViewItemPosition == 0){
            Intent iniciarAvaliarPratos =
                    new Intent(HomeActivity.this, ListaPratosAvaliacao.class);
            startActivity(iniciarAvaliarPratos);
        }
        else if(RecyclerViewItemPosition == 1){
            Intent iniciarPratosFavoritos =
                    new Intent(HomeActivity.this, ListaPratosFavoritos.class);
            startActivity(iniciarPratosFavoritos);
        }
        else if(RecyclerViewItemPosition == 2){
            Intent iniciarGerenciarPrato =
                    new Intent(HomeActivity.this, ListaPratosActivity.class);
            startActivity(iniciarGerenciarPrato);
        }
        else if(RecyclerViewItemPosition == 3){
            Intent iniciarPerfil =
                    new Intent(HomeActivity.this, PerfilActivity.class);
            startActivity(iniciarPerfil);
        }else if(RecyclerViewItemPosition == 4){
            Sessao.instance.reset();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }

    }

    private void configuraRecyclerviewSlopeOne() {
        RecyclerView listaPratosRecomendados = findViewById(R.id.lista_slopeone_recyclerview);
        setAdapterRecomendados(listaPratosRecomendados);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(PratoTipico pratoTipico, int posicao) {
                Sessao.instance.setPratoTipico(pratoTipico);
                startActivity(new Intent(HomeActivity.this, DetalhesPratoActivity.class));
            }

            @Override
            public void onItemClick(PratoTipico pratoTipico, int posicao, boolean isChecked) {
            }

            @Override
            public void onItemClick(PratoTipico pratoTipico, int posicao, boolean like, boolean dislike) {
                if (like){
                    likePessoaPrato(pratoTipico);
                }else if(dislike){
                    dislikePessoaPrato(pratoTipico);
                }else if(!like && !dislike){
                    zeraNota(pratoTipico);
                }
            }
        });
    }

    private PessoaPrato getPessoaPrato(PratoTipico pratoTipico){
        pessoaPrato = pessoaPratoServices.getPessoaPrato(usuario.getPessoa().getId(), pratoTipico.getId());
        if (pessoaPrato == null){
            pessoaPrato = new PessoaPrato();
            pessoaPrato.setPratoTipico(pratoTipico);
            pessoaPrato.setPessoa(usuario.getPessoa());
        }
        return pessoaPrato;
    }

    private void likePessoaPrato(PratoTipico prato) {
        pessoaPrato = getPessoaPrato(prato);
        pessoaPrato.setNota(1);
        try {
            if (pessoaPrato.getId() == 0) {
                pessoaPratoServices.cadastrar(pessoaPrato);
                Toast.makeText(HomeActivity.this, "Você curtiu: " + prato.getNome(), Toast.LENGTH_LONG).show();
            }else {
                pessoaPratoServices.update(pessoaPrato);
                Toast.makeText(HomeActivity.this, "Você curtiu2: " + prato.getNome(), Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void dislikePessoaPrato(PratoTipico prato) {
        pessoaPrato = getPessoaPrato(prato);
        pessoaPrato.setNota(-1);
        try {
            if (pessoaPrato.getId() == 0) {
                pessoaPratoServices.cadastrar(pessoaPrato);
                Toast.makeText(HomeActivity.this, "Você não gostou de: " + prato.getNome(), Toast.LENGTH_LONG).show();
            }else{
                pessoaPratoServices.update(pessoaPrato);
                Toast.makeText(HomeActivity.this, "Você não gostou de2: " + prato.getNome(), Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private  void zeraNota(PratoTipico pratoTipico){
        pessoaPrato = getPessoaPrato(pratoTipico);
        pessoaPrato.setNota(0);
        try {
            if (pessoaPrato.getId() == 0) {
                pessoaPratoServices.cadastrar(pessoaPrato);
            } else {
                pessoaPratoServices.update(pessoaPrato);
            }
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapterRecomendados(RecyclerView recyclerView) {
        adapter = new ListaPratosRecomendadosAdapter(this,geraListaFavoritos());
        recyclerView.setAdapter(adapter);
    }

    private List<PratoTipico> geraListaFavoritos(){
        return pratoTipicoServices.getLista();
    }


    public void AddItemsToRecyclerViewArrayList(){
        Tela = new ArrayList<>();
        Tela.add("Avaliar prato");
        Tela.add("Pratos favoritos");
        Tela.add("Gerenciar pratos");
        Tela.add("Perfil");
        Tela.add("Sair");
    }
}