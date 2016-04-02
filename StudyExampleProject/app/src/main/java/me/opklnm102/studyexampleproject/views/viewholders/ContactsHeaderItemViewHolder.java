package me.opklnm102.studyexampleproject.views.viewholders;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import me.opklnm102.studyexampleproject.R;
import me.opklnm102.studyexampleproject.models.ContactItem;
import me.opklnm102.studyexampleproject.views.adapters.ContactsListAdapter;

/**
 * Created by Administrator on 2016-04-02.
 */
public class ContactsHeaderItemViewHolder extends RecyclerView.ViewHolder {

    private final Context mContext;

    ContactsListAdapter.OnItemAddListener listener;

    public final TextInputLayout tlName;
    public final TextInputLayout tlPhoneNumber;
    public final TextInputEditText etName;
    public final TextInputEditText etPhoneNumber;
    public final Button btnAdd;

    public final View mView;

    public ContactsHeaderItemViewHolder(View itemView, final ContactsListAdapter.OnItemAddListener listener) {
        super(itemView);

        mContext = itemView.getContext();
        mView = itemView;

        this.listener = listener;

        tlName = (TextInputLayout) itemView.findViewById(R.id.textinput_layout_name);
        tlPhoneNumber = (TextInputLayout) itemView.findViewById(R.id.textinput_layout_phone_number);
        etName = (TextInputEditText) itemView.findViewById(R.id.editText_name);
        etPhoneNumber = (TextInputEditText) itemView.findViewById(R.id.editText_phone_number);
        btnAdd = (Button) itemView.findViewById(R.id.button_contacts_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strName = etName.getText().toString();
                String strPhoneNumber = etPhoneNumber.getText().toString();

                if(!"".equals(strName) && !"".equals(strPhoneNumber)){
                    ContactItem contactItem = new ContactItem();
                    contactItem.setProfileImg(R.mipmap.ic_launcher);
                    contactItem.setName(strName);
                    contactItem.setPhoneNumber(strPhoneNumber);

                    etName.setText("");
                    etPhoneNumber.setText("");

                    //soft keyboard hide
                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);

                    if(listener != null){
                        listener.onItemAdd(contactItem);
                    }
                } else {
                    Toast.makeText(mContext, "빈칸을 채워주세요" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void bind(ContactItem contactItem) {

    }
}
