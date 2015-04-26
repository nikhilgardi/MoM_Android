package com.mom.app.fragment;


    import android.os.Bundle;
    import android.support.v4.app.DialogFragment;
    import android.view.KeyEvent;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.view.inputmethod.EditorInfo;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.TextView.OnEditorActionListener;

import com.mom.app.R;

public class MyDialogFragment extends DialogFragment implements
            OnEditorActionListener {

        public interface EditNameDialogListener {
            void onFinishEditDialog(String inputText);
        }

        private EditText mEditText;

        public MyDialogFragment() {
            // Empty constructor required for DialogFragment
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.activity_verify_tpin, container);
            mEditText = (EditText) view.findViewById(R.id.tpinTxt);
            getDialog().setTitle("Dialog Fragment Example");

            // Show soft keyboard automatically
            mEditText.requestFocus();
            mEditText.setOnEditorActionListener(this);

            return view;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (EditorInfo.IME_ACTION_DONE == actionId) {
                // Return input text to activity
                EditNameDialogListener activity = (EditNameDialogListener) getActivity();
                activity.onFinishEditDialog(mEditText.getText().toString());
                this.dismiss();
                return true;
            }
            return false;
        }
}




