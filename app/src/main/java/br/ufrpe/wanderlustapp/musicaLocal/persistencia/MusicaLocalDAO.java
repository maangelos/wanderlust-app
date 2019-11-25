package br.ufrpe.wanderlustapp.musicaLocal.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.ufrpe.wanderlustapp.cidade.persistencia.CidadeDAO;
import br.ufrpe.wanderlustapp.infra.persistencia.AbstractDAO;
import br.ufrpe.wanderlustapp.infra.persistencia.DBHelper;
import br.ufrpe.wanderlustapp.musicaLocal.dominio.MusicaLocal;

public class MusicaLocalDAO extends AbstractDAO {

    private SQLiteDatabase db;
    private DBHelper helper;
    private Context context;

    public MusicaLocalDAO(Context context) {
        this.context = context;
        helper = new DBHelper(context);
    }

    private ContentValues getContentValues(MusicaLocal musicaLocal) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.CAMPO_FK_CIDADE_MUSICA, musicaLocal.getCidade().getId());
        values.put(DBHelper.CAMPO_NOME_MUSICA, musicaLocal.getNome());
        values.put(DBHelper.CAMPO_DESCRICAO_MUSICA, musicaLocal.getDescricao());
        return values;
    }

    private Cursor getCursor(List<MusicaLocal> musicaLocal, String sql) {
        Cursor cursor = db.rawQuery(sql, new String[]{});
        while (cursor.moveToNext()){
            musicaLocal.add(createMusicaLocal(cursor));
        }
        return cursor;
    }

    private void setsMusicaLocal(Cursor cursor, MusicaLocal musicaLocal, CidadeDAO cidadeDAO) {
        int columnIndex = cursor.getColumnIndex(DBHelper.CAMPO_ID_MUSICA);
        musicaLocal.setId(Integer.parseInt(cursor.getString(columnIndex)));
        columnIndex = cursor.getColumnIndex(DBHelper.CAMPO_NOME_MUSICA);
        musicaLocal.setNome(cursor.getString(columnIndex));
        columnIndex = cursor.getColumnIndex(DBHelper.CAMPO_DESCRICAO_MUSICA);
        musicaLocal.setDescricao(cursor.getString(columnIndex));
        columnIndex = cursor.getColumnIndex(DBHelper.CAMPO_FK_CIDADE_MUSICA);
        musicaLocal.setCidade(cidadeDAO.getCidade(cursor.getInt(columnIndex)));
    }

    public MusicaLocal getMusicaLocalById(long id) {
        MusicaLocal musicaLocal = null;
        db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + DBHelper.TABELA_MUSICA + " WHERE " + DBHelper.CAMPO_ID_MUSICA + " LIKE ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{Long.toString(id)});
        musicaLocal = getMusicaLocal(musicaLocal, cursor);
        super.close(cursor, db);
        return musicaLocal;
    }

    private MusicaLocal getMusicaLocal(MusicaLocal musicaLocal, Cursor cursor) {
        if (cursor.moveToFirst()) {
            musicaLocal = createMusicaLocal(cursor);
        }
        return musicaLocal;
    }

    public MusicaLocal getMusicaLocalByNome(String nome) {
        MusicaLocal musicaLocal = null;
        db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + DBHelper.TABELA_MUSICA + " WHERE " + DBHelper.CAMPO_NOME_MUSICA + " LIKE ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{nome});
        musicaLocal = getMusicaLocal(musicaLocal, cursor);
        super.close(cursor, db);
        return musicaLocal;
    }

    public List<MusicaLocal> getListMusicaLocal(){
        List<MusicaLocal> musicas = new ArrayList<MusicaLocal>();
        db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + DBHelper.TABELA_MUSICA;
        Cursor cursor = getCursor(musicas, sql);
        cursor.close();
        db.close();
        return musicas;
    }

    private MusicaLocal createMusicaLocal(Cursor cursor) {
        MusicaLocal musicaLocal = new MusicaLocal();
        CidadeDAO cidadeDAO = new CidadeDAO(context);
        setsMusicaLocal(cursor, musicaLocal, cidadeDAO);
        return musicaLocal;
    }

    public long cadastrar(MusicaLocal musicaLocal){
        db = helper.getWritableDatabase();
        ContentValues values = getContentValues(musicaLocal);
        long id = db.insert(DBHelper.TABELA_MUSICA,null,values);
        super.close(db);
        return id;
    }

}
