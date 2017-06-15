package me.virco.tasktimer;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

public class AddEditActivity extends AppCompatActivity implements AddEditActivityFragment.OnSaveClicked,
AppDialog.DialogEvents {
    private static final String TAG = "AddEditActivity";
    private AddEditActivityFragment mFragment;
    public static final int DIALOG_CANCEL_EDIT_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mFragment = new AddEditActivityFragment();

        Bundle arguments = getIntent().getExtras();
        mFragment.setArguments(arguments);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, mFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: home button pressed");
                if (mFragment.canClose()) {
                    return super.onOptionsItemSelected(item);
                } else {
                    showConfirmDialog();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showConfirmDialog() {
        AppDialog dialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, DIALOG_CANCEL_EDIT_ID);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.cancelEditDialog_message));
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.cancelEditDialog_positive_caption);
        args.putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string.cancelEditDialog_negative_caption);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onSaveClicked() {
        finish();
    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onPositiveDialogResult: called");
        // no action
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onNegativeDialogResult: called");
        finish();
    }

    @Override
    public void onDialogCancelled(int dialogId) {
        Log.d(TAG, "onDialogCancelled: called");
        // no action
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: called");
        if (mFragment.canClose()) {
            super.onBackPressed();
        } else {
           showConfirmDialog();
        }
    }
}
