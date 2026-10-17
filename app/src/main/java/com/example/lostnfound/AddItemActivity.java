package com.example.lostnfound;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {

    EditText etItemName, etDescription, etLocation, etContact;
    Spinner spCategory;
    Button btnDate, btnSubmit, btnChooseImage;
    ImageView imgItemPreview;
    RadioGroup radioGroupType;

    DBHelper db;
    String date = "";
    String imageUri = null;

    ActivityResultLauncher<String[]> imagePicker =
            registerForActivityResult(
                    new ActivityResultContracts.OpenDocument(),
                    uri -> {
                        if (uri != null) {
                            copyGalleryImage(uri);
                        }
                    }
            );

    ActivityResultLauncher<Void> cameraLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.TakePicturePreview(),
                    bitmap -> {
                        if (bitmap != null) {
                            saveCameraImage(bitmap);
                        }
                    }
            );

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
        btnChooseImage = findViewById(R.id.btnChooseImage);
        imgItemPreview = findViewById(R.id.imgItemPreview);
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

        updatePhotoButton();

        radioGroupType.setOnCheckedChangeListener(
                (group, checkedId) -> updatePhotoButton()
        );

        btnChooseImage.setOnClickListener(v -> openPhotoSource());

        btnSubmit.setOnClickListener(v -> saveItem());
    }

    private void updatePhotoButton() {

        int selectedType = radioGroupType.getCheckedRadioButtonId();

        if (selectedType == R.id.radioFound) {
            btnChooseImage.setText("Take Photo");
        } else if (selectedType == R.id.radioLost) {
            btnChooseImage.setText("Upload from Gallery");
        } else {
            btnChooseImage.setText("Choose Item Photo");
        }
    }

    private void openPhotoSource() {

        int selectedType = radioGroupType.getCheckedRadioButtonId();

        if (selectedType == R.id.radioFound) {
            cameraLauncher.launch(null);
        } else if (selectedType == R.id.radioLost) {
            imagePicker.launch(new String[]{"image/*"});
        } else {
            Toast.makeText(
                    this,
                    "Please select Lost or Found first",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void copyGalleryImage(Uri uri) {

        try {
            File folder = new File(getFilesDir(), "item_photos");

            if (!folder.exists()) {
                folder.mkdirs();
            }

            File photoFile = new File(
                    folder,
                    "item_" + System.currentTimeMillis() + ".jpg"
            );

            InputStream inputStream =
                    getContentResolver().openInputStream(uri);

            if (inputStream == null) {
                Toast.makeText(this, "Unable to open selected photo",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            try (InputStream input = inputStream;
                 OutputStream output = new FileOutputStream(photoFile)) {

                byte[] buffer = new byte[4096];
                int length;

                while ((length = input.read(buffer)) != -1) {
                    output.write(buffer, 0, length);
                }
            }

            imageUri = Uri.fromFile(photoFile).toString();
            showPhoto(Uri.parse(imageUri));

        } catch (Exception e) {
            Toast.makeText(this, "Unable to save selected photo",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCameraImage(Bitmap bitmap) {

        try {
            File folder = new File(getFilesDir(), "item_photos");

            if (!folder.exists()) {
                folder.mkdirs();
            }

            File photoFile = new File(
                    folder,
                    "item_" + System.currentTimeMillis() + ".jpg"
            );

            try (FileOutputStream output =
                         new FileOutputStream(photoFile)) {

                bitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        90,
                        output
                );
            }

            imageUri = Uri.fromFile(photoFile).toString();
            showPhoto(Uri.parse(imageUri));

        } catch (Exception e) {
            Toast.makeText(this, "Unable to save camera photo",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showPhoto(Uri uri) {
        imgItemPreview.setImageURI(uri);
        imgItemPreview.setVisibility(ImageView.VISIBLE);
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

        if (imageUri != null) {
            values.put("image_uri", imageUri);
        } else {
            values.putNull("image_uri");
        }

        long result;

        try {
            result = database.insert("Items", null, values);
        } finally {
            database.close();
        }

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
