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
        Calendar c = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    date = String.format(
                            java.util.Locale.getDefault(),
                            "%02d/%02d/%04d",
                            day, month + 1, year
                    );
                    btnDate.setText(date);
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    private void saveItem() {
        String name = etItemName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = spCategory.getSelectedItem().toString();
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

        String type;
        if (radioGroupType.getCheckedRadioButtonId() == R.id.radioLost) {
            type = "Lost";
        } else {
            type = "Found";
        }

        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("item_name", name);
        values.put("description", description);
        values.put("category", category);
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
    }
}
