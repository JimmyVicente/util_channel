import 'dart:async';
import 'package:flutter/services.dart';


class UtilChannel{
  static const MethodChannel _channel = MethodChannel('util_channel');

  static Future<bool> get isInvalidateBiometrics async {
    bool? result = await _channel.invokeMethod('isInvalidateBiometrics');
    return result ?? false;
  }

  static Future<void> get setInvalidateBiometrics async {
    await _channel.invokeMethod('setInvalidateBiometrics');
  }


  static Future<String> get macAddress async {
    final String macID = await _channel.invokeMethod('getMacAddress');
    return macID;
  }
}