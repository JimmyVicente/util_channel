package com.example.util_channel;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * UtilChannelPlugin
 */
public class UtilChannelPlugin implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "util_channel");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
       if (call.method.equals("isInvalidateBiometrics")) {
            boolean resultValidate = isInvalidateBiometrics();
            result.success(resultValidate);
        } else if (call.method.equals("setInvalidateBiometrics")) {
            boolean resultValidate = setInvalidateBiometrics();
            result.success(resultValidate);
        } else if (call.method.equals("getMacAddress")) {
            result.success(getMacAddress());
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    //Util biometric
    private boolean setInvalidateBiometrics() {
        generateSecretKey(new KeyGenParameterSpec.Builder(
                KEY_NAME,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setUserAuthenticationRequired(true)
                // Invalidate the keys if the user has registered a new biometric
                // credential, such as a new fingerprint. Can call this method only
                // on Android 7.0 (API level 24) or higher. The variable
                .setInvalidatedByBiometricEnrollment(true)
                .build());
        return true;
    }

    private boolean isInvalidateBiometrics() {
        try {
            Cipher cipher = getCipher();
            SecretKey secretKey = getSecretKey();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            System.out.print("NO key has changed");
            return false;
        } catch (KeyPermanentlyInvalidatedException e) {
            System.out.print("YES key has changed");
            return true;
        } catch (InvalidKeyException e) {
            System.out.print("ERROR " + e.toString());
//            e.printStackTrace();
            return false;
        }
//        return false;
    }


    private void generateSecretKey(KeyGenParameterSpec keyGenParameterSpec) {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(keyGenParameterSpec);
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
        } catch (NoSuchProviderException e) {
//            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
        }
    }

    private SecretKey getSecretKey() {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            return ((SecretKey) keyStore.getKey(KEY_NAME, null));
        } catch (KeyStoreException e) {
//            e.printStackTrace();
            return null;
        } catch (CertificateException e) {
//            e.printStackTrace();
            return null;
        } catch (IOException e) {
//            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
            return null;
        } catch (UnrecoverableKeyException e) {
//            e.printStackTrace();//
            return null;
        }
    }

    private Cipher getCipher() {
        try {
            return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
            return null;
        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private String getMacAddress(){
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

}
