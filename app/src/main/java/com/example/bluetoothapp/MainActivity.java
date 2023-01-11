package com.example.bluetoothapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    CheckBox enable, visible;
    ListView lv;
    TextView name;
    Button search;
    public static final int BT_E=0;
    public static final int BT_D=1;
    private BluetoothAdapter ba;
    private Set<BluetoothDevice> devices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enable = findViewById(R.id.checkBox);
        visible = findViewById(R.id.checkBox2);
        lv = findViewById(R.id.listview);
        name = findViewById(R.id.textView);
        search = findViewById(R.id.button);
        visible.setVisibility(View.INVISIBLE);
        search.setVisibility(View.INVISIBLE);
        ba = BluetoothAdapter.getDefaultAdapter();
        if(ba==null){
            Toast.makeText(this, "No Bluetooth Found", Toast.LENGTH_SHORT).show();
        }

        else if(ba.isEnabled()){
            visible.setVisibility(View.VISIBLE);
            search.setVisibility(View.VISIBLE);
            enable.setChecked(true);
        }

        if(ba!=null){
            String myname = ba.getName();
            if(myname.equals(null)){
                myname = ba.getAddress();
            }
            name.setText(myname);

        }
        enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    ba.disable();
                    visible.setVisibility(View.INVISIBLE);
                    search.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Turning Off Bluetooth", Toast.LENGTH_SHORT).show();

                }
                else{
                    Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(i,BT_E);

                }
            }
        });
        visible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    //Toast.makeText(MainActivity.this, "Please Turn Off Bluetooth", Toast.LENGTH_SHORT).show();

                }
                else{
                    Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(i,BT_D);
                    Toast.makeText(MainActivity.this, "Turning on Visibility", Toast.LENGTH_SHORT).show();
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                devices = ba.getBondedDevices();
                ArrayList list = new ArrayList();

                for(BluetoothDevice ba : devices){
                    list.add(ba.getName());

                }

                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, list);
                lv.setAdapter(adapter);
            }
        });
    }

    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case BT_E:
                if(resultCode==RESULT_OK){
                    enable.setChecked(true);
                    visible.setVisibility(View.VISIBLE);
                    search.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Turned On Bluetooth", Toast.LENGTH_SHORT).show();
                }
                else{
                    enable.setChecked(false);
                    Toast.makeText(MainActivity.this, "Permission Denied for Bluetooth", Toast.LENGTH_SHORT).show();
                }
                break;
            case BT_D:
                Log.d("viis", String.valueOf(resultCode));
                if(resultCode==120){
                    visible.setChecked(true);
                    Toast.makeText(MainActivity.this, "Turned On Visibility for 120s", Toast.LENGTH_SHORT).show();
                }
                else{
                    visible.setChecked(false);
                    Toast.makeText(MainActivity.this, "Permission Denied for Visibility", Toast.LENGTH_SHORT).show();
                }
                break;
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}