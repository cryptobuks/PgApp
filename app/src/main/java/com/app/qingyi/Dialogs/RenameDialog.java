package com.app.qingyi.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.qingyi.R;

/**
 * Created by ylkjcjq on 2018/6/5.
 */


public class RenameDialog extends Dialog {

    private static RenameDialog dialog;
    private static EditText boxName;
    public RenameDialog(Context context) {
        super(context);
    }

    public RenameDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String edittextName;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setPositiveButton(OnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setEdittextName(String edittext) {
            this.edittextName = edittext;
            return this;
        }

        public Builder setNegativeButton(OnClickListener listener) {
            this.negativeButtonClickListener = listener;
            return this;
        }

        public void dismiss(){
            if(dialog != null)
            dialog.dismiss();
        }
        public String getEdittext(){
            if(boxName != null)
            return boxName.getText().toString().trim();
            return "";
        }

        public RenameDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            dialog = new RenameDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_rename_layout, null);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            // set the commit button
            if (positiveButtonClickListener != null) {
                ((TextView) layout.findViewById(R.id.commit))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                positiveButtonClickListener.onClick(dialog,
                                        DialogInterface.BUTTON_POSITIVE);
                            }
                        });
            }
            // set the  cancel button
            if (negativeButtonClickListener != null) {
                ((TextView) layout.findViewById(R.id.cancel))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                negativeButtonClickListener.onClick(dialog,
                                        DialogInterface.BUTTON_NEGATIVE);
                            }
                        });
            }
            boxName = (EditText) layout.findViewById(R.id.boxName);
            if(edittextName != null){
                boxName.setText(edittextName);
            }
            dialog.setContentView(layout);
            return dialog;
        }

    }
}
