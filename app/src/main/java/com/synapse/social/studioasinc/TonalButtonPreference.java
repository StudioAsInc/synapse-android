package com.synapse.social.studioasinc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.google.android.material.button.MaterialButton;

public class TonalButtonPreference extends Preference {

	public TonalButtonPreference(@NonNull Context context) {
		super(context);
		init();
	}

	public TonalButtonPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TonalButtonPreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public TonalButtonPreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		setWidgetLayoutResource(R.layout.preference_widget_tonal_button);
		setSelectable(false);
	}

	@Override
	public void onBindViewHolder(PreferenceViewHolder holder) {
		super.onBindViewHolder(holder);
		View view = holder.findViewById(R.id.connect_button);
		if (view instanceof MaterialButton) {
			MaterialButton button = (MaterialButton) view;
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Delegate to Preference's click flow
					TonalButtonPreference.this.performClick();
				}
			});
		}
	}
}

