package me.opklnm102.exdialogfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

/**
 * Created by Administrator on 2016-04-06.
 */
public class MyParentFragment extends Fragment implements EditNameDialogFragment.EditNameDialogListener {

    private void showEditDialog(){
        FragmentManager fm  = getFragmentManager();
        EditNameDialogFragment fragment = EditNameDialogFragment.newInstance("Some title");

        // dialog의 결과를 받을 fragment 설정
        fragment.setTargetFragment(MyParentFragment.this, 300);
        fragment.show(fm, "fragment_edit_name");
    }




    @Override
    public void onFinishEditDialog(String inputText) {
        Toast.makeText(getActivity(), "Hi, " + inputText, Toast.LENGTH_SHORT).show();
    }
}
