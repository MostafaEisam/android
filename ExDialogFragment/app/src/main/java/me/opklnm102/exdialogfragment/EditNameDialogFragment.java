package me.opklnm102.exdialogfragment;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Administrator on 2016-04-05.
 */
public class EditNameDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    public static final String ARG_TITLE = "title";

    private EditText mEditText;

    //soft keyboard 리스너
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        //Done 버튼 눌리시
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            EditNameDialogListener listener = (EditNameDialogListener) getActivity();
            listener.onFinishEditDialog(mEditText.getText().toString());

            dismiss();
            return true;
        }
        return false;
    }

    //1. 결과가 전달되는 리스너
    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

    public void sendBackResult() {
        EditNameDialogListener listener = (EditNameDialogListener) getTargetFragment();
        listener.onFinishEditDialog(mEditText.getText().toString());
        dismiss();
    }

    public EditNameDialogFragment() {
    }

    public static EditNameDialogFragment newInstance(String title) {
        EditNameDialogFragment fragment = new EditNameDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_edit_name, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditText = (EditText) view.findViewById(R.id.editText_name);

        String title = getArguments().getString(ARG_TITLE, "Enter Name");
        getDialog().setTitle(title);

        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Set to adjust screen height automatically, when soft keyboard appears on screen
        //manifests.xml에  android:windowSoftInputMode="adjustResize" 추가
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mEditText.setOnEditorActionListener(this);
    }

    // Removing the TitleBar from the Dialog
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {

        /*
          Sizing Dialogs
          Runtime Dimensions
         */
        //1.
//        // Store access variables for window and blank point
//        Window window = getDialog().getWindow();
//        Point size = new Point();
//
//        // Store dimensions of the screen in `size`
//        Display display = window.getWindowManager().getDefaultDisplay();
//        display.getSize(size);
//
//        // Set the width of the dialog proportional to 75% of the screen width
//        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setGravity(Gravity.CENTER);

        //2.
//        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
//        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
//        getDialog().getWindow().setLayout(width, height);

         /*
          Sizing Dialogs
          Full-Screen Dialog
         */
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();

        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams)params);

        // Call super onResume after sizing
        super.onResume();
    }
}
