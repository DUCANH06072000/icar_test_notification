package vn.icar.baseauthentication.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tatv.baseapp.api.ApiService;
import com.tatv.baseapp.utils.network.Connectivity;
import com.tatv.baseapp.utils.qr.QRUtils;
import com.tatv.baseapp.utils.ui.UiUtils;
import com.tatv.baseapp.view.dialog.ConfirmDialog;
import com.tatv.baseapp.view.dialog.ErrorDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import vn.icar.baseauthentication.BuildConfig;
import vn.icar.baseauthentication.R;
import vn.icar.baseauthentication.adapter.DeviceAdapter;
import vn.icar.baseauthentication.data.login.BodyLogin;
import vn.icar.baseauthentication.data.login.Login;
import vn.icar.baseauthentication.data.login.LoginListener;
import vn.icar.baseauthentication.data.login.LoginRepository;
import vn.icar.baseauthentication.data.login.TrialRepository;
import vn.icar.baseauthentication.data.logout.LogoutAndLogin;
import vn.icar.baseauthentication.data.logout.LogoutAndLoginRepository;
import vn.icar.baseauthentication.data.model.DataServer;
import vn.icar.baseauthentication.data.model.Device;
import vn.icar.baseauthentication.data.model.QREvent;
import vn.icar.baseauthentication.data.signup.BodySendOTP;
import vn.icar.baseauthentication.data.signup.BodySignUp;
import vn.icar.baseauthentication.data.signup.BodyverifyOTP;
import vn.icar.baseauthentication.data.signup.OTPListener;
import vn.icar.baseauthentication.data.signup.SendOTPRepository;
import vn.icar.baseauthentication.data.signup.SignUpListener;
import vn.icar.baseauthentication.data.signup.SignUpRepository;
import vn.icar.baseauthentication.data.signup.VerifyOTPRepository;
import vn.icar.baseauthentication.databinding.ActivityQrLoginAuthBinding;
import vn.icar.baseauthentication.forgot_password.BodyForgotPassword;
import vn.icar.baseauthentication.forgot_password.BodySendOTPForgotPassword;
import vn.icar.baseauthentication.forgot_password.BodyverifyOTPForgotPassword;
import vn.icar.baseauthentication.forgot_password.ForgotPasswordListener;
import vn.icar.baseauthentication.forgot_password.ForgotPasswordRepository;
import vn.icar.baseauthentication.forgot_password.OTPForgotPasswordListener;
import vn.icar.baseauthentication.forgot_password.SendOTPForgotPasswordRepository;
import vn.icar.baseauthentication.forgot_password.VerifyOTPForgotPassWordRepository;
import vn.icar.baseauthentication.listener.DeviceClick;
import vn.icar.baseauthentication.socket.SocketManager;

