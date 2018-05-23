
为什么会不一样呢？

Android 4.4（含）开始，通过方式访问图库后，返回的Uri如下（访问“最近”）：
```
Uri is：content://com.android.providers.media.documents/document/image%3A18838
2 Uri.getPath is ：/document/image:18838
3 对应的图片真实路径：/storage/emulated/0/Pictures/Screenshots/Screenshot_2014-09-22-21-40-53.png</span>
```
不但如此，对于不同类型图库，返回的Uri形式并不相同（访问普通相册）：
```
[javascript] view plain copy
Uri is：content://media/external/images/media/18822
2 Uri.getPath is ：/external/images/media/18822
3 对应的图片真实路径：/storage/emulated/0/Download/20130224235013.jpg</span>
```
而4.4之前返回的Uri只存在一种形式，如下：
```
[javascript] view plain copy
Uri is：content://media/external/images/media/14046
2 Uri.getPath is ：/external/images/media/14046
3 对应的图片真实路径：/storage/emulated/0/DCIM/Camera/20130224235013.jpg
```
因此，在Android 4.4或更高版本设备上，通过简单的getDataColumn(Context, Uri, null, null)进行图片数据库已经不能满足所有需求，因此在获取图片真实路径的时候需要根据不同类型区分对待。

版本判断：
```
[javascript] view plain copy
//版本比较：是否是4.4及以上版本
    final boolean mIsKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
```
从相册选择照片方法比较：
```
[javascript] view plain copy
/**
     * <br>功能简述:4.4及以上从相册选择照片
     * <br>功能详细描述:
     * <br>注意:
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void SelectImageUriAfterKikat() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, SELECET_A_PICTURE_AFTER_KIKAT);
    }
```
```
[javascript] view plain copy
/**
     * <br>功能简述:4.4以下从相册选照片并剪切
     * <br>功能详细描述:
     * <br>注意:
     */
    private void cropImageUri() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 640);
        intent.putExtra("outputY", 640);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, SELECT_A_PICTURE);
    }
```
4.4及以上选取照片后需要调用剪切方法：
```
[javascript] view plain copy
/**
     * <br>功能简述: 4.4及以上选取照片后剪切方法
     * <br>功能详细描述:
     * <br>注意:
     * @param uri
     */
    private void cropImageUriAfterKikat(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 640);
        intent.putExtra("outputY", 640);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true); //返回数据bitmap
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, SET_ALBUM_PICTURE_KITKAT);
    }
```
拍照通用方法，对返回数据做处理：

```
[javascript] view plain copy
/**
     * <br>功能简述:对拍照的图片剪切
     * <br>功能详细描述:
     * <br>注意:
     * @param uri
     */
    private void cameraCropImageUri(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/jpeg");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 640);
        intent.putExtra("outputY", 640);
        intent.putExtra("scale", true);
        if (mIsKitKat) {
            intent.putExtra("return-data", true);
        } else {
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, SET_PICTURE);
    }
```
显示等处理如下：
```
[javascript] view plain copy
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_A_PICTURE) {
            if (resultCode == RESULT_OK && null != data) {
                Log.i("zou", "4.4以下的");
                Bitmap bitmap = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH,
                        TMP_IMAGE_FILE_NAME)));
                mAcountHeadIcon.setImageBitmap(bitmap);
            }
        } else if (requestCode == SELECET_A_PICTURE_AFTER_KIKAT) {
            if (resultCode == RESULT_OK && null != data) {
                Log.i("zou", "4.4以上上的");
                mAlbumPicturePath = getPath(MainActivity.this, data.getData());
                cropImageUriAfterKikat(Uri.fromFile(new File(mAlbumPicturePath)));
            }
        } else if (requestCode == SET_ALBUM_PICTURE_KITKAT) {
            Log.i("zou", "4.4以上上的 RESULT_OK");
            Bitmap bitmap = data.getParcelableExtra("data");
            mAcountHeadIcon.setImageBitmap(bitmap);
        } else if (requestCode == TAKE_A_PICTURE) {
            Log.i("zou", "resultCode:" + resultCode);
            cameraCropImageUri(Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
        } else if (requestCode == SET_PICTURE) {
            Log.i("zou", "SET_PICTURE-resultCode:" + resultCode);

            Bitmap bitmap = null;
            if (mIsKitKat) {
                if (null != data) {
                    bitmap = data.getParcelableExtra("data");
                }
            } else {
                bitmap = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
            }
            mAcountHeadIcon.setImageBitmap(bitmap);
        }
    }
```