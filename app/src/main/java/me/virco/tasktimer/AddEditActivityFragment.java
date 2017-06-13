package me.virco.tasktimer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEditActivityFragment extends Fragment {
    private static final String TAG = "AddEditActivityFragment";

    public enum FragmentEditMode {EDIT, ADD}
    private FragmentEditMode mMode;
    private EditText mNameTextView;
    private EditText mDescriptionEditText;
    private EditText mSortOrderEditText;
    private Button mSaveButton;
    private OnSaveClicked mSaveListener = null;

    interface OnSaveClicked {
        void onSaveClicked();
    }

    public AddEditActivityFragment() {
        Log.d(TAG, "AddEditActivityFragment: constructor called");
    }

    public boolean canClose() {
        return false;
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: starts");
        super.onAttach(context);
        try {
            mSaveListener = (OnSaveClicked) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnSaveClicked interface");
        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mSaveListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starts");
        View view = inflater.inflate(R.layout.fragment_add_edit, container, false);
        mNameTextView = (EditText) view.findViewById(R.id.addedit_name);
        mDescriptionEditText = (EditText) view.findViewById(R.id.addedit_description);
        mSortOrderEditText = (EditText) view.findViewById(R.id.addedit_sortorder);
        mSaveButton = (Button) view.findViewById(R.id.addedit_save);

//        Bundle arguments = getActivity().getIntent().getExtras();
        Bundle arguments = getArguments();

        final Task task;
        if (arguments != null) {
            Log.d(TAG, "onCreateView: retrieving task details");
            task = (Task) arguments.getSerializable(Task.class.getSimpleName());
            if (task != null) {
                Log.d(TAG, "onCreateView: Task details found, editing...");
                mNameTextView.setText(task.getName());
                mDescriptionEditText.setText(task.getDescription());
                mSortOrderEditText.setText(String.valueOf(task.getSortOrder()));
                mMode = FragmentEditMode.EDIT;
            } else {
                // No Task, add a new task, don't edit the existing one
                mMode = FragmentEditMode.ADD;
            }
        } else {
            task = null;
            Log.d(TAG, "onCreateView: No arguments, adding new record");
            mMode = FragmentEditMode.ADD;
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the database if at least one field has changed.
                // - There's no need to hit the database unless this has happened.
                int so;     // to save repeated conversions to int;
                if (mSortOrderEditText.length() > 0) {
                    so = Integer.parseInt(mSortOrderEditText.getText().toString());
                } else {
                    so = 0;
                }
                ContentResolver contentResolver = getActivity().getContentResolver();
                ContentValues contentValues = new ContentValues();

                switch (mMode) {
                    case EDIT:
                        if (!mNameTextView.getText().toString().equals(task.getName())) {
                            contentValues.put(TasksContract.Columns.TASKS_NAME, mNameTextView.getText().toString());
                        }
                        if (!mDescriptionEditText.getText().toString().equals(task.getDescription())) {
                            contentValues.put(TasksContract.Columns.TASKS_DESCRIPTION, mDescriptionEditText.getText().toString());
                        }
                        if (so != task.getSortOrder()) {
                            contentValues.put(TasksContract.Columns.TASKS_SORT_ORDER, so);
                        }
                        if (contentValues.size() != 0) {
                            Log.d(TAG, "onClick: updating task");
                            contentResolver.update(TasksContract.buildTaskUri(task.get_Id()), contentValues, null, null);
                        }
                        break;
                    case ADD:
                        if (mNameTextView.length() > 0) {
                            Log.d(TAG, "onClick: adding new task");
                            contentValues.put(TasksContract.Columns.TASKS_NAME, mNameTextView.getText().toString());
                            contentValues.put(TasksContract.Columns.TASKS_DESCRIPTION, mDescriptionEditText.getText().toString());
                            contentValues.put(TasksContract.Columns.TASKS_SORT_ORDER, so);
                            contentResolver.insert(TasksContract.CONTENT_URI, contentValues);
                        }
                        break;
                }
                Log.d(TAG, "onClick: Done editing");

                if (mSaveListener != null) {
                    mSaveListener.onSaveClicked();
                }
            }
        });
        Log.d(TAG, "onCreateView: Exiting...");
        return view;
    }
}
