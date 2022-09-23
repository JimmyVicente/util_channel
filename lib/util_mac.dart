import 'dart:async';
import 'package:util_channel/util_channel.dart';

class UtilMac {
  static Future<String> macAddress() async {
    final String macID = await channel.invokeMethod('getMacAddress');
    return macID;
  }
}
