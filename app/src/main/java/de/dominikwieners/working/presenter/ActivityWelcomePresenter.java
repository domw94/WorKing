package de.dominikwieners.working.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.Menu;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import de.dominikwieners.working.Config;
import de.dominikwieners.working.R;
import de.dominikwieners.working.data.Type;
import de.dominikwieners.working.repository.WorkingDatabase;
import de.dominikwieners.working.ui.view.ActivityWelcomeView;

/**
 * Created by dominikwieners on 13.03.18.
 */

public class ActivityWelcomePresenter extends MvpBasePresenter<ActivityWelcomeView> {

    /**
     * Load Type data from Room db
     *
     * @param context
     * @return
     */
    public List<Type> loadTypeData(Context context) {
        return WorkingDatabase
                .getInstance(context)
                .getTypeDao()
                .getAll();
    }

    /**
     * Delete Type data from Room db
     *
     * @param context
     * @param type
     */
    public void deleteTypeData(Context context, Type type) {
        WorkingDatabase.getInstance(context).getTypeDao().delete(type);
    }

    /**
     * Insert Type data from Room db
     *
     * @param context
     * @param addedType
     */
    public void insertTypeData(Context context, Type addedType) {
        WorkingDatabase.getInstance(context).getTypeDao().insertAll(addedType);
    }

    /**
     * Set a random color for a item
     *
     * @param typeColor
     * @param context
     * @return
     */
    public int getRandomMaterialColor(String typeColor, Context context) {
        int returnColor = Color.GRAY;
        int arrayId = context.getResources().getIdentifier("mdcolor_" + typeColor, "array", context.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    /**
     * Check if Type is in list
     * @param addedType
     * @param typeList
     * @return
     */
    public boolean isInTypeList(Type addedType, List<Type> typeList) {
        for (Type type : typeList) {
            if (type.getType().equals(addedType.getType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets item state of next in OptionsMenu
     *
     * @param menu
     * @param mState
     */
    public void setStateOfNextItem(Menu menu, int mState) {
        if (mState == Config.HIDE_MENU) {
            menu.findItem(R.id.welcome_menu_next).setEnabled(false);
        } else {
            menu.findItem(R.id.welcome_menu_next).setEnabled(true);
        }
    }


    public void goBackFromDialog(DialogInterface dialog, int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog.dismiss();
        }
    }


}
