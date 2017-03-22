package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityTAG_";

    private static final String VIDEO_TITLE = "Hello title";
    private static final String VIDEO_SUBTITLE = "Hello subtitle";
    private static final String VIDEO_URL = "http://techslides.com/demos/sample-videos/small.mp4";
    private static final String VIDEO_TYPE = "videos/mp4";
    private static final int VIDEO_DURATION = 5;

    private CastContext castContext;
    private MenuItem mediaRouteMenuItem;
    private CastSession castSession;

    private SessionManagerListener<CastSession> sessionManagerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        castContext = CastContext.getSharedInstance(this);
        castSession = castContext.getSessionManager().getCurrentCastSession();
        sessionManagerListener = generateSessionManagerListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.browse, menu);
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.m_browse_cast);
        return true;
    }

    @Override
    protected void onResume() {
        castContext.getSessionManager().addSessionManagerListener(sessionManagerListener, CastSession.class);
        super.onResume();
    }

    private MediaInfo buildMediaInfo() {
        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);

        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, VIDEO_SUBTITLE);
        movieMetadata.putString(MediaMetadata.KEY_TITLE, VIDEO_TITLE);

        return new MediaInfo.Builder(VIDEO_URL)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType(VIDEO_TYPE)
                .setMetadata(movieMetadata)
                .setStreamDuration(VIDEO_DURATION * 1000)
                .build();
    }

    public void doMagic(View view) {
        if (castSession == null) {
            Toast.makeText(this, "Connect to Chromecast first", Toast.LENGTH_SHORT).show();
            return;
        }
        RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
        remoteMediaClient.load(buildMediaInfo(), true, 0);
    }

    private SessionManagerListener<CastSession> generateSessionManagerListener() {
        return new SessionManagerListener<CastSession>() {
            @Override
            public void onSessionStarting(CastSession castSession) {

            }

            @Override
            public void onSessionStarted(CastSession castSession, String s) {
                MainActivity.this.castSession = castSession;
            }

            @Override
            public void onSessionStartFailed(CastSession castSession, int i) {

            }

            @Override
            public void onSessionEnding(CastSession castSession) {

            }

            @Override
            public void onSessionEnded(CastSession castSession, int i) {

            }

            @Override
            public void onSessionResuming(CastSession castSession, String s) {

            }

            @Override
            public void onSessionResumed(CastSession castSession, boolean b) {
                MainActivity.this.castSession = castSession;
            }

            @Override
            public void onSessionResumeFailed(CastSession castSession, int i) {

            }

            @Override
            public void onSessionSuspended(CastSession castSession, int i) {

            }
        };
    }
}
