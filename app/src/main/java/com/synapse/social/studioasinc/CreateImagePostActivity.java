package com.synapse.social.studioasinc;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.app.AlertDialog;
import android.content.*;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.os.Bundle;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.*;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.bumptech.glide.Glide;
import com.google.android.material.color.MaterialColors;
import com.google.firebase.FirebaseApp;
import com.theartofdev.edmodo.cropper.*;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yalantis.ucrop.*;
import java.io.*;
import java.io.InputStream;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaMetadataRetriever;
import android.database.Cursor;
import android.provider.MediaStore;
import com.bumptech.glide.request.RequestOptions;
import java.net.URL;
import java.net.MalformedURLException;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.textfield.TextInputLayout;

import com.google.android.material.card.*;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import androidx.core.content.ContextCompat;

public class CreateImagePostActivity extends AppCompatActivity {
	
	public final int REQ_CD_IMAGE_PICKER = 101;
	
	private ProgressDialog SynapseLoadingDialog;
	private void saveBitmapAsPng(Bitmap bitmap) throws IOException {
		_LoadingDialog(true);
		Calendar cc = Calendar.getInstance();
		
		File getCacheDir = getExternalCacheDir();
		String getCacheDirName = "cropped_images";
		File getCacheFolder = new File(getCacheDir, getCacheDirName);
		getCacheFolder.mkdirs();
		File getImageFile = new File(getCacheFolder, cc.getTimeInMillis() + ".png");
		String savedFilePath = getImageFile.getAbsolutePath();
		
		final FileOutputStream outStream;
		try {
			outStream = new FileOutputStream(getImageFile);
			final Bitmap finalBitmap = bitmap;
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
						outStream.flush();
						outStream.close();
						
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								_LoadingDialog(false);
								intent.setClass(getApplicationContext(), CreateImagePostNextStepActivity.class);
								intent.putExtra("type", "local");
								intent.putExtra("path", savedFilePath);
								startActivity(intent);
								finish();
							}
						});
					} catch (IOException e) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								_LoadingDialog(false);
								Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
							}
						});
						e.printStackTrace();
					}
				}
			}).start();
			
		} catch (IOException e) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					_LoadingDialog(false);
					Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
				}
			});
			throw e;
		}
	}
	
	private String AddFromUrlStr = null;
	
	private ArrayList<HashMap<String, Object>> imagesListMap = new ArrayList<>();
	
	private LinearLayout main;
	private LinearLayout top;
	private CropImageView cropImageView;
	private LinearLayout urlImagePreview;
	private LinearLayout body;
	private ImageView back;
	private TextView title;
	private LinearLayout topSpc;
	private Button continueButton;
	private ImageView urlImagePreviewImage;
	private RecyclerView imagesView;
	private LinearLayout bottomButtons;
	private Button selectGallery;
	private Button From_url;
	
	private Intent intent = new Intent();
	private Intent IMAGE_PICKER = new Intent(Intent.ACTION_GET_CONTENT);
	private AlertDialog cd;
	private AlertDialog.Builder Dialogs;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.create_image_post);
		initialize(_savedInstanceState);
		FirebaseApp.initializeApp(this);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		main = findViewById(R.id.main);
		top = findViewById(R.id.top);
		cropImageView = findViewById(R.id.cropImageView);
		urlImagePreview = findViewById(R.id.urlImagePreview);
		body = findViewById(R.id.body);
		back = findViewById(R.id.back);
		title = findViewById(R.id.title);
		topSpc = findViewById(R.id.topSpc);
		continueButton = findViewById(R.id.continueButton);
		urlImagePreviewImage = findViewById(R.id.urlImagePreviewImage);
		imagesView = findViewById(R.id.imagesView);
		bottomButtons = findViewById(R.id.bottomButtons);
		selectGallery = findViewById(R.id.selectGallery);
		From_url = findViewById(R.id.From_url);
		IMAGE_PICKER.setType("image/*");
		IMAGE_PICKER.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		Dialogs = new AlertDialog.Builder(this);
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				onBackPressed();
			}
		});
		
		continueButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (AddFromUrlStr == null) {
					try {
						saveBitmapAsPng(cropImageView.getCroppedImage());
					} catch (IOException e) {
						
					}
				} else {
					intent.setClass(getApplicationContext(), CreateImagePostNextStepActivity.class);
					intent.putExtra("type", "url");
					intent.putExtra("path", AddFromUrlStr);
					startActivity(intent);
					finish();
				}
			}
		});
		
		selectGallery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (Build.VERSION.SDK_INT >= 23) {
					if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_DENIED || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_DENIED) {
						requestPermissions(new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
					} else {
						Intent sendImgInt = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(sendImgInt, REQ_CD_IMAGE_PICKER);
					}
				} else {
					Intent sendImgInt = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(sendImgInt, REQ_CD_IMAGE_PICKER);
				}
			}
		});
		
		From_url.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_AddFromUrlDialog();
			}
		});
	}
	
	private void initializeLogic() {
		Display display = getWindowManager().getDefaultDisplay();
		int screenHeight = display.getHeight();
		int desiredHeight = screenHeight * 1 / 2 - 24;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, desiredHeight);
		
		cropImageView.setLayoutParams(params);
		urlImagePreview.setLayoutParams(params);
		_stateColor(0xFFFFFFFF, 0xFFFFFFFF);
		_viewGraphics(back, 0xFFFFFFFF, 0xFFE0E0E0, 300, 0, Color.TRANSPARENT);
		imagesView.setAdapter(new ImagesViewAdapter(imagesListMap));
		GridLayoutManager imagesViewGridLayout = new GridLayoutManager(this, 4);
		imagesView.setLayoutManager(imagesViewGridLayout);
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_DENIED || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_DENIED) {
				requestPermissions(new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
			} else {
				_getImageFiles();
			}
		} else {
			_getImageFiles();
		}
		cropImageView.setAspectRatio(4, 3);
		cropImageView.setFixedAspectRatio(false);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_IMAGE_PICKER:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				intent.setClass(getApplicationContext(), CreateImagePostNextStepActivity.class);
				intent.putExtra("type", "url");
				intent.putExtra("path", _filePath.get((int)(0)));
				startActivity(intent);
				finish();
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	public void _ImageColor(final ImageView _image, final int _color) {
		_image.setColorFilter(_color,PorterDuff.Mode.SRC_ATOP);
	}
	
	
	public void _viewGraphics(final View _view, final int _onFocus, final int _onRipple, final double _radius, final double _stroke, final int _strokeColor) {
		android.graphics.drawable.GradientDrawable GG = new android.graphics.drawable.GradientDrawable();
		GG.setColor(_onFocus);
		GG.setCornerRadius((float)_radius);
		GG.setStroke((int) _stroke, _strokeColor);
		android.graphics.drawable.RippleDrawable RE = new android.graphics.drawable.RippleDrawable(new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{ _onRipple}), GG, null);
		_view.setBackground(RE);
	}
	
	
	public void _getImageFiles() {
		String[] projection = {
			MediaStore.Images.Media.DATA
		};
		Cursor imagesCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,null,null,null);
		
		if (imagesCursor != null && imagesCursor.moveToFirst()) {
			do {
				String imagePath = imagesCursor.getString(imagesCursor.getColumnIndex(MediaStore.Images.Media.DATA));
				if (imagePath.endsWith(".png") || (imagePath.endsWith(".jpg") || imagePath.endsWith(".jpeg"))) {
					HashMap<String, Object> mediaItem = new HashMap<>();
					mediaItem.put("type", "Image");
					mediaItem.put("path", imagePath);
					imagesListMap.add(mediaItem);
				}
			} while (imagesCursor.moveToNext());
			imagesCursor.close();
		}
		
		imagesView.getAdapter().notifyDataSetChanged();
		_loadCropImage(imagesListMap.get((int)0).get("path").toString(), false);
	}
	
	
	public void _stateColor(final int _statusColor, final int _navigationColor) {
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		getWindow().setStatusBarColor(_statusColor);
		getWindow().setNavigationBarColor(_navigationColor);
	}
	
	
	public void _LoadingDialog(final boolean _visibility) {
		if (_visibility) {
			if (SynapseLoadingDialog== null){
				SynapseLoadingDialog = new ProgressDialog(this);
				SynapseLoadingDialog.setCancelable(false);
				SynapseLoadingDialog.setCanceledOnTouchOutside(false);
				
				SynapseLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
				SynapseLoadingDialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT));
				
			}
			SynapseLoadingDialog.show();
			SynapseLoadingDialog.setContentView(R.layout.loading_synapse);
			
			LinearLayout loading_bar_layout = (LinearLayout)SynapseLoadingDialog.findViewById(R.id.loading_bar_layout);
			
			
			//loading_bar_layout.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)100, 0xFFFFFFFF));
		} else {
			if (SynapseLoadingDialog != null){
				SynapseLoadingDialog.dismiss();
			}
		}
		
	}
	
	
	public void _loadCropImage(final String _path, final boolean _isUrl) {
		if (!_isUrl) {
			AddFromUrlStr = null;
			java.io.File file = new java.io.File(_path);
			Uri uri = Uri.fromFile(file);
			cropImageView.setImageUriAsync(uri);
			urlImagePreview.setVisibility(View.GONE);
			cropImageView.setVisibility(View.VISIBLE);
		} else {
			AddFromUrlStr = _path;
			Glide.with(getApplicationContext()).load(Uri.parse(_path)).into(urlImagePreviewImage);
			urlImagePreview.setVisibility(View.VISIBLE);
			cropImageView.setVisibility(View.GONE);
		}
	}
	
	
	public void _AddFromUrlDialog() {
		MaterialAlertDialogBuilder Dialogs = new MaterialAlertDialogBuilder(CreateImagePostActivity.this);
		Dialogs.setTitle("Add through url");
		View EdittextDesign = LayoutInflater.from(CreateImagePostActivity.this).inflate(R.layout.single_et, null);
		Dialogs.setView(EdittextDesign);
		final EditText edittext1 = EdittextDesign.findViewById(R.id.edittext1);
		final TextInputLayout textinputlayout1 = EdittextDesign.findViewById(R.id.textinputlayout1);
		edittext1.setFocusableInTouchMode(true);
		Dialogs.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				if (!edittext1.getText().toString().trim().equals("")) {
					if (_checkValidUrl(edittext1.getText().toString().trim())) {
						AddFromUrlStr = edittext1.getText().toString().trim();
						cd.dismiss();
					}
				}
			}
		});
		Dialogs.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				
			}
		});
		androidx.appcompat.app.AlertDialog edittextDialog = Dialogs.create();
		
		edittextDialog.setCancelable(true);
		edittextDialog.show();
	}
	
	
	public boolean _checkValidUrl(final String _url) {
		try {
			new URL(_url);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}
	
	public class ImagesViewAdapter extends RecyclerView.Adapter<ImagesViewAdapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public ImagesViewAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getLayoutInflater();
			View _v = _inflater.inflate(R.layout.synapse_create_img_post_image_cv, null);
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}
		
		@Override
		public void onBindViewHolder(ViewHolder _holder, final int _position) {
			View _view = _holder.itemView;
			
			final RelativeLayout relative = _view.findViewById(R.id.relative);
			final androidx.cardview.widget.CardView main = _view.findViewById(R.id.main);
			final LinearLayout relativeTop = _view.findViewById(R.id.relativeTop);
			final ImageView image = _view.findViewById(R.id.image);
			final LinearLayout relativeSpc = _view.findViewById(R.id.relativeSpc);
			final ImageView typeIcon = _view.findViewById(R.id.typeIcon);
			final LinearLayout rvtSpc = _view.findViewById(R.id.rvtSpc);
			final TextView mediaDuration = _view.findViewById(R.id.mediaDuration);
			
			_ImageColor(typeIcon, 0xFFFFFFFF);
			typeIcon.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)24, 0x7B000000));
			mediaDuration.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)24, 0x7B000000));
			typeIcon.setImageResource(R.drawable.image_ic);
			mediaDuration.setVisibility(View.GONE);
			relative.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					_loadCropImage(_data.get((int)_position).get("path").toString(), false);
				}
			});
			image.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(_data.get((int)_position).get("path").toString(), 1024, 1024));
		}
		
		@Override
		public int getItemCount() {
			return _data.size();
		}
		
		public class ViewHolder extends RecyclerView.ViewHolder {
			public ViewHolder(View v) {
				super(v);
			}
		}
	}
}