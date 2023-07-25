package com.l0pht.dinepal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CustomDiaglog extends AppCompatDialogFragment {
    private EditText edtextdish;
    private EditText edtextprice;

    private CustomDialogListner listner;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.custom_dialog_layout,null);

        builder.setView(view)
                .setTitle("Update")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String dishname=edtextdish.getText().toString();
                        String price=edtextprice.getText().toString();

                        listner.applyTexts(dishname,price);

                    }
                });

        edtextdish =view.findViewById(R.id.ed1);
        edtextprice=view.findViewById(R.id.ed2);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listner=(CustomDialogListner)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement CustomDialogListner");
        }
    }

    public interface CustomDialogListner{
        void applyTexts(String dishname,String price);
    }

}
