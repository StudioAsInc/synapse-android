package com.synapse.social.studioasinc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.synapse.social.studioasinc.animations.textview.TVeffects;

public class SummaryBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_SUMMARY_TEXT = "summary_text";

    public static SummaryBottomSheetDialogFragment newInstance(String summaryText) {
        SummaryBottomSheetDialogFragment fragment = new SummaryBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUMMARY_TEXT, summaryText);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TVeffects summaryTextView = view.findViewById(R.id.summary_text);
        if (getArguments() != null) {
            String summaryText = getArguments().getString(ARG_SUMMARY_TEXT);
            if (summaryText != null) {
                summaryTextView.setCharDelay(20);

                summaryTextView.startTyping(summaryText);
            }
        }
    }
}
