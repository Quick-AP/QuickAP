package com.quickap.quickap.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.quickap.quickap.R;
import com.quickap.quickap.databinding.ActivityMainBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


    /**
     * Utility to confirm if a input Stirng is a valid mobile number in China using Regular Expression.
     * Regex refered from: https://blog.csdn.net/cm519096/article/details/126370622.
     * @param mobile input string.
     * @return boolean identifying if the input string is a valid mobile number.
     */
    public static boolean isMobile(String mobile) {
        if (mobile == null) return false;
        String regex = "^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    /**
     * Get Phone number from the system as a String, returns null if failed.
     * @return phone number of the user's device, of null when failed.
     */
    private String getPhoneNumberFromSystem() {
        TelephonyManager telephonyManager =
                (TelephonyManager) getApplicationContext()
                .getSystemService(TELEPHONY_SERVICE);

        boolean permissionsNotGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED;

        if (permissionsNotGranted) {
            return null;
        }

        String phoneNumber = telephonyManager.getLine1Number();
        return isMobile(phoneNumber) ? phoneNumber : null;
    }


    /**
     * Display pop-up and save obtained phone number in a given String array.
     * @param phoneNumber Single element String array to save the user inputted phone number
     */
    private void getPhoneNumberFromUser(String[] phoneNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please input your phone number to begin using!");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) ->
                phoneNumber[0] = input.getText().toString());
        builder.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // Try getting phone number from system
        final String[] phoneNumber = {getPhoneNumberFromSystem()};
        // if failed to obtai, then get from user
        if (phoneNumber[0] == null || !isMobile(phoneNumber[0])) {
            getPhoneNumberFromUser(phoneNumber);
        }

        binding.jumpToMap.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), SecondFloor.class);
            Bundle bundle = new Bundle();
            bundle.putString("phoneNumber", phoneNumber[0]);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        binding.jumpToMenu.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MenuActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("phoneNumber", phoneNumber[0]);
            bundle.putInt("tableId", 1);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        binding.jumpToReservation.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ReservationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("phoneNumber", phoneNumber[0]);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}