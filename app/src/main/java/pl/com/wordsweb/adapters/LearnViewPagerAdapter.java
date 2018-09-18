package pl.com.wordsweb.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pl.com.wordsweb.R;
import pl.com.wordsweb.ui.fragments.LearnLevelsFragment;
import pl.com.wordsweb.ui.fragments.LearnListFragment;
import pl.com.wordsweb.ui.fragments.LearnModeFragment;

/**
 * Created by wewe on 01.11.16.
 */

public class LearnViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private int PAGE_COUNT = 3;

    public LearnViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LearnListFragment();
            case 1:
                return new LearnLevelsFragment();
            case 2:
                return new LearnModeFragment();
        }
        return null;
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.tab_section_learn);
            case 1:
                return context.getString(R.string.tab_section_level);
            case 2:
                return context.getString(R.string.tab_section_mode);

        }

        return null;
    }
}
