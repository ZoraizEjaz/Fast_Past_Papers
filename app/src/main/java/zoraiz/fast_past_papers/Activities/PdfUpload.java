package zoraiz.fast_past_papers.Activities;


import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import java.util.UUID;

import zoraiz.fast_past_papers.R;

public class PdfUpload extends AppCompatActivity{

    Button SelectButton, UploadButton;

    EditText PdfNameEditText ;

    Uri uri;

    public static final String PDF_UPLOAD_HTTP_URL = "http://fastnu.technocraz.net/api/paper";

    public int PDF_REQ_CODE = 1;

    String PdfNameHolder, PdfPathHolder, PdfID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_upload);

        AllowRunTimePermission();

        SelectButton = (Button) findViewById(R.id.button);
        UploadButton = (Button) findViewById(R.id.button2);
        PdfNameEditText = (EditText) findViewById(R.id.editText);

        SelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 //PDF selection code start from here .

                Intent intent = new Intent();

                intent.setType("application/docx");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF_REQ_CODE);

                //browseDocuments();

            }
        });

        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PdfUploadFunction();

            }
        });

    }

    private void browseDocuments(){

        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent,"ChooseFile"), PDF_REQ_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PDF_REQ_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            SelectButton.setText("PDF is Selected");
        }
    }

    public void PdfUploadFunction() {

        PdfNameHolder = PdfNameEditText.getText().toString().trim();

        PdfPathHolder = FilePath.getPath(this, uri);

        if (PdfPathHolder == null) {

            Toast.makeText(this, "Please move your PDF file to internal storage & try again.", Toast.LENGTH_LONG).show();

        } else {

            try {

                PdfID = UUID.randomUUID().toString();

                new MultipartUploadRequest(this, PdfID, PDF_UPLOAD_HTTP_URL)
                        .addFileToUpload(PdfPathHolder, "paper_doc")
                        .addParameter("paper_name", PdfNameHolder)
                        .addParameter("year","2017-09-14")
                        .addParameter("type","doc")
                        .addParameter("semester","fall")
                        .addParameter("dept_id","1")
                        .addParameter("course_id","1")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(5)
                        .startUpload();

            } catch (Exception exception) {

                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void AllowRunTimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(PdfUpload.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {

            Toast.makeText(PdfUpload.this,"READ_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(PdfUpload.this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(PdfUpload.this,"Permission Granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(PdfUpload.this,"Permission Canceled", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


}