package me.dong.exmvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by Dong on 2016-07-24.
 */
public class StateMaintainer {

    protected final String TAG = getClass().getSimpleName();

    private final String mStateMaintainerTAG;
    private final WeakReference<FragmentManager> mFragmentManager;
    private StateMngFragment mStateMaintainerFrag;

    /**
     * Constructor
     *
     * @param fragmentManager    FragmentManager reference
     * @param stateMaintainerTAG the TAG used to insert the state maintainer fragment
     */
    public StateMaintainer(FragmentManager fragmentManager, String stateMaintainerTAG) {
        mFragmentManager = new WeakReference<FragmentManager>(fragmentManager);
        mStateMaintainerTAG = stateMaintainerTAG;
    }

    /**
     * Create the state maintainer fragment
     *
     * @return true: the frag was created for the first time
     * false: recovering the object
     */
    public boolean firstTimeIn() {

        try {

            // Recovering the reference
            mStateMaintainerFrag = (StateMngFragment)
                    mFragmentManager.get().findFragmentByTag(mStateMaintainerTAG);

            // Creating a new RetainedFragment
            if (mStateMaintainerFrag == null) {
                Log.d(TAG, "Creating a new RetainedFragment " + mStateMaintainerTAG);
                mStateMaintainerFrag = new StateMngFragment();
                mFragmentManager.get().beginTransaction()
                        .add(mStateMaintainerFrag, mStateMaintainerTAG)
                        .commit();
                return true;
            } else {
                Log.d(TAG, "Returns a existent retained fragment existente " + mStateMaintainerTAG);
                return false;
            }
        } catch (NullPointerException e) {
            Log.w(TAG, "Error firstTimeIn()");
            return false;
        }
    }

    /**
     * Insert Object to be preserved during configuration change
     *
     * @param key Object's TAG reference
     * @param obj Object to maintain
     */
    public void put(String key, Object obj) {
        mStateMaintainerFrag.put(key, obj);
    }

    /**
     * Insert Object to be preserved during configuration change
     * Uses the Object's class name as a TAG reference
     * Should only be used one time by type class
     *
     * @param obj Object to maintain
     */
    public void put(Object obj) {
        put(obj.getClass().getName(), obj);
    }

    /**
     * Recovers saved object
     *
     * @param key TAG reference
     * @param <T> Class type
     * @return Objects
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return mStateMaintainerFrag.get(key);
    }

    /**
     * Verify the object existence
     *
     * @param key Obj TAG
     */
    private boolean hasKey(String key) {
        return mStateMaintainerFrag.get(key) != null;
    }

    /**
     * Save and manages objects that show be preserved
     * during configuration changes.
     */
    public static class StateMngFragment extends Fragment {
        private HashMap<String, Object> mData = new HashMap<>();

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Grants that the frag will be preserved
            setRetainInstance(true);
        }

        /**
         * Insert objects
         *
         * @param key reference TAG
         * @param obj Object to save
         */
        public void put(String key, Object obj) {
            mData.put(key, obj);
        }

        /**
         * Insert obj using class name as TAG
         *
         * @param obj obj to save
         */
        public void put(Object obj) {
            put(obj.getClass().getName(), obj);
        }

        /**
         * Recover obj
         *
         * @param key reference TAG
         * @param <T> Class
         * @return Obj saved
         */
        @SuppressWarnings("unchecked")
        public <T> T get(String key) {
            return (T) mData.get(key);
        }


    }


}
