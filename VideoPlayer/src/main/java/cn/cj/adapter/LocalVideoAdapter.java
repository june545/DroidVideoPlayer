package cn.cj.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.cj.media.video.player.R;
import cn.cj.util.LoadFileIconTask;

/**
 * Created by June on 2016/10/5.
 */

public class LocalVideoAdapter extends CursorAdapter {
    private final String TAG = LocalVideoAdapter.class.getSimpleName();
    private int gridViewWidth;

    public LocalVideoAdapter(Context context, Cursor c) {
        super(context, c, false);
    }

    public void setGridViewWidth(int gridViewWidth) {
        this.gridViewWidth = gridViewWidth;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = View.inflate(context, R.layout.adapter_grid_view_item, null);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if(lp == null)
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, gridViewWidth);
        view.setLayoutParams(lp);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        int _id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
        String _title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.TITLE));
        String _display_name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME));
        final String _data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
        String _mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.MIME_TYPE));
        int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION));

        Log.d(TAG, "title: " + _title + "\n");
        Log.d(TAG, "display_name: " + _display_name + "\n");
        Log.d(TAG, "data: " + _data + "\n");
        Log.d(TAG, "mimeType: " + _mimeType + "\n");

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.setViewValue(_id, _data, _title, duration);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("cn.cj.intent.action.player");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse(_data), "video/*");
                context.startActivity(intent);
            }
        });
    }

    class ViewHolder {
        ImageView img;
        TextView title;
        TextView durationTV;

        LoadFileIconTask task;

        ViewHolder(View view){
            img = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
            durationTV = (TextView) view.findViewById(R.id.duration);
        }

        /** 给item中的内容赋值 */
        public void setViewValue(int _id, String _data, String _title, int duration){
            if (task != null){
                task.cancel(true);
                task = null;
            }
            task = new LoadFileIconTask(img.getContext(), img);
            task.execute(_data);

            title.setText(_title);
            durationTV.setText(formatMillisTime(duration));
        }
    }

    public static String formatMillisTime(long millis) {
        if (millis < 1000) {
            return "00:00:00";
        }

        long total_s = millis / 1000;
        int s = (int) (total_s % 60);// 秒

        long total_m = total_s / 60;
        int m = (int) (total_m % 60);

        long total_h = total_m / 60;
        int h = (int) total_h;

        return String.format("%02d:%02d:%02d", h, m, s);
    }

}
