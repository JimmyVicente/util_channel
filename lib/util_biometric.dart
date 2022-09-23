import 'dart:async';
import 'util_channel.dart';

class UtilBiometric {
  static Future<bool> get isInvalidateBiometrics async {
    bool? result = await channel.invokeMethod('isInvalidateBiometrics');
    return result ?? false;
  }

  static Future<void> get setInvalidateBiometrics async {
    await channel.invokeMethod('setInvalidateBiometrics');
  }
}
