package me.dong.base.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import me.dong.base.utils.BusUtils;

public abstract class BaseFragment extends Fragment {

    protected static final String PARAM_BUNDLE = "param_bundle";
    protected int fragmentId;

    private Bundle savedState;

    public BaseFragment() {
        fragmentId = (int) System.nanoTime();
        setArguments(new Bundle());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusUtils.BUS.register(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!restoreStateFromArguments()) {
            initialize();
        } else {
            onRestore();
        }
    }

    protected abstract void onRestore();

    protected abstract void initialize();

    protected abstract void onSaveState(Bundle bundle);

    protected abstract void onRestoreState(Bundle bundle);

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveStateToArguments();
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        saveStateToArguments();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        try {
            BusUtils.BUS.unregister(this);
        } catch (Exception ignore) {
        }
        super.onDestroy();
    }

    private void saveStateToArguments() {
        if (getView() != null) {
            savedState = saveState();
        }
        if (savedState != null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                bundle.putBundle(PARAM_BUNDLE, savedState);
            }
        }
    }

    private boolean restoreStateFromArguments() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return false;
        }
        savedState = bundle.getBundle(PARAM_BUNDLE);
        if (savedState == null) {
            return false;
        }
        restoreState();
        return true;
    }

    private void restoreState() {
        if (savedState != null) {
            onRestoreState(savedState);
        }
    }

    private Bundle saveState() {
        Bundle state = new Bundle();
        onSaveState(state);
        return state;
    }

    public boolean onBackPressed() {
        return false;
    }

    public int getFragmentId() {
        return fragmentId;
    }
}
