package com.example.dong.doitmission_09;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * 부드러운 곡선으로 그려지도록 기능 추가
 * 
 * @author Mike
 *
 */
public class BestPaintBoardActivity extends ActionBarActivity {
	
	BestPaintBoard board;
	Button colorBtn;
	Button penBtn;
	Button eraserBtn;
	Button undoBtn;
    RadioButton buttBtn;
    RadioButton roundBtn;
    RadioButton squareBtn;
	
	LinearLayout addedLayout;
	Button colorLegendBtn;
	TextView sizeLegendTxt;
	
	int mColor = 0xff000000;
	int mSize = 2;
	int oldColor;
	int oldSize;
	boolean eraserSelected = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        LinearLayout toolsLayout = (LinearLayout) findViewById(R.id.toolsLayout);
        LinearLayout boardLayout = (LinearLayout) findViewById(R.id.boardLayout);
        colorBtn = (Button) findViewById(R.id.colorBtn);
        penBtn = (Button) findViewById(R.id.penBtn);
        eraserBtn = (Button) findViewById(R.id.eraserBtn);
        undoBtn = (Button) findViewById(R.id.undoBtn);
        buttBtn = (RadioButton)findViewById(R.id.buttButton);
        roundBtn = (RadioButton)findViewById(R.id.roundButton);
        squareBtn = (RadioButton)findViewById(R.id.squareButton);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.MATCH_PARENT,
        		LinearLayout.LayoutParams.MATCH_PARENT);
        
        board = new BestPaintBoard(this);
        board.setLayoutParams(params);
        board.setPadding(2, 2, 2, 2);
        
        boardLayout.addView(board);
        
        // add legend buttons
        LinearLayout.LayoutParams addedParams = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.MATCH_PARENT,
        		LinearLayout.LayoutParams.MATCH_PARENT);
        
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.MATCH_PARENT,
        		48);
        addedLayout = new LinearLayout(this);
        addedLayout.setLayoutParams(addedParams);
        addedLayout.setOrientation(LinearLayout.VERTICAL);
        addedLayout.setPadding(8,8,8,8);
        
        LinearLayout outlineLayout = new LinearLayout(this);
        outlineLayout.setLayoutParams(buttonParams);
        outlineLayout.setOrientation(LinearLayout.VERTICAL);
        outlineLayout.setBackgroundColor(Color.LTGRAY);
        outlineLayout.setPadding(1,1,1,1);
        
        colorLegendBtn = new Button(this);
        colorLegendBtn.setLayoutParams(buttonParams);
        colorLegendBtn.setText(" ");
        colorLegendBtn.setBackgroundColor(mColor);
        colorLegendBtn.setHeight(20);
        outlineLayout.addView(colorLegendBtn);
        addedLayout.addView(outlineLayout);
        
        sizeLegendTxt = new TextView(this);
        sizeLegendTxt.setLayoutParams(buttonParams);
        sizeLegendTxt.setText("Size : " + mSize);
        sizeLegendTxt.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        sizeLegendTxt.setTextSize(16);
        sizeLegendTxt.setTextColor(Color.BLACK);
        addedLayout.addView(sizeLegendTxt);
        
        toolsLayout.addView(addedLayout);
        
        
        colorBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		ColorPaletteDialog.listener = new OnColorSelectedListener() {
        			public void onColorSelected(int color) {
        				mColor = color;
        				board.updatePaintProperty(mColor, mSize);
        				displayPaintProperty();
        			}
        		};
        		// show color palette dialog
        		Intent intent = new Intent(getApplicationContext(), ColorPaletteDialog.class);
        		startActivity(intent);
        	}
        });
        
        penBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		PenPaletteDialog.listener = new OnPenSelectedListener() {
        			public void onPenSelected(int size) {
        				mSize = size;
        				board.updatePaintProperty(mColor, mSize);
        				displayPaintProperty();
        			}
        		};
        		// show pen palette dialog
        		Intent intent = new Intent(getApplicationContext(), PenPaletteDialog.class);
        		startActivity(intent);
        	}
        });
        
        eraserBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		eraserSelected = !eraserSelected;
        		
        		if (eraserSelected) {
                    colorBtn.setEnabled(false);
                    penBtn.setEnabled(false);
                    undoBtn.setEnabled(false);
        			
                    colorBtn.invalidate();
                    penBtn.invalidate();
                    undoBtn.invalidate();
                    
                    oldColor = mColor;
                    oldSize = mSize;
                    
                    mColor = Color.WHITE;
                    mSize = 15;
                    
                    board.updatePaintProperty(mColor, mSize);
                    displayPaintProperty();
                    
                } else {
                	colorBtn.setEnabled(true);
                    penBtn.setEnabled(true);
                    undoBtn.setEnabled(true);
        			
                    colorBtn.invalidate();
                    penBtn.invalidate();
                    undoBtn.invalidate();
                    
                    mColor = oldColor;
                    mSize = oldSize;
                    
                    board.updatePaintProperty(mColor, mSize);
                    displayPaintProperty();
                }
        	}
        });
        
        undoBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		board.undo();
        	}
        });

        buttBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                board.updatePaintCap(1);
                displayPaintProperty();
            }
        });

        roundBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                board.updatePaintCap(2);
                displayPaintProperty();
            }
        });

        squareBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                board.updatePaintCap(3);
                displayPaintProperty();
            }
        });
        
    }
    
        
    public int getChosenColor() {
    	return mColor;
    }
    
    public int getPenThickness() {
    	return mSize;
    }
    
    private void displayPaintProperty() {
    	colorLegendBtn.setBackgroundColor(mColor);
    	sizeLegendTxt.setText("Size : " + mSize);
    	
    	addedLayout.invalidate();
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}