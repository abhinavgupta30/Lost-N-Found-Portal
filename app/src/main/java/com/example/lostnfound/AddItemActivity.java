package com.example.lostnfound;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    private EditText etItemName, etItemDescription;
    private EditText etItemCategory, etItemLocation;
    private EditText etItemDate, etItemContact;
    private RadioGroup rgItemType;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etItemName = findViewById(R.id.etItemName);
        etItemDescription = findViewById(R.id.etItemDescription);
        etItemCategory = findViewById(R.id.etItemCategory);
        etItemLocation = findViewById(R.id.etItemLocation);
        etItemDate = findViewById(R.id.etItemDate);
        etItemContact = findViewById(R.id.etItemContact);

        rgItemType = findViewById(R.id.rgItemType);

        Button btnSubmitItem = findViewById(R.id.btnSubmitItem);
        Button btnCancelItem = findViewById(R.id.btnCancelItem);

        dbHelper = new DBHelper(this);

        btnSubmitItem.setOnClickListener(v -> saveItem());

        btnCancelItem.setOnClickListener(v -> finish());
    }

    private void saveItem() {

        String itemName = etItemName.getText().toString().trim();
        String description = etItemDescription.getText().toString().trim();
        String category = etItemCategory.getText().toString().trim();
        String location = etItemLocation.getText().toString().trim();
        String date = etItemDate.getText().toString().trim();
        String contact = etItemContact.getText().toString().trim();

        String type;

        if (rgItemType.getCheckedRadioButtonId() == R.id.rbLost) {
            type = "Lost";
        } else if (rgItemType.getCheckedRadioButtonId() == R.id.rbFound) {
            type = "Found";
        } else {
            Toast.makeText(this, "Please select Lost or Found",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (itemName.isEmpty() || description.isEmpty()
                || category.isEmpty() || location.isEmpty()
                || date.isEmpty() || contact.isEmpty()) {

            Toast.makeText(this, "Please fill all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.addItem(itemName, description, category,
                location, date, type, contact);

        if (success) {
            Toast.makeText(this, "Item reported successfully!",
                    Toast.LENGTH_SHORT).show();

            finish();
        } else {
            Toast.makeText(this, "Failed to report item",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
