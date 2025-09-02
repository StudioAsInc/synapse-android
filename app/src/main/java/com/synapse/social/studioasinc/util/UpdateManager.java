package com.synapse.social.studioasinc.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.synapse.social.studioasinc.R;
import com.synapse.social.studioasinc.RequestNetwork;
import com.synapse.social.studioasinc.RequestNetworkController;

import java.util.HashMap;

public class UpdateManager {

    private final Activity activity;
    private final Runnable onUpdateNotRequired;

    public UpdateManager(Activity activity, Runnable onUpdateNotRequired) {
        this.activity = activity;
        this.onUpdateNotRequired = onUpdateNotRequired;
    }

    public void checkForUpdate() {
        if (!isNetworkAvailable()) {
            onUpdateNotRequired.run();
            return;
        }

        final int currentVersionCode;
        try {
            currentVersionCode = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            showErrorDialog("Version check failed: " + e.getMessage());
            e.printStackTrace();
            onUpdateNotRequired.run();
            return;
        }

        RequestNetwork network = new RequestNetwork(activity);
        network.startRequestNetwork(
                RequestNetworkController.GET,
                "https://pastebin.com/raw/sQuaciVv",
                "update",
                new RequestNetwork.RequestListener() {
                    @Override
                    public void onResponse(String tag, String response, HashMap<String, Object> _responseHeaders) {
                        try {
                            HashMap<String, Object> updateMap = new Gson().fromJson(
                                    response,
                                    new TypeToken<HashMap<String, Object>>() {
                                    }.getType()
                            );

                            double latestVersionCode = 0;
                            if (updateMap.containsKey("versionCode")) {
                                Object vc = updateMap.get("versionCode");
                                if (vc instanceof Double) {
                                    latestVersionCode = (Double) vc;
                                } else if (vc instanceof String) {
                                    latestVersionCode = Double.parseDouble((String) vc);
                                } else if (vc instanceof Number) {
                                    latestVersionCode = ((Number) vc).doubleValue();
                                }
                            }

                            if (latestVersionCode > currentVersionCode) {
                                String title = updateMap.get("title").toString();
                                String versionName = updateMap.get("versionName").toString();
                                String changelog = updateMap.get("whatsNew").toString().replace("\\n", "\n");
                                String updateLink = updateMap.get("updateLink").toString();
                                boolean isCancelable = false;
                                if (updateMap.containsKey("isCancelable")) {
                                    Object ic = updateMap.get("isCancelable");
                                    if (ic instanceof Boolean) {
                                        isCancelable = (Boolean) ic;
                                    } else if (ic instanceof String) {
                                        isCancelable = Boolean.parseBoolean((String) ic);
                                    }
                                }
                                showUpdateDialog(title, versionName, changelog, updateLink, isCancelable);
                            } else {
                                onUpdateNotRequired.run();
                            }
                        } catch (Exception e) {
                            showErrorDialog("Update parsing error: " + e.getMessage());
                            e.printStackTrace();
                            onUpdateNotRequired.run();
                        }
                    }

                    @Override
                    public void onErrorResponse(String tag, String message) {
                        onUpdateNotRequired.run();
                    }
                }
        );
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void showUpdateDialog(final String title, final String versionName, final String changelog, final String updateLink, final boolean isCancelable) {
        AlertDialog.Builder updateDialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_dialog, null);

        updateDialogBuilder.setView(dialogView);
        updateDialogBuilder.setCancelable(isCancelable);

        TextView tvTitle = dialogView.findViewById(R.id.update_title);
        TextView tvVersion = dialogView.findViewById(R.id.update_version);
        TextView tvChangelog = dialogView.findViewById(R.id.update_changelog);
        MaterialButton btnUpdate = dialogView.findViewById(R.id.button_update);
        MaterialButton btnLater = dialogView.findViewById(R.id.button_later);

        tvTitle.setText(title);
        tvVersion.setText("Version " + versionName);
        tvChangelog.setText(changelog);

        final AlertDialog dialog = updateDialogBuilder.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateLink));
                activity.startActivity(intent);
                dialog.dismiss();
                activity.finish();
            }
        });

        btnLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                dialog.dismiss();
                if (isCancelable) {
                    onUpdateNotRequired.run();
                }
            }
        });

        if (!isCancelable) {
            btnLater.setVisibility(View.GONE);
            dialog.setCanceledOnTouchOutside(false);
        } else {
            btnLater.setVisibility(View.VISIBLE);
            dialog.setCanceledOnTouchOutside(true);
        }

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        dialog.show();
    }

    private void showErrorDialog(final String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.error_dialog, null);

        builder.setView(dialogView);
        builder.setCancelable(true);

        TextView tvErrorMessage = dialogView.findViewById(R.id.error_message_textview);
        MaterialButton btnOk = dialogView.findViewById(R.id.ok_button);

        tvErrorMessage.setText(errorMessage);

        final AlertDialog dialog = builder.create();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                dialog.dismiss();
                onUpdateNotRequired.run();
            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        dialog.show();
    }
}
