package me.opklnm102.studyexampleproject;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
                    Contact contact = new Contact();
                    contact.setName(strName);
                    contact.setName(strPhoneNumber);

                    if(listener != null){
                        listener.onItemAdd(contact);
                    }
                } else {
                    Toast.makeText(mContext, "빈칸을 채워주세요" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void bind(Contact contact) {

    }




}
