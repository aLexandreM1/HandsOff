package handsoff.handsoff.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import handsoff.handsoff.DAO.UsuarioDAO;
import handsoff.handsoff.R;
import handsoff.handsoff.domain.Usuario;
import handsoff.handsoff.util.MaskEditUtil;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtNome, edtEmail, edtSenha, edtTelefone, edtCelular, edtCidade;
    private static final String TAG = "LOG REGISTER ACTIVITY: ";
    private Usuario usuario = new Usuario();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    boolean flag = false;
    private TextInputLayout textInputLayout;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Modificando o titulo da activity
        getSupportActionBar().setTitle("Cadastrar um novo contato");

        //Views
        edtNome = findViewById(R.id.edtName);
        edtSenha = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtTelefone = findViewById(R.id.edtPhone);
        edtTelefone.addTextChangedListener(MaskEditUtil.mask(edtTelefone, MaskEditUtil.FORMAT_PHONE));
        edtCelular = findViewById(R.id.edtCellPhone);
        edtCelular.addTextChangedListener(MaskEditUtil.mask(edtCelular, MaskEditUtil.FORMAT_CELLPHONE));
        edtCidade = findViewById(R.id.edtCity);
        textInputLayout = findViewById(R.id.txtInputLayoutPassword);

        //Manipulação do toggle
        textInputLayout.setPasswordVisibilityToggleEnabled(true);

        //Autocomplete de cidades
        AutoCompleteTextView completeTextView = (AutoCompleteTextView) findViewById(R.id.edtCity);
        String[] states = getResources().getStringArray(R.array.states_array);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, states);
        completeTextView.setAdapter(arrayAdapter);

    }

    //Limpar o formulário
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

    //Demais validações
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

    //Cadastrar novo usuário
    public void saveUser(View view) {
        validateForm();
        if (!flag) {
            usuario.setNome(edtNome.getText().toString());
            usuario.setSenha(edtSenha.getText().toString());
            usuario.setEmail(edtEmail.getText().toString());
            usuario.setTelefone(edtTelefone.getText().toString());
            usuario.setCelular(edtCelular.getText().toString());
            usuario.setCidade(edtCidade.getText().toString());

            //Chamando método para criar um novo usuário
            usuarioDAO.onCreateUser(usuario, this);

            clearForm(view);
            edtNome.requestFocus();
        }
    }
}
