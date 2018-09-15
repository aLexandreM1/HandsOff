package handsoff.handsoff.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import handsoff.handsoff.DAO.ConfiguracaoFirebase;
import handsoff.handsoff.R;
import handsoff.handsoff.domain.Usuario;
import handsoff.handsoff.util.MaskEditUtil;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtNome, edtEmail, edtSenha, edtTelefone, edtCelular, edtCidade;
    private static final String TAG = "";
    private Usuario usuario = new Usuario();
    private DatabaseReference reference;
    private FirebaseAuth auth;
    boolean flag = false;
    boolean flagSnapshot = false;
    TextInputLayout textInputLayout;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Cadastrar um novo contato");

        edtNome = findViewById(R.id.edtName);
        edtSenha = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtTelefone = findViewById(R.id.edtPhone);
        edtTelefone.addTextChangedListener(MaskEditUtil.mask(edtTelefone, MaskEditUtil.FORMAT_PHONE));
        edtCelular = findViewById(R.id.edtCellPhone);
        edtCelular.addTextChangedListener(MaskEditUtil.mask(edtCelular, MaskEditUtil.FORMAT_CELLPHONE));
        edtCidade = findViewById(R.id.edtCity);

        textInputLayout = findViewById(R.id.txtInputLayoutPassword);

        textInputLayout.setPasswordVisibilityToggleEnabled(true);

        AutoCompleteTextView completeTextView = (AutoCompleteTextView) findViewById(R.id.edtCity);
        String[] states = getResources().getStringArray(R.array.states_array);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, states);
        completeTextView.setAdapter(arrayAdapter);

        auth = ConfiguracaoFirebase.getFirebaseAuth();

        reference = FirebaseDatabase.getInstance().getReference("usuarios");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean exist = snapshot.hasChildren();
                //Verifica se existe usuário no database
                flagSnapshot = !exist;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(RegisterActivity.this, "Erro de conexão com o banco de dados, tente cadastrar novamente por favor.", Toast.LENGTH_LONG).show();
                Log.v(TAG,"Erro: " + error);
            }
        });

    }

    public void clearForm(View view) {
        edtNome.getText().clear();
        edtSenha.getText().clear();
        edtEmail.getText().clear();
        edtTelefone.getText().clear();
        edtCelular.getText().clear();
        edtCidade.getText().clear();

        edtNome.requestFocus();
    }

    //Validação exclusiva para e-mail
    private boolean isEmail(EditText text) {
        CharSequence chrEmail = text.getText().toString();
        return (!TextUtils.isEmpty(chrEmail) && Patterns.EMAIL_ADDRESS.matcher(chrEmail).matches());
    }

    private void validateForm() {

        flag = false;

        if (edtNome.getText().toString().isEmpty()) {
            edtNome.setError("O campo nome não pode ficar em branco.");
            flag = true;
        }
        if (edtSenha.getText().toString().isEmpty()) {
            textInputLayout.setPasswordVisibilityToggleEnabled(false);
            edtSenha.setError("O campo senha não pode ficar em branco.");
            flag = true;
        }
        if (!isEmail(edtEmail)) {
            edtEmail.setError("Insira um e-mail válido. (XXX@XXXX.com");
            flag = true;
        }
        if (edtTelefone.getText().toString().isEmpty()) {
            edtTelefone.setError("O campo telefone não pode ficar em branco.");
            flag = true;
        }
        if (edtCelular.getText().toString().isEmpty()) {
            edtCelular.setError("O campo celular não pode ficar em branco.");
            flag = true;
        }
        if (edtCidade.getText().toString().isEmpty()) {
            edtCidade.setError("O campo cidade não pode ficar em branco");
            flag = true;
        }
    }

    public void saveContact(View view) {
        validateForm();

        if (!flag) {
            usuario.setNome(edtNome.getText().toString());
            usuario.setSenha(edtSenha.getText().toString());
            usuario.setEmail(edtEmail.getText().toString());
            usuario.setTelefone(edtTelefone.getText().toString());
            usuario.setCelular(edtCelular.getText().toString());
            usuario.setCidade(edtCidade.getText().toString());

            //Manter os dados sincronizados com o Firebase Realtime.
            reference = FirebaseDatabase.getInstance().getReference("usuarios");
            reference.keepSynced(true);

            auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                insertingContact(usuario);

                                Toast.makeText(RegisterActivity.this, "Usuário cadastrado com sucesso.", Toast.LENGTH_LONG).show();

                                //Depois que salva o usuário vai para a tela de login.
                                /*Intent toLogin = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(toLogin);
                                finish();*/
                            } else {

                                String erroExecucao = "";

                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    erroExecucao = "Digite uma senha mais forte, contendo no mínimo 8 caracteres e que contenha letras e números";
                                    Log.v(TAG, e.toString());
                                } catch (FirebaseAuthUserCollisionException e) {
                                    erroExecucao = "Esse e-mail já está cadastrado!";
                                    Log.v(TAG, e.toString());
                                } catch (Exception e) {
                                    erroExecucao = "Erro ao caddastrar um novo usuário";
                                    Log.v(TAG, e.toString());
                                }

                                Toast.makeText(RegisterActivity.this, erroExecucao, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            clearForm(view);
            edtNome.requestFocus();
        }

    }

    private boolean insertingContact(Usuario usuario) {
        try {
            //Referencia um filho no database.
            reference = ConfiguracaoFirebase.getFirebase().child("usuarios");

            //Insere no firebase (o push() cria uma chave única, um Id para o registro).
            reference.push().setValue(usuario);
            Toast.makeText(this, "Usuário cadastrado com sucesso.", Toast.LENGTH_LONG).show();
            return true;
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao cadastrar usuário! Tente novamente por favor.", Toast.LENGTH_LONG).show();
            Log.v(TAG, "ERRO: " + e);
            return false;
        }
    }
}
