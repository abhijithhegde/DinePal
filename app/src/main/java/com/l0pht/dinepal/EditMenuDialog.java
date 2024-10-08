package com.l0pht.dinepal;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class EditMenuDialog extends Dialog {
    private TextView slNoTextView, titleTextView;
    private EditText itemNameEditText, itemPriceEditText;
    private Button deleteButton;
    private Button updateButton;
    private DatabaseHelper databaseHelper;
    private OnDialogButtonClickListener buttonClickListener;

    public EditMenuDialog(@NonNull Context context, int slNo, String itemName, String itemPrice, DatabaseHelper databaseHelper) {
        super(context);
        setContentView(R.layout.activity_edit_menu_dialog);

        titleTextView = findViewById(R.id.titleTextView);
        slNoTextView = findViewById(R.id.slNoTextView);
        itemNameEditText = findViewById(R.id.itemNameEditText);
        itemPriceEditText = findViewById(R.id.itemPriceEditText);
        deleteButton = findViewById(R.id.deleteButton);
        updateButton = findViewById(R.id.updateButton);

        deleteButton.setText("Delete");
        updateButton.setText("Update");

        slNoTextView.setText(String.valueOf(slNo));
        itemNameEditText.setText(itemName);
        itemPriceEditText.setText(itemPrice);

        deleteButton.setOnClickListener(v -> {
            dismiss();
            if (buttonClickListener != null) {
                buttonClickListener.onDeleteButtonClick();
            }
        });

        updateButton.setOnClickListener(v -> {
            dismiss();
            String slNoText = slNoTextView.getText().toString();
            String itemNameText = itemNameEditText.getText().toString();
            String itemPriceText = itemPriceEditText.getText().toString();
            if (buttonClickListener != null) {
                buttonClickListener.onUpdateButtonClick(slNoText, itemNameText, itemPriceText);
            }
        });
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setOnDialogButtonClickListener(OnDialogButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    public void setDeleteButtonVisibility(boolean visible) {
        if (visible) {
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            deleteButton.setVisibility(View.GONE);
        }
    }

    public interface OnDialogButtonClickListener {
        void onDeleteButtonClick();

        void onUpdateButtonClick(String slNo, String itemName, String itemPrice);
    }
}


