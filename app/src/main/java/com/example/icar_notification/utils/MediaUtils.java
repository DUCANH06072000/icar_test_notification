package com.example.icar_notification.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.SeekBar;

import com.example.icar_notification.helper.MediaSessionHelper;
import com.example.icar_notification.listener.IMusicListener;
import com.example.icar_notification.model.Music;
import com.example.icar_notification.share.DataMusicStore;
import com.example.icar_notification.share.DataStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MediaUtils {

    private final static String TAG = "MediaUtils";
    private MediaControllerCompat mMediaControllerCompat;
    private DataMusicStore mDataStoreMusic;
    private Context context;
    private boolean isMediaPlaying = false;
    private long mediaCurrentTime = 0;
    private long mediaTotalTime = 0;

    private IMusicListener listener;

    public boolean stateNotification = false;
    public DataStore dataStore;

    private Timer timer;

    private List<Music> dataMusic = new ArrayList<>();

    public MediaUtils(Context context, IMusicListener listener) {
        this.context = context;
        this.listener = listener;
        mDataStoreMusic = new DataMusicStore(context);
        dataStore = new DataStore(context);
        Music youtube = new Music();
        youtube.setNameMusic("Youtube");
        youtube.setPackageName("app.rvx.android.youtube");

        Music zing = new Music();
        zing.setNameMusic("ZingMp3");
        zing.setPackageName("com.zing.mp3");

        dataMusic.add(youtube);
        dataMusic.add(zing);
        timer = new Timer();
        refreshController();
    }


    /**
     * khởi tạo các giá trị
     */
    public void refreshController() {
        mMediaControllerCompat = null;
        try {
            for (int i = 0; i < dataMusic.size(); i++) {
                MediaSessionCompat.Token token = MediaSessionHelper.getMediaSessionTokenForPackage(context, dataMusic.get(i).getPackageName());
                Log.e(TAG, token.toString());
                if (token != null) {
                    mMediaControllerCompat = new MediaControllerCompat(context, token);
                    mMediaControllerCompat.registerCallback(mCallback);
                    mCallback.onPlaybackStateChanged(mMediaControllerCompat.getPlaybackState());
                    mCallback.onMetadataChanged(mMediaControllerCompat.getMetadata());
                    stateNotification = true;
                    Log.e(TAG, dataMusic.get(i).getNameMusic());
                } else {
                    stateNotification = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * lắng nghe chơi nhạc bao gồm Tráng thái phát,hình ảnh, title và cả thời gian
     */
    private final MediaControllerCompat.Callback mCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat playbackState) {
            if (playbackState != null) {
                if (playbackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
                    isMediaPlaying = true;
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if (isMediaPlaying && isMediaControllerCompatActive()) {
                                try {
                                    if (mMediaControllerCompat.getPlaybackState() != null) {
                                        mediaCurrentTime = mMediaControllerCompat.getPlaybackState().getPosition();
                                        listener.onChangeTimeMusic(mediaCurrentTime, mediaTotalTime);
                                    } else {
                                        Log.e(TAG, "PlaybackState is NULL");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, 0, 1000);
                    listener.onChangePlaying(1);
                } else {
                    isMediaPlaying = false;
                    listener.onChangePlaying(0);
                }
            }
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            try {
                if (metadata != null) {
                    mediaTotalTime = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
                    // Trích xuất hình ảnh album từ metadata
                    Bitmap albumArt;
                    albumArt = metadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ART);
                    if (albumArt != null) {
                        // Hiển thị hình ảnh album trong một ImageView
                        Log.e(TAG, "album not null");
                    } else {
                        Log.e(TAG, "album null");
                    }
                    listener.onChangeTitleMusic((String) metadata.getText(MediaMetadataCompat.METADATA_KEY_TITLE));
                }
            } catch (Exception e) {
            }
        }

        @Override
        public void onSessionDestroyed() {
            mMediaControllerCompat = null;
        }
    };

    public void onRemoveMusic() {
        if (timer != null) {
            timer.cancel();
        }
        if (mMediaControllerCompat != null) {
            mMediaControllerCompat = null;
        }
    }


    /**
     * kiểm tra xem có tồn tại không bao hay không
     */
    public boolean isMediaControllerCompatActive() {
        return mMediaControllerCompat != null;
    }


    public void onPrevPressed() {
        if (isMediaControllerCompatActive()) {
            mMediaControllerCompat.getTransportControls().skipToPrevious();
        }
    }

    public void onPlayOrPausePressed() {
        if (isMediaControllerCompatActive()) {
            if (isMediaPlaying) {
                onPause();
            } else {
                onPlay();
            }
        }
    }


    public void onPlay() {
        if (isMediaControllerCompatActive()) {
            mMediaControllerCompat.getTransportControls().play();
        }
    }

    public void onPause() {
        if (isMediaControllerCompatActive()) {
            mMediaControllerCompat.getTransportControls().pause();
        }
    }

    public void onNextPressed() {
        if (isMediaControllerCompatActive()) {
            mMediaControllerCompat.getTransportControls().skipToNext();
        }
    }

    public void onTimeMusicChanged(SeekBar seekBar, int progress) {
        if (isMediaControllerCompatActive()) {
            mMediaControllerCompat.getTransportControls().seekTo(progress);
        }
    }

}

