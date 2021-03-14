package id.haerulmuttaqin.jobfinder.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import dagger.android.AndroidInjection;
import id.haerulmuttaqin.jobfinder.Constants;
import id.haerulmuttaqin.jobfinder.R;
import id.haerulmuttaqin.jobfinder.Utils;

public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity {

    public static final String TAG = "-->";
    
    private T binding;
    private V viewModel;
    private String tag = null;
    public AlertDialog.Builder progressBuilder;
    private ProgressDialog progressDialog;
    public Dialog dialog;

    public abstract int getBindingVariable();

    public abstract @LayoutRes int getLayoutId();

    public abstract V getViewModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performDependencyInjection();
        performDataBinding();
        hideKeyboard();
        createDialog(this);
        Utils.setupTheme(Utils.getBooleanPreference(this, Constants.DARK_MODE));
    }

    public void createDialog(Context context){
        progressBuilder = new AlertDialog.Builder(this, R.style.ProgressStyle);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_progress, null);
        progressBuilder.setView(view);
        dialog = progressBuilder.create();
        dialog.setCancelable(false);
        progressDialog = new ProgressDialog(this, R.style.ProgressDialogStyle);
        progressDialog.setMessage("Please wait... ");
        progressDialog.setCancelable(false);
    }

    public T getViewDataBinding() {
        return binding;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void showKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, 0);
            }
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    
    public void showProgress() {
        Log.w(TAG, "showProgress");
        try {
            hideKeyboard();
            if (dialog != null);
                dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgressDelay() {
        new Handler().postDelayed(() -> {
            try {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 3000);
    }

    public void hideProgress() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgress2() {
        if (progressDialog != null)
            progressDialog.show();
    }

    public void hideProgress2() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    private void performDataBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        this.viewModel = viewModel == null ? getViewModel() : viewModel;
        binding.setVariable(getBindingVariable(), viewModel);
        binding.executePendingBindings();
    }

    
    public boolean isNetworkConnected() {
        return Utils.isNetworkConnected(getApplicationContext());
    }
    

    public void performDependencyInjection() {
        AndroidInjection.inject(this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
