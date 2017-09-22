package zoraiz.fast_past_papers.Activities;

import android.Manifest;
import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.snatik.storage.Storage;
import com.snatik.storage.helpers.OrderType;
import com.snatik.storage.helpers.SizeUnit;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import zoraiz.fast_past_papers.Adapter.FilesAdapter;
import zoraiz.fast_past_papers.Dialogs.ConfirmDeleteDialog;
import zoraiz.fast_past_papers.Dialogs.RenameDialog;
import zoraiz.fast_past_papers.Dialogs.UpdateItemDialog;
import zoraiz.fast_past_papers.R;
import zoraiz.fast_past_papers.Utils.Helper;

import static zoraiz.fast_past_papers.Utils.Helper.fileExt;

/**
 * Created by Zoraiz on 8/15/2017.
 */

public class FileManager extends AppCompatActivity implements
        FilesAdapter.OnFileItemListener,
        UpdateItemDialog.DialogListener,
        ConfirmDeleteDialog.ConfirmListener,
        RenameDialog.DialogListener {

    private static final int PERMISSION_REQUEST_CODE = 1000;
    private RecyclerView mRecyclerView;
    private FilesAdapter mFilesAdapter;
    private Storage mStorage;

    private TextView mPathView;
    private TextView mMovingText;
    private boolean mCopy;
    private View mMovingLayout;
    private int mTreeSteps = 0;
    private String mMovingPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStorage = new Storage(getApplicationContext());

        setContentView(R.layout.activity_file_manger);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(getResources().getString(R.string.downloads));


        try{
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            }

        }catch (Exception ex){

        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mPathView = (TextView) findViewById(R.id.path);
        mPathView.setVisibility(View.GONE);
        mMovingLayout = findViewById(R.id.moving_layout);
        mMovingText = (TextView) mMovingLayout.findViewById(R.id.moving_file_name);

        mMovingLayout.findViewById(R.id.accept_move).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMovingLayout.setVisibility(View.GONE);
                if (mMovingPath != null) {

                    if (!mCopy)
                    {

                        String toPath = getCurrentPath() + File.separator + mStorage.getFile(mMovingPath).getName();
                        if (!mMovingPath.equals(toPath)) {
                            mStorage.move(mMovingPath, toPath);
                            Helper.showSnackbar("Moved", mRecyclerView);
                            showFiles("/storage/emulated/0/Android/data/com.fast.zoraiz.papers/files/Download");
                        } else {
                            Helper.showSnackbar("The file is already here", mRecyclerView);
                        }
                    }
                    else
                    {
                        String toPath = getCurrentPath() + File.separator + "copy " + mStorage.getFile(mMovingPath).getName();
                        mStorage.copy(mMovingPath, toPath);
                        Helper.showSnackbar("Copied", mRecyclerView);
                        showFiles("/storage/emulated/0/Android/data/com.fast.zoraiz.papers/files/Download");
                    }
                    mMovingPath = null;
                }
            }
        });

        mMovingLayout.findViewById(R.id.decline_move).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMovingLayout.setVisibility(View.GONE);
                mMovingPath = null;
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mFilesAdapter = new FilesAdapter(getApplicationContext());
        mFilesAdapter.setListener(this);
        mRecyclerView.setAdapter(mFilesAdapter);



        // load files
        String mypath=mStorage.getExternalStorageDirectory();
        mypath="/storage/emulated/0/Android/data/com.fast.zoraiz.papers/files/Download";
        showFiles(mypath);

        checkPermission();
    }


    private void showFiles(String path) {
        mPathView.setText(path);
        List<File> files = mStorage.getFiles(path);
        if (files != null) {
            Collections.sort(files, OrderType.NAME.getComparator());
        }
        mFilesAdapter.setFiles(files);
        mFilesAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(File file) {
        if (file.isDirectory()) {
            mTreeSteps++;
            String path = file.getAbsolutePath();
            showFiles(path);
        } else {

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String mimeType =  MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExt(file.getAbsolutePath()));
                Uri apkURI = FileProvider.getUriForFile(
                        this,
                        getApplicationContext()
                                .getPackageName() + ".provider", file);
                intent.setDataAndType(apkURI, mimeType);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                if (mStorage.getSize(file, SizeUnit.KB) > 500) {
                    Helper.showSnackbar("The file is too big for preview", mRecyclerView);
                    return;
                }
                Intent intent = new Intent(this, ViewTextActivity.class);
                intent.putExtra(ViewTextActivity.EXTRA_FILE_NAME, file.getName());
                intent.putExtra(ViewTextActivity.EXTRA_FILE_PATH, file.getAbsolutePath());
                startActivity(intent);
            }

        }
    }

    @Override
    public void onLongClick(File file) {
        UpdateItemDialog.newInstance(file.getAbsolutePath()).show(getFragmentManager(), "update_item");
    }

    @Override
    public void onBackPressed() {

       finish();
    }

    private String getCurrentPath() {
        return mPathView.getText().toString();
    }

    private String getPreviousPath() {
        String path = getCurrentPath();
        int lastIndexOf = path.lastIndexOf(File.separator);
        if (lastIndexOf < 0) {
            Helper.showSnackbar("Can't go anymore", mRecyclerView);
            return getCurrentPath();
        }
        return path.substring(0, lastIndexOf);
    }

    @Override
    public void onOptionClick(int which, String path) {
        switch (which) {

            case R.id.delete:
                ConfirmDeleteDialog.newInstance(path).show(getFragmentManager(), "confirm_delete");
                break;
            case R.id.rename:
                RenameDialog.newInstance(path).show(getFragmentManager(), "rename");
                break;
            case R.id.move:
                mMovingText.setText(getString(R.string.moving_file, mStorage.getFile(path).getName() + " To Download Folder") );
                mMovingPath = path;
                mCopy = false;
                mMovingLayout.setVisibility(View.VISIBLE);
                mPathView.setText("/storage/emulated/0/Download");
                break;
            case R.id.copy:
                mMovingText.setText(getString(R.string.copy_file, mStorage.getFile(path).getName()));
                mMovingPath = path;
                mCopy = true;
                mMovingLayout.setVisibility(View.VISIBLE);
                mPathView.setText("/storage/emulated/0/Download");

                break;
        }
    }

    @Override
    public void onConfirmDelete(String path) {
        if (mStorage.getFile(path).isDirectory()) {
            mStorage.deleteDirectory(path);
            Helper.showSnackbar("Folder was deleted", mRecyclerView);
        } else {
            mStorage.deleteFile(path);
            Helper.showSnackbar("File was deleted", mRecyclerView);
        }
        showFiles(getCurrentPath());
    }

    @Override
    public void onRename(String fromPath, String toPath) {
        mStorage.rename(fromPath, toPath);
        showFiles(getCurrentPath());
        Helper.showSnackbar("Renamed", mRecyclerView);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();

                return true;
        }
        return true;
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showFiles(mStorage.getExternalStorageDirectory());
        } else {
            finish();
        }
    }

}