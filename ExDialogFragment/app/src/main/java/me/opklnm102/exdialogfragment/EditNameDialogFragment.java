package me.opklnm102.exdialogfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        if(EditorInfo.IME_ACTION_DONE == actionId){
            EditNameDialogListener listener = (EditNameDialogListener)getActivity();
            listener.onFinishEditDialog(mEditText.getText().toString());

            dismiss();
            return true;
        }
        return false;
    }

    //1. 결과가 전달되는 리스너
   public interface EditNameDialogListener{
        void onFinishEditDialog(String inputText);
    }

    public EditNameDialogFragment() {
    }

    public static EditNameDialogFragment newInstance(String title){
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

        mEditText = (EditText)view.findViewById(R.id.editText_name);

        String title = getArguments().getString(ARG_TITLE, "Enter Name");
        getDialog().setTitle(title);

        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mEditText.setOnEditorActionListener(this);

    }
}
