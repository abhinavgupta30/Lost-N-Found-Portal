package com.example.lostnfound;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {

    EditText etItemName, etDescription, etLocation, etContact;
    Spinner spCategory;
    Button btnDate, btnSubmit;
    RadioGroup radioGroupType;
    DBHelper db;

    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etItemName = findViewById(R.id.etItemName);
        etDescription = findViewById(R.id.etDescription);
        etLocation = findViewById(R.id.etLocation);
        etContact = findViewById(R.id.etContact);

        spCategory = findViewById(R.id.spCategory);
        btnDate = findViewById(R.id.btnDate);
        btnSubmit = findViewById(R.id.btnSubmit);
        radioGroupType = findViewById(R.id.radioGroupType);

        db = new DBHelper(this);

        String[] categories = {
                "Select Category",
                "Electronics",
                "Bags",
                "Documents",
                "Keys",
                "Clothing",
                "Accessories",
                "Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spCategory.setAdapter(adapter);

        btnDate.setOnClickListener(v -> selectDate());
        btnSubmit.setOnClickListener(v -> saveItem());
    }

    private void selectDate() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    date = String.format(
                            Locale.getDefault(),
                            "%02d/%02d/%04d",
                            day,
                            month + 1,
                            year
                    );

                    btnDate.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    private void saveItem() {
        String name = etItemName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String contact = etContact.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty()
                || location.isEmpty() || contact.isEmpty()) {
            Toast.makeText(this, "Please fill all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (spCategory.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a category",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (date.isEmpty()) {
            Toast.makeText(this, "Please select a date",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedType = radioGroupType.getCheckedRadioButtonId();

        String type;

        if (selectedType == R.id.radioLost) {
            type = "Lost";
        } else if (selectedType == R.id.radioFound) {
            type = "Found";
        } else {
            Toast.makeText(this, "Please select Lost or Found",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("item_name", name);
        values.put("description", description);
        values.put("category", spCategory.getSelectedItem().toString());
        values.put("location", location);
        values.put("date", date);
        values.put("type", type);
        values.put("contact", contact);
        values.put("status", "Pending");

        long result = database.insert("Items", null, values);

        if (result != -1) {
            Toast.makeText(this, "Item reported successfully!",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save item",
                    Toast.LENGTH_SHORT).show();
        }

        database.close();
    }
}
