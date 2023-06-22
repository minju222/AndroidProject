package kr.jaen.android.layout;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import kr.jaen.android.layout.databinding.ActivityFramelayoutBinding;

public class FrameLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityFramelayoutBinding binding = ActivityFramelayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_framelayout);

//        ImageView iv01 = findViewById(R.id.imageView01);
//        ImageView iv02 = findViewById(R.id.imageView02);
//        Button btnChangeImage = findViewById(R.id.btnChangeImage);

        binding.btnChangeImage.setOnClickListener(v -> {
            if (binding.imageView01.getVisibility() == View.VISIBLE) {
                binding.imageView01.setVisibility(View.INVISIBLE);
                binding.imageView02.setVisibility(View.VISIBLE);
            }
            else{
                Glide.with(this)
                        .load(Uri.parse("https://a.cdn-hotels.com/gdcs/production53/d592/8ec85890-58f0-11e8-9946-0242ac110009.jpg?impolicy=fcrop&w=1600&h=1066&q=medium"))
                        .into(binding.imageView01);
                binding.imageView01.setVisibility(View.VISIBLE);
                binding.imageView02.setVisibility(View.INVISIBLE);
            }
        });
    }
}