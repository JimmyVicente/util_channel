#import "UtilChannelPlugin.h"
#if __has_include(<util_channel/util_channel-Swift.h>)
#import <util_channel/util_channel-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "util_channel-Swift.h"
#endif

@implementation UtilChannelPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftUtilChannelPlugin registerWithRegistrar:registrar];
}
@end
