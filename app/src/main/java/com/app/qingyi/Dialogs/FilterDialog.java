package com.app.qingyi.Dialogs;

import android.content.DialogInterface;
import android.view.View;

/**
 * Created by ylkjcjq on 2018/6/5.
 */

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.qingyi.R;


public class FilterDialog extends Dialog {

    private static FilterDialog dialog;
    public FilterDialog(Context context) {
        super(context);
    }

    public FilterDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
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

        public Builder setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener) {
            this.mOnCheckedChangeListener = mOnCheckedChangeListener;
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

        public FilterDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            dialog = new FilterDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_select_layout, null);
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

            ((RadioGroup) layout.findViewById(R.id.mRadioGroup)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (mOnCheckedChangeListener != null) {
                        mOnCheckedChangeListener.onCheckedChanged(radioGroup, i);
                    }
                }
            });
            dialog.setContentView(layout);
            return dialog;
        }

    }
}
