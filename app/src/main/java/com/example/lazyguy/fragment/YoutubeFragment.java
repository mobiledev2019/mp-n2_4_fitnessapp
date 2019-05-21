package com.example.lazyguy.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lazyguy.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;


public class YoutubeFragment extends Fragment{
    //private static final String API_KEY = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    private static final String API_KEY = "AIzaSyDiknFb_PAk9uXW7xCeK72mkxqvRcr8H3M";
    private static String VIDEO_ID = "EGy39OMyHzw";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.you_tube_api, container, false);

        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(API_KEY, new OnInitializedListener() {
            @Override
            public void onInitializationSuccess(Provider provider, final YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    //player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                    player.loadVideo(VIDEO_ID);
                    player.play();
                    player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onLoading() {

                        }
                        @Override
                        public void onLoaded(String s) {

                        }
                        @Override
                        public void onAdStarted() {

                        }
                        @Override
                        public void onVideoStarted() {
                        }
                        @Override
                        public void onVideoEnded() {
                            player.loadVideo(VIDEO_ID);
                            player.play();
                        }
                        @Override
                        public void onError(YouTubePlayer.ErrorReason errorReason) {

                        }
                    });

                    player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                        @Override
                        public void onPlaying() {
                        }

                        @Override
                        public void onPaused() {

                        }

                        @Override
                        public void onStopped() {

                        }

                        @Override
                        public void onBuffering(boolean b) {

                        }

                        @Override
                        public void onSeekTo(int i) {

                        }
                    });
                }
            }

            @Override
            public void onInitializationFailure(Provider provider, YouTubeInitializationResult error) {
                // YouTube error
                String errorMessage = error.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                Log.d("errorMessage:", errorMessage);
            }

        });
        return rootView;
    }

    public void setVideoId (String text) {
        VIDEO_ID = text;
    }
}