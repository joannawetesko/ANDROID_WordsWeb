package pl.com.wordsweb.ui.utils;

import android.content.Context;
import android.content.DialogInterface;

import pl.com.wordsweb.R;
import pl.com.wordsweb.event.OnGoToLoginEvent;

import static pl.com.wordsweb.config.AppSettings.bus;

/**
 * Created by wewe on 11.11.16.
 */

public class PhraseWebAlertDialog {

    public static void showNeedLogoutDialog(Context context) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setMessage(R.string.register_dialog_message)
                .setTitle(R.string.register_dialog_title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showNeedConfirmEmail(Context context) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setMessage(R.string.register_dialog_message)
                .setTitle(R.string.register_dialog_title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                bus.post(new OnGoToLoginEvent());

            }
        });
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}
