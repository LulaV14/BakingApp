package com.example.lulavillalobos.bakingapp.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lulavillalobos.bakingapp.R;
import com.example.lulavillalobos.bakingapp.dummy.DummyContent;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements StepsFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("step_number", item.id);
        DescriptionFragment descriptionFragment = new DescriptionFragment();
        descriptionFragment.setArguments(bundle);
    }
}
