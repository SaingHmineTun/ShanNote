package it.saimao.shannote.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import it.saimao.shannote.databinding.ActivityAboutUsBinding;


public class AboutUsActivity extends AppCompatActivity {

    private ActivityAboutUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initListeners();
    }

    private void initListeners() {

        binding.ibBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        binding.lyEmail.setOnClickListener(v -> onItemClick(0));
        binding.lyFacebook.setOnClickListener(v -> onItemClick(1));
        binding.lyGithub.setOnClickListener(v -> onItemClick(2));
        binding.lyPlayStore.setOnClickListener(v -> onItemClick(3));
    }

    public void onItemClick(int i) {
        Intent intent;
        if (i == 1) {
            try {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/100377671433172"));
            } catch (Exception e) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/100377671433172"));
            }
            startActivity(intent);
        } else if (i == 0) {
            String to = "tmk.muse@gmail.com";
            intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        } else if (i == 2) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SaingHmineTun/ShanNote")));
        } else if (i == 3) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=it.saimao.shannote")));
        }
    }
}
