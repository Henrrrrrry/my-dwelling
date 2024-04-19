import android.os.Bundle;
import android.widget.Button;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfilePageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button followButton = null;
        Button fireAlarmNoti=null;
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// link fire alarm noti to subcribers
            }
        });

        fireAlarmNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               publish noti function
            }
        });
    }
}
