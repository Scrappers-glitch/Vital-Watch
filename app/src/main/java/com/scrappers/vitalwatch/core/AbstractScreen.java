package com.scrappers.vitalwatch.core;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

/**
 * A factory pattern based class used to create a new fragment screen and
 * add an entry animation for it.
 *
 * @author pavl_g.
 */
public abstract class AbstractScreen extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(getLayoutId(), container, false);
        final CardView animatorView = new CardView(layout.getContext());
        animatorView.setBackground(layout.getBackground());
        animatorView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        animatorView.addView(layout);
        if (getAnimationId() != 0) {
            animatorView.startAnimation(AnimationUtils.loadAnimation(layout.getContext(), getAnimationId()));
        }
        return animatorView;
    }

    /**
     * Override and return the path to your layout starting from R.layout.
     * @return the layout id.
     */
    public abstract int getLayoutId();

    /**
     * Override and return the path to your animation xml starting from R.anim.
     * @return the animation id.
     */
    public abstract int getAnimationId();
}
