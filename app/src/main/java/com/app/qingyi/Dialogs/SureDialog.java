package com.app.qingyi.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.qingyi.R;

public class SureDialog extends Dialog {

    private static SureDialog dialog;
    public SureDialog(Context context) {
        super(context);
    }

    public SureDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title = "确认？";
        private int sureColor = 0;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setSureColor(int sureColor) {
            this.sureColor = sureColor;
            return this;
        }

        public Builder setPositiveButton(OnClickListener listener) {
            this.positiveButtonClickListener = listener;
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

        public SureDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            dialog = new SureDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_sure_layout, null);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            // set the commit button
            if(title != null){
                ((TextView) layout.findViewById(R.id.title)).setText(title);
            }
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
            if(sureColor != 0){
                ((TextView) layout.findViewById(R.id.commit)).setTextColor(context.getResources().getColor(sureColor));
            }
            dialog.setContentView(layout);
            return dialog;
        }

    }
}
