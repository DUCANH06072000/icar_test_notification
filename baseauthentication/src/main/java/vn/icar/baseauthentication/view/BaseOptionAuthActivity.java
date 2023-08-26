package vn.icar.baseauthentication.view;

import android.view.View;

import vn.icar.baseauthentication.R;
import vn.icar.baseauthentication.databinding.ActivityOptionLoginBinding;

/**
 * Created by tatv on 10/10/2022.
 */
public abstract class BaseOptionAuthActivity extends BaseAuthActivity<ActivityOptionLoginBinding> implements View.OnClickListener{
    @Override
    public void onInitial() {
        super.onInitial();
        switch (getSharedPreference().getAuthOption()){
            case 1:
                onOptionLeftPressed();
                break;
            case 2:
                onOptionRightPressed();
                break;
            default:
                if(getSharedPreference().getToken().equals("")){
                    initView();
                    initEvent();
                }else {
                    onLogged();
                }


        }
    }

    @Override
    protected void initView() {
        super.initView();
        binding.txtDescription.setText(getTextDescription());
        binding.btnLeft.setText(getTextOptionLeft());
        binding.btnRight.setText(getTextOptionRight());
    }

    @Override
    protected void initEvent() {
        binding.btnLeft.setOnClickListener(this);
        binding.btnRight.setOnClickListener(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_option_login;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_left) {
            getSharedPreference().setAuthOption(1);
            onOptionLeftPressed();
        } else if (id == R.id.btn_right) {
            onOptionRightPressed();
        }
    }



    @Override
    public void onReceiveQRCode(String qr, long time) {

    }

    @Override
    public void onAuthError() {

    }

    protected abstract void onLogged();
    protected abstract void onOptionLeftPressed();
    protected abstract void onOptionRightPressed();
    protected abstract String getTextDescription();
    protected abstract String getTextOptionLeft();
    protected abstract String getTextOptionRight();

}
