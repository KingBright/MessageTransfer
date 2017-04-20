package name.kingbright.messagetransfer.ui;

import com.dexafree.materialList.card.CardContentProvider;

/**
 * Created by jinliang on 2017/4/20.
 */

public class WelcomeCardProvider extends CardContentProvider {
    @Override
    public String getTitle() {
        return "Welcome";
    }

    @Override
    public String getDescription() {
        return "hello there";
    }
}
