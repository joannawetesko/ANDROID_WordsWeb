package pl.com.wordsweb.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pl.com.wordsweb.R;
import pl.com.wordsweb.ui.fragments.LoginFragment;
import pl.com.wordsweb.ui.fragments.RegisterFragment;


/**
 * Created by wewe on 06.04.16.
 */

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    Context context;
    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a LoginFragment (defined as a static inner class below).

        switch(position){
            case 0:
                return new LoginFragment();
            case 1:
                return new RegisterFragment();
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
                return  context.getString(R.string.tab_section_login);
            case 1:
                return  context.getString(R.string.tab_section_register);

        }
        return null;
    }
}