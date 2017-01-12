package me.dong.recyclerviewstudy.ui.boxes;

import me.dong.recyclerviewstudy.base.BasePresenter;
import me.dong.recyclerviewstudy.base.BaseView;
import me.dong.recyclerviewstudy.model.ABox;


public interface BoxesContract {

    /**
     * View -> Presenter
     */
    interface Presenter extends BasePresenter {

        /**
         * Model Layer(DB, Network)로 저장할 박스를 보낸다.
         * @param aBox
         */
        void addBox(ABox aBox);
    }

    /**
     *
     */
    interface View extends BaseView<Presenter> {
        void showBoxList();
    }
}
