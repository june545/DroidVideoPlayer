package cn.cj.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import cn.cj.adapter.LocalVideoAdapter;
import cn.cj.media.video.player.R;

/**
 * list local video
 */
public class LocalVideoActivity extends AppCompatActivity {
    private GridView gridView;
    private boolean layouted = false;
    private int columnWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.local_video_title);
        setContentView(R.layout.activity_local_video);
        gridView = (GridView) findViewById(R.id.grid_view);
        gridView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (!layouted && right > 0) {
                    columnWidth = gridView.getColumnWidth();
                    layouted = true;
                    getSupportLoaderManager().initLoader(0, null, loaderCallback);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_online:
                startActivity(new Intent(getApplicationContext(), OnlineVideoActivity.class));
                return true;
            case R.id.action_about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * asynchronously player_loading
     */
    LoaderManager.LoaderCallbacks<Cursor> loaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader loader = new CursorLoader(getApplicationContext());
            loader.setUri(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            String[] projection = {MediaStore.Video.VideoColumns._ID,
                    MediaStore.Video.VideoColumns.TITLE,
                    MediaStore.Video.VideoColumns.DISPLAY_NAME,
                    MediaStore.Video.VideoColumns.DATA,
                    MediaStore.Video.VideoColumns.MIME_TYPE,
                    MediaStore.Video.VideoColumns.DURATION};
            loader.setProjection(projection);
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            LocalVideoAdapter adapter = new LocalVideoAdapter(getApplicationContext(), cursor);
            adapter.setGridViewWidth(columnWidth);
            gridView.setAdapter(adapter);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

}
