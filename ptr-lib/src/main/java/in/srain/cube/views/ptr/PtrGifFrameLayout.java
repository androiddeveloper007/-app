package in.srain.cube.views.ptr;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Hank on 2016/6/15.
 * Description:
 */
public class PtrGifFrameLayout extends PtrFrameLayout {

    private PtrGifHeader mPtrClassicHeader;

    public PtrGifFrameLayout(Context context) {
        super(context);
        initViews();
    }

    public PtrGifFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PtrGifFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        mPtrClassicHeader = new PtrGifHeader(getContext());
        setHeaderView(mPtrClassicHeader);
        addPtrUIHandler(mPtrClassicHeader);
    }

    public PtrGifHeader getHeader() {
        return mPtrClassicHeader;
    }

}
