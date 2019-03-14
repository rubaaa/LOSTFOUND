package com.example.losts.Posts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Base64;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class GalleryCameraUtils {
    private Activity context;

    public GalleryCameraUtils(Activity context) {
        this.context = context;
    }

    public void onGetPic(final Activity context, final Fragment mFragment, final int PICK_IMAGE_CAMERA, final int PICK_IMAGE, String DialogMessage, String DialogTitle, String CameraBtnText, String GalleryBtnText, GetImageData getImageData) {

        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(context);
        mAlertDialog.setMessage(DialogMessage);
        mAlertDialog.setTitle(DialogTitle);
        mAlertDialog.setNeutralButton(CameraBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mFragment!=null) {
                    Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mFragment.startActivityForResult(mIntent, PICK_IMAGE_CAMERA);
                    dialog.dismiss();
                } else {
                    Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    context.startActivityForResult(mIntent, PICK_IMAGE_CAMERA);
                    dialog.dismiss();
                }

            }
        });
        mAlertDialog.setPositiveButton(GalleryBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mFragment!=null) {
//                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                    getIntent.setType("image/*");
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");
//                    Intent chooserIntent = Intent.createChooser(pickIntent, "Select Profile Picture");
//                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
                    mFragment.startActivityForResult(pickIntent, PICK_IMAGE);
                    dialog.dismiss();
                } else {
//                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                    getIntent.setType("image/*");
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");
//                    Intent chooserIntent = Intent.createChooser(pickIntent, "Select Profile Picture");
//                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
                    context.startActivityForResult(pickIntent, PICK_IMAGE);
                    dialog.dismiss();
                }
            }
        });
        AlertDialog show = mAlertDialog.show();

    }

    public void onGetPicOnActivityResult(Activity basePresenter, int resultCode, int requestCode, Intent data, int ImgWidth, int ImgHeight, int PICK_IMAGE_CAMERA, int PICK_IMAGE ) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                Uri filePath = null;
                filePath = data.getData();
                String realPathFromURI = getRealPathFromURI(context, filePath);
                String filename = realPathFromURI.substring(realPathFromURI.lastIndexOf("/") + 1);
                try {
                    //Getting the Bitmap from Gallery
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
                    bitmap = bitmap.createScaledBitmap(bitmap, ImgWidth, ImgHeight, false);
                    if (basePresenter instanceof GetImageData) {
                        ((GetImageData) basePresenter).onGetImageData(filename, bitmap, filePath, realPathFromURI);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == PICK_IMAGE_CAMERA) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Uri filePath = getImageUri(context, bitmap);
                String realPathFromURI = getRealPathFromURI(context, filePath);
                String filename = realPathFromURI.substring(realPathFromURI.lastIndexOf("/") + 1);
                bitmap = bitmap.createScaledBitmap(bitmap, ImgWidth, ImgHeight, false);

                if (basePresenter instanceof GetImageData) {
                    ((GetImageData) basePresenter).onGetImageData(filename, bitmap, filePath, realPathFromURI);
                }
            }
        }
    }

    public interface GetImageData {
        public void onGetImageData(String Name, Bitmap bitmap, Uri filePath, String realPathFromURI);
    }

    //Get Path From Uri
    private static String getRealPathFromURI(Activity context, Uri contentURI) {
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    //Get Uri For Image
    private static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return Uri.parse(path);
    }

    // Encode Bitmap Image To Base 64
    public String encodeImageBase64(Activity context, Bitmap Bitmap) {
        byte[] byteArrayImage = getFileDataFromBitmap(Bitmap);
        String mEncodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        return mEncodedImage;
    }

    public Bitmap decodedBase64(String img64) {
        byte[] bytes = Base64.decode(img64, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }

    public byte[] getFileDataFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
