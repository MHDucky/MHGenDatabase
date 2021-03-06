package com.ghstudios.android.features.weapons;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;

import com.ghstudios.android.mhgendatabase.R;
import com.ghstudios.android.GenericActivity;
import com.ghstudios.android.MenuSection;

public class WeaponSelectionListActivity extends GenericActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.weapons);

        // Tag as top level activity
        super.setAsTopLevel();
    }

    @Override
    protected int getSelectedSection() {
        return MenuSection.WEAPONS;
    }

    @Override
    protected Fragment createFragment() {
        return new WeaponSelectionListFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

}
