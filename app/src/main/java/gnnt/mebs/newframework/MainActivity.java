package gnnt.mebs.newframework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.beecampus.common.selectSchool.SelectSchoolActivity;

import mvvm.com.test.story.StoryActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        startActivity (new Intent (this,StoryActivity.class));
    }
}
