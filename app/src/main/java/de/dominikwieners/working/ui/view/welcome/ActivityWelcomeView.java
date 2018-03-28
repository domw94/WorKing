package de.dominikwieners.working.ui.view.welcome;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import de.dominikwieners.working.data.room.Type;

/**
 * Created by dominikwieners on 13.03.18.
 */

public interface ActivityWelcomeView extends MvpView {
    void showWelcome();

    void hideWelcome();

    void showEmptyFieldMessage();

    void showIsMemeberMessage(Type member);
}
