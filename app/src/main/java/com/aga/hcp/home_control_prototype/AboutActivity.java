package com.aga.hcp.home_control_prototype;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_about, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            TextView aboutBox = (TextView)view.findViewById(R.id.aboutTextView);
            aboutBox.setText(Html.fromHtml("HCP is an app designed to streamline home automation devices. Developed by Jyotheeswar, Anson, and Gary for ECE1780H. You can contact us if you have any questions."));

            ImageButton mailButton = (ImageButton)view.findViewById(R.id.sendMailImageButton);
            mailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent mailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:tadgh@cs.toronto.edu"));
                    mailIntent.putExtra(Intent.EXTRA_SUBJECT, "HCP Feedback!");
                    startActivity(mailIntent);
                }
            });
        }
    }
}
