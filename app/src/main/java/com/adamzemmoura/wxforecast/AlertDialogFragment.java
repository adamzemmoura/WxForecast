package com.adamzemmoura.wxforecast;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

public class AlertDialogFragment extends DialogFragment {

    private AlertType type = AlertType.Generic_Error;

    enum AlertType {
        Generic_Error, No_Network_Error, JSON_Parsing_Error
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.error_title)
                .setPositiveButton(R.string.error_okay_button_text, null);

        switch (type) {
            case No_Network_Error:
                builder.setMessage(R.string.network_unavailable_message);
                break;
            case JSON_Parsing_Error:
                builder.setMessage(R.string.json_parsing_error_message);
            default:
                builder.setMessage(R.string.error_message);
        }

        return builder.create();
    }

    public void setType(AlertType type) {
        this.type = type;
    }
}
