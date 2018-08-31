package com.app.qingyi.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.qingyi.R;

public class UpdateAppDialog extends Dialog {

    private static UpdateAppDialog dialog;
    private static TextView commit;
    public UpdateAppDialog(Context context) {
        super(context);
    }

    public UpdateAppDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title = "确认？";
        private Boolean isShowNevButton = true;
        private String content = "1.优化app功能\\n2.修复存在的bug\\n";
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public void setCommitText(String content) {
           if(commit != null){
               commit.setText(content);
           }
        }

        public Builder setPositiveButton(OnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(OnClickListener listener) {
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setShowNegativeButton(Boolean bool) {
            this.isShowNevButton = bool;
            return this;
        }

        public void dismiss() {
            if (dialog != null)
                dialog.dismiss();
        }

        public UpdateAppDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            dialog = new UpdateAppDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_updateapp_layout, null);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            // set the commit button
            if (title != null) {
                ((TextView) layout.findViewById(R.id.title)).setText(title);
            }
            if (content != null) {
                ((TextView) layout.findViewById(R.id.content)).setText(content.replace("\\n", "\n"));
            }
            if (positiveButtonClickListener != null) {
                commit = (TextView) layout.findViewById(R.id.commit);
                commit.setOnClickListener(new View.OnClickListener() {
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
            if (!isShowNevButton) {
                layout.findViewById(R.id.cancel).setVisibility(View.INVISIBLE);
            }
            dialog.setCanceledOnTouchOutside(isShowNevButton);
            dialog.setContentView(layout);
            dialog.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0) {
                        ((Activity)context).finish();
                    }
                    return false;
                }
            });
            return dialog;
        }

    }
}
