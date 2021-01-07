package breakthecode.com.clickandgo.classes;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import breakthecode.com.clickandgo.R;

public class TransactionDialog {

    Activity activity;
    Dialog dialog;

    public TransactionDialog(Activity myActivity){
        this.activity = myActivity;
    }

    public void startTransactionDialog(){
        dialog = new Dialog(activity);
        Window window = dialog.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.transaction_dialog);
        dialog.setCancelable(true);
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
