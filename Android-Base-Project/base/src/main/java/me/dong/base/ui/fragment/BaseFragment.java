package me.dong.base.ui.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.dong.base.utils.BusUtils;

public abstract class BaseFragment extends Fragment {

    protected static final String PARAM_BUNDLE = "param_bundle";
    protected int fragmentId;

    private Bundle savedState;

    protected Unbinder mUnbinder;

    @LayoutRes
    public abstract int getLayoutId();

    public abstract void initView(View rootView);

    public BaseFragment() {
        fragmentId = (int) System.nanoTime();
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
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
        mUnbinder.unbind();
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
