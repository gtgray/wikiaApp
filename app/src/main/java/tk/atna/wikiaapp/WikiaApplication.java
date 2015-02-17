package tk.atna.wikiaapp;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WikiaApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        initContentHelper();
    }

    /**
     * Initializes content helper singleton with application context.
     */
    private void initContentHelper() {
        final String INIT = "init";
        try {
            Class<?> clazz = Class.forName(ContentHelper.class.getName());
            Method init = clazz.getDeclaredMethod(INIT, Context.class);
            init.setAccessible(true);
            init.invoke(null, getApplicationContext());

        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException
                        | IllegalAccessException e) {
            throw new IllegalStateException("Can't initialize ContentHelper", e);
        }
    }
}