public abstract class BaseQRLoginAuthActivity extends BaseAuthActivity<ActivityQrLoginAuthBinding> implements LoginListener, OTPListener, SignUpListener, OTPForgotPasswordListener, ForgotPasswordListener {
    private static String TAG = "BaseQRLoginAuthActivity";
    private boolean mSendOTP = false, mCheckOTP = false, mConfirmSignUp = false, mSendOTPForgotPassword = false, mCheckOTPForgotPassword = false, mConfirmForgotPassword = false;
    private Connectivity connectivity;
    private String sdt = "", password = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
        connectivity = new Connectivity(context);
    }

    @Override
    protected void initView() {
        super.initView();
        //khi người dùng nhập sdt của màn đăng ký
        binding.layoutSignUp.edtSdtSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mSendOTP) {
                    mSendOTP = false;
                    binding.layoutSignUp.btSendOtp.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_bt_login));
                    binding.layoutSignUp.txtSendOtp.setVisibility(View.GONE);
                    binding.layoutSignUp.txtTimeOtp.setVisibility(View.GONE);
                    binding.layoutSignUp.btSignUp.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_bt_signout));
                    binding.layoutSignUp.txtContentPassword.setVisibility(View.GONE);
                    binding.layoutSignUp.edtPasswordNew.setVisibility(View.GONE);
                    binding.layoutSignUp.edtPasswordNewAgain.setVisibility(View.GONE);
                    mConfirmSignUp = false;
                    mCheckOTP = false;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //khi người dùng nhập sdt của màn quên mật khẩu
        binding.layoutForgotPassword.edtSdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mSendOTPForgotPassword) {
                    mSendOTPForgotPassword = false;
                    binding.layoutForgotPassword.btSendOtp.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_bt_login));
                    binding.layoutForgotPassword.txtSendOtp.setVisibility(View.GONE);
                    binding.layoutForgotPassword.txtTimeOtp.setVisibility(View.GONE);
                    binding.layoutForgotPassword.btConfirm.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_bt_signout));
                    binding.layoutForgotPassword.txtContentPassword.setVisibility(View.GONE);
                    binding.layoutForgotPassword.edtPasswordNew.setVisibility(View.GONE);
                    binding.layoutForgotPassword.edtPasswordNewAgain.setVisibility(View.GONE);
                    mConfirmForgotPassword = false;
                    mCheckOTPForgotPassword = false;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //khi người dùng nhập otp của màn đăng ký
        binding.layoutSignUp.edtOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.layoutSignUp.edtOtp.getText().length() == 6) {
                    if (mCheckOTP) {
                        BodyverifyOTP bodyverifyOTP = new BodyverifyOTP(binding.layoutSignUp.edtSdtSignUp.getText().toString().trim(), binding.layoutSignUp.edtOtp.getText().toString().trim(), getAppId());
                        VerifyOTPRepository verifyOTPRepository = new VerifyOTPRepository(BaseQRLoginAuthActivity.this);
                        verifyOTPRepository.verifyOTPSignup(bodyverifyOTP, context, getUrlApi());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //khi người dùng nhập otp của màn quên mật khẩu
        binding.layoutForgotPassword.edtOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.layoutForgotPassword.edtOtp.getText().length() == 6) {
                    if (mCheckOTPForgotPassword) {
                        BodyverifyOTPForgotPassword bodyverifyOTPForgotPassword = new BodyverifyOTPForgotPassword(binding.layoutForgotPassword.edtSdt.getText().toString().trim(), binding.layoutForgotPassword.edtOtp.getText().toString().trim(), getAppId());
                        VerifyOTPForgotPassWordRepository verifyOTPForgotPassWordRepository = new VerifyOTPForgotPassWordRepository(BaseQRLoginAuthActivity.this);
                        verifyOTPForgotPassWordRepository.verifyOTPForgotPassword(bodyverifyOTPForgotPassword, context, getUrlApi());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Người dùng nhấn nút next ở bàn phím hệ thống của màn đăng ký
        binding.layoutSignUp.edtSdtSignUp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    checkSendOtp();
                }
                return false;
            }
        });
        // Người dùng nhấn nút next ở bàn phím hệ thống của màn đăng ký
        binding.layoutForgotPassword.edtSdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    checkSendOtpForgotPassword();
                }
                return false;
            }
        });
    }

    @Override
    protected void initEvent() {
        super.initEvent();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_qr_login_auth;
    }

    /**
     * Hiển thị view option login
     */
    public void showViewOptionLogin() {
        binding.layoutOptionLogin.viewOptionLogin.setVisibility(View.VISIBLE);
        binding.layoutLogin.viewLogin.setVisibility(View.GONE);
        binding.layoutForgotPassword.viewForgotPassword.setVisibility(View.GONE);
        binding.layoutSignUp.viewSignUp.setVisibility(View.GONE);
        binding.layoutDeviceLimit.viewDeviceLimit.setVisibility(View.GONE);
    }

    /**
     * Hiển thị view login
     */
    public void showViewLogin() {
        binding.layoutOptionLogin.viewOptionLogin.setVisibility(View.GONE);
        binding.layoutLogin.viewLogin.setVisibility(View.VISIBLE);
        binding.layoutForgotPassword.viewForgotPassword.setVisibility(View.GONE);
        binding.layoutSignUp.viewSignUp.setVisibility(View.GONE);
        binding.layoutDeviceLimit.viewDeviceLimit.setVisibility(View.GONE);
    }

    /**
     * Hiển thị view quên mật khẩu
     */
    public void showViewForgotPassword() {
        binding.layoutOptionLogin.viewOptionLogin.setVisibility(View.GONE);
        binding.layoutLogin.viewLogin.setVisibility(View.GONE);
        binding.layoutForgotPassword.viewForgotPassword.setVisibility(View.VISIBLE);
        binding.layoutSignUp.viewSignUp.setVisibility(View.GONE);
        binding.layoutDeviceLimit.viewDeviceLimit.setVisibility(View.GONE);
    }

    /**
     * Hiển thị view giới hạn thiết bị
     */
    public void showViewDeviceLimit() {
        binding.layoutOptionLogin.viewOptionLogin.setVisibility(View.GONE);
        binding.layoutLogin.viewLogin.setVisibility(View.GONE);
        binding.layoutForgotPassword.viewForgotPassword.setVisibility(View.GONE);
        binding.layoutSignUp.viewSignUp.setVisibility(View.GONE);
        binding.layoutDeviceLimit.viewDeviceLimit.setVisibility(View.VISIBLE);
    }

    /**
     * Hiển thị view đăng ký
     */
    public void showViewSignUp() {
        binding.layoutOptionLogin.viewOptionLogin.setVisibility(View.GONE);
        binding.layoutLogin.viewLogin.setVisibility(View.GONE);
        binding.layoutForgotPassword.viewForgotPassword.setVisibility(View.GONE);
        binding.layoutSignUp.viewSignUp.setVisibility(View.VISIBLE);
        binding.layoutDeviceLimit.viewDeviceLimit.setVisibility(View.GONE);
    }

    /**
     * Nhấn dùng thử
     */
    public void onTrial(View view) {
        UiUtils.fixMultiClick(view);
        binding.layoutOptionLogin.progressBar.setVisibility(View.VISIBLE);
        if (connectivity.isNetworkConnected()) {
            BodyLogin bodyLogin = new BodyLogin(UiUtils.getModel(), "", "", getAppId(), getAppVersion(), getMac(), "fcm");
            TrialRepository trialRepository = new TrialRepository(this);
            trialRepository.GetTrial(bodyLogin, context, getUrlApi());
        } else {
            hideProgressbar();
            new ErrorDialog(context, new ErrorDialog.DialogErrorListener() {
                @Override
                public void onRetry(ErrorDialog dialog) {
                    dialog.dismiss();
                    onTrial(view);
                }

                @Override
                public void onCancel(ErrorDialog dialog) {
                    dialog.dismiss();
                }
            }).setContent(getString(R.string.internet_error))
                    .showBottomCancel(true)
                    .show();
        }
    }

    /**
     * ẩn tất cả các progressBar
     */
    public void hideProgressbar() {
        binding.layoutLogin.progressBarLogin.setVisibility(View.GONE);
        binding.layoutSignUp.progressBarSignUp.setVisibility(View.GONE);
        binding.layoutOptionLogin.progressBar.setVisibility(View.GONE);
        binding.layoutForgotPassword.progressBar.setVisibility(View.GONE);
    }

    /**
     * nhấn nút đăng nhập
     */
    public void onLogin(View view) {
        UiUtils.fixMultiClick(view);
        if (connectivity.isNetworkConnected()) {
            showViewLogin();
        } else {
            hideProgressbar();
            new ErrorDialog(context, new ErrorDialog.DialogErrorListener() {
                @Override
                public void onRetry(ErrorDialog dialog) {
                    dialog.dismiss();
                    showViewLogin();
                }

                @Override
                public void onCancel(ErrorDialog dialog) {
                    dialog.dismiss();
                }
            }).setContent("Vui lòng kiểm tra kết nối internet")
                    .showBottomCancel(true)
                    .show();
        }

    }

    /**
     * Nhấn nút quên mật khẩu
     */
    public void onForgotPassword(View view) {
        UiUtils.fixMultiClick(view);
        showViewForgotPassword();
    }

    /**
     * Nhấn vào mặt để xem mật khẩu
     */
    private boolean showEye = false;

    /**
     * Nhấn vào icon mắt để hiển thị hoặc ẩn password
     */
    public void onEye(View view) {
        if (!showEye) {
            binding.layoutLogin.edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            binding.layoutLogin.edtPassword.setSelection(binding.layoutLogin.edtPassword.getText().length());
            binding.layoutLogin.imgEye.setImageResource(R.drawable.ic_eye_open);
            showEye = true;
        } else {
            binding.layoutLogin.edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            binding.layoutLogin.edtPassword.setSelection(binding.layoutLogin.edtPassword.getText().length());
            binding.layoutLogin.imgEye.setImageResource(R.drawable.ic_eye_close);
            showEye = false;
        }

    }

    /**
     * Nhấn nút đăng nhập bằng QR
     */
    public void onLoginQR(View view) {
        showLoginQR();
    }

    /**
     * hiển thị giao diện đăng nhập QR
     */
    private boolean NewQRCode = false, viewQRLogin = true;

    private void showLoginQR() {
        binding.layoutLogin.txtNoteLoginQr.setVisibility(View.VISIBLE);
        binding.layoutLogin.txtNoteLogin.setVisibility(View.INVISIBLE);
        binding.layoutLogin.txtLoginSdt.setTextColor(Color.parseColor("#CCCCCC"));
        binding.layoutLogin.txtLoginQr.setTextColor(Color.parseColor("#000000"));
        binding.layoutLogin.viewLoginQr.setVisibility(View.VISIBLE);
        binding.layoutLogin.viewLoginSdt.setVisibility(View.GONE);
        binding.layoutLogin.imgQrLogin.setVisibility(View.VISIBLE);
        binding.layoutLogin.viewSdt.setVisibility(View.INVISIBLE);
        binding.layoutLogin.viewPassword.setVisibility(View.INVISIBLE);
        binding.layoutLogin.txtForgotPassword.setVisibility(View.INVISIBLE);
        binding.layoutLogin.btConfirm.setVisibility(View.INVISIBLE);
        viewQRLogin = true;
        if (NewQRCode) {
            binding.layoutLogin.imgQrLogin.setVisibility(View.INVISIBLE);
            binding.layoutLogin.btGetQrCode.setVisibility(View.VISIBLE);
        } else {
            binding.layoutLogin.imgQrLogin.setVisibility(View.VISIBLE);
            binding.layoutLogin.btGetQrCode.setVisibility(View.GONE);
        }
    }

    /**
     * Nhấn nút đăng nhập bằng số điện thoại
     */
    public void onLoginSdt(View view) {
        showLoginSdt();

    }

    /**
     * hiển thị giao diện đăng nhập số điện thoại
     */
    public void showLoginSdt() {
        binding.layoutLogin.txtNoteLoginQr.setVisibility(View.INVISIBLE);
        binding.layoutLogin.btGetQrCode.setVisibility(View.GONE);
        binding.layoutLogin.txtNoteLogin.setVisibility(View.VISIBLE);
        binding.layoutLogin.txtLoginQr.setTextColor(Color.parseColor("#CCCCCC"));
        binding.layoutLogin.txtLoginSdt.setTextColor(Color.parseColor("#000000"));
        binding.layoutLogin.viewLoginQr.setVisibility(View.GONE);
        binding.layoutLogin.viewLoginSdt.setVisibility(View.VISIBLE);
        binding.layoutLogin.imgQrLogin.setVisibility(View.INVISIBLE);
        binding.layoutLogin.viewSdt.setVisibility(View.VISIBLE);
        binding.layoutLogin.viewPassword.setVisibility(View.VISIBLE);
        binding.layoutLogin.txtForgotPassword.setVisibility(View.VISIBLE);
        binding.layoutLogin.btConfirm.setVisibility(View.VISIBLE);
        viewQRLogin = false;
    }

    /**
     * Nhấn nút tạo lại mã QR
     */
    public void onGetQRCode(View view) {
        UiUtils.fixMultiClick(view);
        SocketManager.getInstance().recreateQRCode();
        binding.layoutLogin.progressBarLogin.setVisibility(View.VISIBLE);
        binding.layoutLogin.btGetQrCode.setVisibility(View.GONE);
    }

    /**
     * Nhấn nút xác nhận đăng nhập
     */
    public void onConfirmLogin(View view) {
        UiUtils.fixMultiClick(view);
        sdt = binding.layoutLogin.edtSdt.getText().toString().trim();
        password = binding.layoutLogin.edtPassword.getText().toString().trim();
        binding.layoutLogin.progressBarLogin.setVisibility(View.VISIBLE);
        if (sdt.equals("")) {
            onDialogError(getString(R.string.phone_number_error));
            binding.layoutLogin.progressBarLogin.setVisibility(View.GONE);
        } else if (password.equals("")) {
            onDialogError(getString(R.string.password_error));
            binding.layoutLogin.progressBarLogin.setVisibility(View.GONE);
        } else {
            BodyLogin bodyLogin = new BodyLogin(UiUtils.getModel(), sdt, password, getAppId(), getAppVersion(), getMac(), "fcm");
            LoginRepository loginRepository = new LoginRepository(this);
            loginRepository.GetLogin(bodyLogin, context, getUrlApi());
        }
    }

    /**
     * Nhấn đăng ký
     */
    public void onSignup(View view) {
        showViewSignUp();
    }

    /**
     * Nhấn nút đóng view đăng ký
     */
    public void onCloseSignUp(View v) {
        showViewLogin();
    }

    /**
     * Nhấn nút đóng view quá số lượng thiết bị
     */
    public void onCloseNoteDeviceLimit(View v) {
        showViewLogin();
    }

    /**
     * Nhấn nút đóng view quên mật khẩu
     */
    public void onCloseForgotPassword(View view) {
        showViewLogin();
    }

    /**
     * Nhấn nút gửi otp
     */
    public void onSendOTP(View view) {
        UiUtils.fixMultiClick(view);
        if (!mSendOTP) {
            checkSendOtp();
        }
    }

    /**
     * check đầu vào trước khi call gửi  otp
     */
    private void checkSendOtp() {
        if (binding.layoutSignUp.edtSdtSignUp.getText().toString().trim().equals("")) {
            onDialogError(getString(R.string.phone_number_error));
        } else if (binding.layoutSignUp.edtSdtSignUp.getText().toString().trim().length() < 10) {
            onDialogError(getString(R.string.long_phone_number));
        } else {
            BodySendOTP bodySendOTP = new BodySendOTP(binding.layoutSignUp.edtSdtSignUp.getText().toString().trim());
            SendOTPRepository sendOTPRepository = new SendOTPRepository(this);
            sendOTPRepository.sendOTPSignup(bodySendOTP, context, getUrlApi());
        }
    }

    /**
     * Nhấn nút gửi otp của giao diện quên mật khẩu
     */
    public void onSendOTPForgotPassword(View view) {
        if (!mSendOTPForgotPassword) {
            checkSendOtpForgotPassword();
        }
    }

    /**
     * check đầu vào trước khi call gửi  otp của giao diện quên mật khẩu
     */
    private void checkSendOtpForgotPassword() {
        if (binding.layoutForgotPassword.edtSdt.getText().toString().trim().equals("")) {
            onDialogError(getString(R.string.phone_number_error));
        } else if (binding.layoutForgotPassword.edtSdt.getText().toString().trim().length() < 10) {
            onDialogError(getString(R.string.long_phone_number));
        } else {
            BodySendOTPForgotPassword bodySendOTPForgotPassword = new BodySendOTPForgotPassword(binding.layoutForgotPassword.edtSdt.getText().toString().trim());
            SendOTPForgotPasswordRepository sendOTPForgotPasswordRepository = new SendOTPForgotPasswordRepository(this);
            sendOTPForgotPasswordRepository.sendOTPForgotPassword(bodySendOTPForgotPassword, context, getUrlApi());
        }
    }

    /**
     * Nhấn nút gửi lại otp của màn đăng ký
     */
    public void onResendOTP(View view) {
        checkSendOtp();
    }

    /**
     * Nhấn nút gửi lại otp của màn quên mật khẩu
     */
    public void onResendOTPForgotPassword(View view) {
        checkSendOtpForgotPassword();
    }

    /**
     * nhấn nút xác nhận đăng ký
     */
    public void onConfirmSignup(View view) {
        if (mConfirmSignUp) {
            setHintTxtSignUp();
            if (binding.layoutSignUp.edtOtp.getText().toString().trim().length() != 6) {
                onDialogError(getString(R.string.otp_error));
            } else if (binding.layoutSignUp.edtPasswordNew.getText().toString().trim().length() < 1) {
                onDialogError(getString(R.string.password_error));
            } else if (binding.layoutSignUp.edtPasswordNewAgain.getText().toString().trim().length() < 1) {
                onDialogError(getString(R.string.repassword_error));
            } else if (!binding.layoutSignUp.edtPasswordNew.getText().toString().trim().equals(binding.layoutSignUp.edtPasswordNewAgain.getText().toString().trim())) {
                onDialogError(getString(R.string.repasswork_not_match));
            } else {
                binding.layoutSignUp.progressBarSignUp.setVisibility(View.VISIBLE);
                this.sdt = binding.layoutSignUp.edtSdtSignUp.getText().toString().trim();
                this.password = binding.layoutSignUp.edtPasswordNew.getText().toString().trim();
                BodySignUp bodySignUp = new BodySignUp(binding.layoutSignUp.edtSdtSignUp.getText().toString().trim(), binding.layoutSignUp.edtOtp.getText().toString().trim(), getAppId(), binding.layoutSignUp.edtPasswordNew.getText().toString().trim(), binding.layoutSignUp.edtPasswordNewAgain.getText().toString().trim(), "fcm");
                SignUpRepository signUpRepository = new SignUpRepository(this);
                signUpRepository.getSignup(bodySignUp, context, getUrlApi());
            }
        }
    }

    /**
     * nhấn nút xác nhận đặt lại mật khẩu
     */
    public void onConfirmForgotPassword(View view) {
        if (mConfirmForgotPassword) {
            setHintTxtForgotPassword();
            if (binding.layoutForgotPassword.edtOtp.getText().toString().trim().length() != 6) {
                onDialogError(getString(R.string.otp_error));

            } else if (binding.layoutForgotPassword.edtPasswordNew.getText().toString().trim().length() < 1) {
                onDialogError(getString(R.string.password_error));
            } else if (binding.layoutForgotPassword.edtPasswordNewAgain.getText().toString().trim().length() < 1) {
                onDialogError(getString(R.string.repassword_error));
            } else if (!binding.layoutForgotPassword.edtPasswordNew.getText().toString().trim().equals(binding.layoutForgotPassword.edtPasswordNewAgain.getText().toString().trim())) {
                onDialogError(getString(R.string.repasswork_not_match));
            } else {
                binding.layoutForgotPassword.progressBar.setVisibility(View.VISIBLE);
                this.sdt = binding.layoutForgotPassword.edtSdt.getText().toString().trim();
                this.password = binding.layoutForgotPassword.edtPasswordNew.getText().toString().trim();
                BodyForgotPassword bodyForgotPassword = new BodyForgotPassword(binding.layoutForgotPassword.edtSdt.getText().toString().trim(), binding.layoutForgotPassword.edtOtp.getText().toString().trim(), getAppId(), password, binding.layoutForgotPassword.edtPasswordNewAgain.getText().toString().trim(), "fcm");
                ForgotPasswordRepository forgotPasswordRepository = new ForgotPasswordRepository(this);
                forgotPasswordRepository.getForgotPassword(bodyForgotPassword, context, getUrlApi());

            }
        }
    }

    @Override
    public void onReceiveQRCode(String qr, long time) {
        new QRUtils().setQRView(qr, binding.layoutLogin.imgQrLogin, 500);
        setNewQRCode(false);
        new Handler().postDelayed(() -> {
            setNewQRCode(true);
        }, time * 1000 - new Date().getTime());
    }

    @Override
    public <T> void onQRScanSuccessful(T obj) {
        getSharedPreference().setAuthOption(2);
        getSharedPreference().setAuth((QREvent) obj);
        getSharedPreference().setPhoneNumber(((QREvent) obj).getData().get(0).getAccount());
        SocketManager.getInstance().init(context, getSocketBaseUrl(), null);
        SocketManager.getInstance().connect();
    }

    @Override
    public void onConnectTimeout() {
        setNewQRCode(true);
    }

    /**
     * check để hiển thị nút tạo lại mã
     */
    public void setNewQRCode(boolean view) {
        NewQRCode = view;
        if (viewQRLogin) {
            if (view) {
                binding.layoutLogin.btGetQrCode.setVisibility(View.VISIBLE);
                binding.layoutLogin.imgQrLogin.setVisibility(View.GONE);
                binding.layoutLogin.progressBarLogin.setVisibility(View.GONE);
            } else {
                binding.layoutLogin.imgQrLogin.setVisibility(View.VISIBLE);
                binding.layoutLogin.btGetQrCode.setVisibility(View.GONE);
                binding.layoutLogin.progressBarLogin.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void dataLogin(Login login, int status, String message, JSONObject jsonObject) {
        hideProgressbar();
        if (status == 200) {
            getSharedPreference().setPhoneNumber(sdt);
            loginSuccessful(login);
        } else if (status == 403) {
            ArrayList<Device> device = new ArrayList<>();
            try {
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject object = data.getJSONObject(i);
                    device.add(new Device(object.getString("deviceMac"), object.getString("deviceName"), object.getString("accessToken"), object.getString("appId")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            showDeviceLimit(device);
        } else {
            onDialogError(message);
        }
    }

    /**
     * hiển thị các thiết bị đang đăng nhập
     */
    public void showDeviceLimit(ArrayList<Device> devices) {
        DeviceAdapter deviceAdapter = new DeviceAdapter(devices, context);
        deviceAdapter.DeviceClick(new DeviceClick() {
            @Override
            public void OnClick(int position) {
                //khởi tạo dialog xác nhận
                new ConfirmDialog(context, new ConfirmDialog.ConfirmDialogListener() {
                    @Override
                    public void onConfirm(ConfirmDialog dialog) {
                        LogoutAndLogin logoutAndLogin = new LogoutAndLogin(new LogoutAndLogin.Logout(devices.get(position).getAppId(), devices.get(position).getAccessToken()), new LogoutAndLogin.Login(sdt, password, getAppId(), getAppVersion(), getMac(), "fcm"));
                        LogoutAndLoginRepository logoutAndLoginRepository = new LogoutAndLoginRepository(BaseQRLoginAuthActivity.this);
                        logoutAndLoginRepository.GetLogoutAndLogin(logoutAndLogin, context, getUrlApi());
                    }

                    @Override
                    public void onCancel(ConfirmDialog dialog) {
                        dialog.dismiss();
                    }
                }).setTitle(getString(R.string.logout_device) + devices.get(position).getDeviceName())
                        .setContent(getString(R.string.content_logout_device))
                        .setActionBackground(R.color.black)
                        .show();
            }
        });
        binding.layoutDeviceLimit.recyclerDeviceLimit.setLayoutManager(new LinearLayoutManager(context));
        binding.layoutDeviceLimit.recyclerDeviceLimit.setAdapter(deviceAdapter);
        showViewDeviceLimit();
    }

    @Override
    public void dataSendOTP(DataServer dataServer, int status, String message) {
        if (status == 200) {
            onDialogError(getString(R.string.send_otp_success));
            sendOTPSuccess();
        } else {
            onDialogError(message);
        }
    }

    /**
     * Tạo bộ đếm ngược khi gửi otp thành công
     */
    public void sendOTPSuccess() {
        mSendOTP = true;
        mCheckOTP = true;
        binding.layoutSignUp.btSendOtp.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_bt_signout));
        binding.layoutSignUp.txtSendOtp.setVisibility(View.VISIBLE);
        binding.layoutSignUp.txtTimeOtp.setVisibility(View.VISIBLE);
        final int[] x = {60};
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                if (x[0] > 0) {
                    binding.layoutSignUp.txtTimeOtp.setText("OTP" + " (" + x[0] + "s)");
                    x[0]--;
                }

            }

            @Override
            public void onFinish() {
                binding.layoutSignUp.txtTimeOtp.setVisibility(View.GONE);

            }
        };
        countDownTimer.start();
    }

    @Override
    public void dataVerifyOTP(DataServer dataServer, int status, String message) {
        UiUtils.hideKeyboard(this);
        if (status == 200) {
            onDialogError(getString(R.string.otp_success));
            verifyOTPSuccess();
        } else if (status == 201) {
            onDialogError(getString(R.string.note_user_partner));
            showViewLogin();
        } else {
            onDialogError(message);
        }
    }

    /**
     * hiển thị giad diện sau khi xác thực otp màn đăng ký
     */
    public void verifyOTPSuccess() {
        binding.layoutSignUp.btSignUp.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_bt_login));
        binding.layoutSignUp.txtContentPassword.setVisibility(View.VISIBLE);
        binding.layoutSignUp.edtPasswordNew.setVisibility(View.VISIBLE);
        binding.layoutSignUp.edtPasswordNewAgain.setVisibility(View.VISIBLE);
        binding.layoutSignUp.txtTimeOtp.setVisibility(View.GONE);
        binding.layoutSignUp.txtSendOtp.setVisibility(View.GONE);
        mConfirmSignUp = true;
    }

    /**
     * hiển thị giad diện sau khi xác thực otp màn quên mật khẩu
     */
    public void verifyOTPForgotPasswordSuccess() {
        binding.layoutForgotPassword.btConfirm.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_bt_login));
        binding.layoutForgotPassword.txtContentPassword.setVisibility(View.VISIBLE);
        binding.layoutForgotPassword.edtPasswordNew.setVisibility(View.VISIBLE);
        binding.layoutForgotPassword.edtPasswordNewAgain.setVisibility(View.VISIBLE);
        binding.layoutForgotPassword.txtTimeOtp.setVisibility(View.GONE);
        binding.layoutForgotPassword.txtSendOtp.setVisibility(View.GONE);
        mConfirmForgotPassword = true;
    }

    @Override
    public void dataSignUp(DataServer dataServer, int status, String message, JSONObject data) {
        hideProgressbar();
        if (status == 200) {
            BodyLogin bodyLogin = new BodyLogin(UiUtils.getModel(), this.sdt, this.password, getAppId(), getAppVersion(), getMac(), "");
            LoginRepository loginRepository = new LoginRepository(this);
            loginRepository.GetLogin(bodyLogin, context, getUrlApi());
            setHintTxtSignUp();
        } else if (status == 400) {
            try {
                JSONArray jsonArray = data.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.getString("field").equals("password")) {
                        binding.layoutSignUp.txtPasswordNew.setVisibility(View.VISIBLE);
                        binding.layoutSignUp.txtPasswordNew.setText(jsonObject.getString("error"));
                    } else if (jsonObject.getString("field").equals("rePassword")) {
                        binding.layoutSignUp.txtPasswordNewAgain.setVisibility(View.VISIBLE);
                        binding.layoutSignUp.txtPasswordNewAgain.setText(jsonObject.getString("error"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            setHintTxtSignUp();
            onDialogError(message);
        }
    }

    /**
     * Ẩn thông báo lỗi mật khẩu ở màn quên mật khẩu
     */
    public void setHintTxtForgotPassword() {
        binding.layoutForgotPassword.txtPasswordNewError.setVisibility(View.GONE);
        binding.layoutForgotPassword.txtPasswordNewAgainError.setVisibility(View.GONE);
    }

    /**
     * Ẩn thông báo lỗi mật khẩu ở màn đăng ký
     */
    public void setHintTxtSignUp() {
        binding.layoutSignUp.txtPasswordNew.setVisibility(View.GONE);
        binding.layoutSignUp.txtPasswordNewAgain.setVisibility(View.GONE);
    }

    @Override
    public void dataSendOTPForgotPassword(DataServer dataServer, int status, String message) {
        if (status == 200) {
            onDialogError(getString(R.string.send_otp_success));
            sendOTPForgotPasswordSuccess();
        } else {
            onDialogError(message);
        }
    }

    /**
     * Tạo bộ đếm ngược khi gửi otp thành công ở màn quên mật khẩu
     */
    public void sendOTPForgotPasswordSuccess() {
        mSendOTPForgotPassword = true;
        mCheckOTPForgotPassword = true;
        binding.layoutForgotPassword.btSendOtp.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_bt_signout));
        binding.layoutForgotPassword.txtSendOtp.setVisibility(View.VISIBLE);
        binding.layoutForgotPassword.txtTimeOtp.setVisibility(View.VISIBLE);
        final int[] x = {60};
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                if (x[0] > 0) {
                    binding.layoutForgotPassword.txtTimeOtp.setText("OTP" + " (" + x[0] + "s)");
                    x[0]--;
                }

            }

            @Override
            public void onFinish() {
                binding.layoutForgotPassword.txtTimeOtp.setVisibility(View.GONE);

            }
        };
        countDownTimer.start();
    }

    @Override
    public void dataVerifyOTPForgotPassword(DataServer dataServer, int status, String message) {
        UiUtils.hideKeyboard(this);
        if (status == 200) {
            onDialogError(getString(R.string.otp_success));
            verifyOTPForgotPasswordSuccess();
        } else {
            onDialogError(message);
        }
    }

    @Override
    public void dataForgotPassword(DataServer dataServer, int status, String message, JSONObject data) {
        hideProgressbar();
        if (status == 200) {
            BodyLogin bodyLogin = new BodyLogin(UiUtils.getModel(), this.sdt, this.password, getAppId(), getAppVersion(), getMac(), "");
            LoginRepository loginRepository = new LoginRepository(this);
            loginRepository.GetLogin(bodyLogin, context, getUrlApi());
            setHintTxtForgotPassword();
        } else if (status == 400) {
            try {
                JSONArray jsonArray = data.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.getString("field").equals("password")) {
                        binding.layoutForgotPassword.txtPasswordNewError.setVisibility(View.VISIBLE);
                        binding.layoutForgotPassword.txtPasswordNewError.setText(jsonObject.getString("error"));
                    } else if (jsonObject.getString("field").equals("rePassword")) {
                        binding.layoutForgotPassword.txtPasswordNewAgainError.setVisibility(View.VISIBLE);
                        binding.layoutForgotPassword.txtPasswordNewAgainError.setText(jsonObject.getString("error"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            setHintTxtForgotPassword();
            onDialogError(message);
        }
    }
    /**
     * show dialog thông báo lỗi thay cho toast
     * */
    public void onDialogError(String message){
        new ErrorDialog(context, new ErrorDialog.DialogErrorListener() {
            @Override
            public void onRetry(ErrorDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void onCancel(ErrorDialog dialog) {
                dialog.dismiss();
            }
        }).setContent(message)
                .showBottomCancel(true)
                .setTextBottomRetry("Ok")
                .show();
    }

    protected abstract void loginSuccessful(Login login);


    /**
     * url call api
     */
    protected abstract String getUrlApi();

    /**
     * appId
     */
    protected abstract String getAppId();

    /**
     * version của app
     */
    protected abstract String getAppVersion();

    /**
     * địa chỉ mac của thiết bị
     */
    protected abstract String getMac();

}
