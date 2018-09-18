package pl.com.wordsweb.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pl.com.wordsweb.entities.Language;
import pl.com.wordsweb.ui.fragments.AddPhraseFragment;

/**
 * Created by wewe on 27.11.16.
 */

public class AddPhraseViewPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private int PAGE_COUNT = 2;
    private Language firstLanguage;
    private Language secondLanguage;

    public AddPhraseViewPagerAdapter(FragmentManager fm, Context context, Language firstLanguage, Language secondLanguage) {
        super(fm);
        this.context = context;
        this.firstLanguage = firstLanguage;
        this.secondLanguage = secondLanguage;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AddPhraseFragment().newInstance(firstLanguage, 1);
            case 1:
                return new AddPhraseFragment().newInstance(secondLanguage, 2);
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
                return (firstLanguage.getName()).toUpperCase();
            case 1:
                return (secondLanguage.getName()).toUpperCase();
        }
        return super.getPageTitle(position);
    }
}
