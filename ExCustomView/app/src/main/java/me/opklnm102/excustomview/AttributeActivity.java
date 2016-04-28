package me.opklnm102.excustomview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.TextView;


public class AttributeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attribute);

        AttrButton btnAttr = (AttrButton) findViewById(R.id.button_attr);
        TextView tv = (TextView) findViewById(R.id.textView_attr);
        tv.setText(btnAttr.strAttr);
    }
}
