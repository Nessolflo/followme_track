package drive.tracker.register;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import drive.tracker.R;
import drive.tracker.domain.ConstantsTracker;
import drive.tracker.domain.permissions.PermissionObj;
import drive.tracker.domain.permissions.Permissions;
import drive.tracker.io.pojo.ResponseRegister;
import drive.tracker.service.ServiceLocation;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtId;
    private TextView lblId;
    private Button btnSend;
    private ProgressBar progressBar;
    private RegisterModel registerModel;
    private SharedPreferences sharedPreferences;

    private Permissions permissions;
    private PermissionObj permisoObj = new PermissionObj(new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION}, 123);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sharedPreferences = getApplication().getSharedPreferences(getPackageName(), MODE_PRIVATE);
        lblId = findViewById(R.id.lblId);
        txtId = findViewById(R.id.txtId);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        progressBar = findViewById(R.id.progress);
        registerModel = ViewModelProviders.of(this).get(RegisterModel.class);
        registerModel.init(getApplication());
        registerModel.getResponseData().observe(this, this::showResult);
        showId();

    }

    @Override
    public void onClick(View v) {
        if (v == btnSend) {
            progressBar.setVisibility(View.VISIBLE);
            registerModel.sendData(txtId.getText().toString(),
                    FirebaseInstanceId.getInstance().getToken());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(!this.permissions.onRequestPermissionsResult(requestCode, permissions, grantResults))
            Toast.makeText(this, getString(R.string.denied_permissions),
                    Toast.LENGTH_SHORT).show();
        else
            startLocationService();
    }

    private void showId() {
        final String tempId = sharedPreferences.getString(ConstantsTracker.KEY_ID, "");
        if (!tempId.isEmpty()) {
            lblId.setVisibility(View.VISIBLE);
            lblId.setText(String.format(getString(R.string.id_registered), tempId));
            requestPermissions();
        } else
            lblId.setVisibility(View.GONE);
    }

    private void showResult(ResponseRegister responseRegister) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConstantsTracker.KEY_ID, String.valueOf(responseRegister.id));
        editor.apply();
        editor.commit();
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, responseRegister.message, Toast.LENGTH_SHORT).show();
        txtId.setText("");
        showId();
    }

    public void startLocationService() {
        final Intent i = new Intent(this, ServiceLocation.class);
        if (ServiceLocation.INSTANCE != null)
            stopService(i);
        startService(i);
    }

    private void requestPermissions(){
        if(permissions==null)
            permissions = new Permissions(this, permisoObj);
        if(!permissions.comprobarPermisos()){
            permissions.solicitarPermisos();
        }else {
            startLocationService();
        }
    }
}
