package tk.atna.wikiaapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class that contains instrumental methods
 */
public class Utils {

    /**
     * First tries to find fragment by tag in fragment manager.
     * Otherwise, creates it or initializes (with data or not, optional).
     * Sets retainance. Loads into container with tag.
     *
     * @param fm link to fragment manager
     * @param container resource to park fragment to
     * @param clazz type of fragment to park
     * @param init if fragment is needed to be initialized
     * @param initData data to init fragment with
     * @param retainable if fragment is needed to be retainable
     * @param <T> child of Fragment class
     */
    public static <T extends Fragment> void parkFragment(FragmentManager fm, int container,
                                                         Class<T> clazz,
                                                         boolean init, Bundle initData,
                                                         boolean retainable) {
        Fragment fragment = findFragment(fm, clazz);
        if (fragment == null) {
            fragment = init ? initFragment(clazz, initData) : createFragment(clazz);

            fragment.setRetainInstance(retainable);

            fm.beginTransaction()
                    .replace(container, fragment, findTag(clazz))
                    .commit();

            fm.executePendingTransactions();
        }
    }

    /**
     * Removes fragment of type clazz from fragment manager
     *
     * @param fm link to fragment manager
     * @param clazz type of fragment to unpark
     * @param <T> child of Fragment class
     */
    public static <T extends Fragment> void unparkFragment(FragmentManager fm, Class<T> clazz) {
        Fragment fragment = findFragment(fm, clazz);
        if(fragment == null)
            return;

        fm.beginTransaction()
          .remove(fragment)
          .commit();
    }

    /**
     * Tries to find fragment of type clazz in fragment manager
     *
     * @param fm link to fragment manager
     * @param clazz type of fragment to
     * @param <T> child of Fragment class
     * @return found fragment or null
     */
    @SuppressWarnings("unchecked")
    public static <T extends Fragment> T findFragment(FragmentManager fm, Class<T> clazz) {
        return (T) fm.findFragmentByTag(findTag(clazz));
    }

    /**
     * Shows dialog fragment of type clazz initialized with initData (or not, optional)
     *
     * @param fm link to fragment manager
     * @param clazz type of dialog fragment to show
     * @param initData data to init dialog with
     * @param <T> child of dialog fragment class
     */
    public static <T extends DialogFragment> void showDialog(FragmentManager fm, Class<T> clazz,
                                                             Bundle initData) {
        DialogFragment dialog = findFragment(fm, clazz);
        if (dialog == null) {
            dialog = initFragment(clazz, initData);

        } else
            dialog.dismiss();

        dialog.show(fm, findTag(clazz));
    }

    /**
     *  Searches for static field TAG in clazz type
     *
     * @param clazz type of fragment to find tag for
     * @param <T> child of Fragment class
     * @return value of TAG field or null
     */
    private static <T extends Fragment> String findTag(Class<T> clazz) {

        final String TAG = "TAG";

        try {
            Field tag = clazz.getDeclaredField(TAG);
            return (String) tag.get(null);

        } catch (IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Log.d("myLogs", Utils.class.getSimpleName() + "findTag: " + clazz.getSimpleName()
                                    + " class must have static final field 'TAG'");
        }
        return null;
    }

    /**
     * Creates fragment of type clazz
     *
     * @param clazz type of fragment to create
     * @param <T> child of Fragment class
     * @return created fragment or null
     */
    private static <T extends Fragment> T createFragment(Class<T> clazz) {

        try {
            return clazz.newInstance();

        } catch (ClassCastException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tries to initialize fragment of type clazz by one of three methods:
     * init(Class, Bundle), init(Bundle), init().
     *
     * @param clazz type of fragment to initialize
     * @param data data to initialize fragment with
     * @param <T> child of Fragment class
     * @return initialized fragment or null
     */
    private static <T extends Fragment> T initFragment(Class<T> clazz, Bundle data) {

        final String INIT = "init";

        try {
            Method init;
            Object fragment;

            if (data != null) {
                try {
                    init = clazz.getMethod(INIT, Class.class, Bundle.class);
                    fragment = init.invoke(null, clazz, data);

                } catch (NoSuchMethodException e) {
                    init = clazz.getMethod(INIT, Bundle.class);
                    fragment = init.invoke(null, data);
                }
            } else {
                init = clazz.getMethod(INIT);
                fragment = init.invoke(null);
            }
            return clazz.cast(fragment);

        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Wraps object of type clazz to bundle
     *
     * @param value object to wrap
     * @param clazz type of object to put into bundle
     * @param <T> any parcelable object
     * @return bundle with serialized object or null
     */
    public static <T> Bundle toBundle(T value, Class<T> clazz) {
        try {
            Bundle data = new Bundle();
            data.putParcelable(clazz.toString(), (Parcelable) value);
            return data;

        } catch (NullPointerException | ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Unwraps object of type clazz from bundle
     *
     * @param source bundle to get object from
     * @param clazz type of object to get from bundle
     * @param <T> any parcelable object
     * @return deserialized object or null
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromBundle(Bundle source, Class<T> clazz) {
        try {
            return (T) source.getParcelable(clazz.toString());

        } catch (NullPointerException | ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

}
