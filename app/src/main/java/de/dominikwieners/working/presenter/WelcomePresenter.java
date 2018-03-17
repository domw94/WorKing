package de.dominikwieners.working.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import de.dominikwieners.working.Config;
import de.dominikwieners.working.R;
import de.dominikwieners.working.data.Type;
import de.dominikwieners.working.repository.TypeDatabase;
import de.dominikwieners.working.ui.activities.welcome.WelcomeActivity;
import de.dominikwieners.working.ui.view.WelcomeView;

/**
 * Created by dominikwieners on 13.03.18.
 */

public class WelcomePresenter extends MvpBasePresenter<WelcomeView> {

    /**
     * Load Type data from Room db
     *
     * @param context
     * @return
     */
    public List<Type> loadData(Context context) {
        return TypeDatabase
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
    public void delete(Context context, Type type) {
        TypeDatabase.getInstance(context).getTypeDao().delete(type);
    }

    /**
     * Insert Type data from Room db
     *
     * @param context
     * @param addedType
     */
    public void insert(Context context, Type addedType) {
        TypeDatabase.getInstance(context).getTypeDao().insertAll(addedType);
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

    public void addNewItemToList(String text, List<Type> typeList, Context context) {
        Type type = new Type(getRandomMaterialColor("400", context), text);
        typeList.add(type);
        insert(context, type);
    }

}
