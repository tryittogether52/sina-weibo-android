//
//  WPError.m
//  WordPress
//
//  Created by Jorge Bernal on 4/17/12.
//  Copyright (c) 2012 WordPress. All rights reserved.
//

#import "WPError.h"
#import "WordPressAppDelegate.h"

NSString * const WPErrorResponseKey = @"wp_error_response";

@implementation WPError

+ (NSError *)errorWithResponse:(NSHTTPURLResponse *)response error:(NSError *)error {
    NSMutableDictionary *userInfo = [[error.userInfo mutableCopy] autorelease];
    [userInfo setValue:response forKey:WPErrorResponseKey];
    return [NSError errorWithDomain:error.domain code:error.code userInfo:[NSDictionary dictionaryWithDictionary:userInfo]];
}

+ (void)showAlertWithError:(NSError *)error title:(NSString *)title {
    NSString *message = nil;
    NSString *customTitle = nil;
    NSHTTPURLResponse *response = (NSHTTPURLResponse *)[error.userInfo objectForKey:WPErrorResponseKey];
    
    if ([error.domain isEqual:AFNetworkingErrorDomain]) {
        switch (error.code) {
            case NSURLErrorBadServerResponse:
                if (response) {
                    switch (response.statusCode) {
                        case 400:
                        case 405:
                        case 406:
                        case 411:
                        case 412:
                        case 413:
                        case 414:
                        case 415:
                        case 416:
                        case 417:
                            customTitle = NSLocalizedString(@"Incompatible site", @"");
                            message = [NSString stringWithFormat:NSLocalizedString(@"Your WordPress site returned a error %d.\nThat probably means you have some special configuration that is not compatible with this app.\nPlease let us know in the forums about it.", @""), response.statusCode];
                            break;
                        case 403:
                            customTitle = NSLocalizedString(@"Forbidden Access", @"");
                            message = NSLocalizedString(@"Received 'Forbidden Access'.\nIt seems there is a problem with your WordPress site", @"");
                            break;
                        case 500:
                        case 501:
                            customTitle = NSLocalizedString(@"Internal Server Error", @"");
                            message = NSLocalizedString(@"Received 'Internal Server Error'.\nIt seems there is a problem with your WordPress site", @"");
                            break;
                        case 502:
                        case 503:
                        case 504:
                            customTitle = NSLocalizedString(@"Temporary Server Error", @"");
                            message = NSLocalizedString(@"It seems your WordPress site is not accessible at this time, please try again later", @"");
                            break;
                        default:
                            break;
                    }
                }
                break;
                
            default:
                break;
        }
    }
    
    if (message == nil) {
        message = [error localizedDescription];
    }
    
    if (title == nil) {
        if (customTitle == nil) {
            title = @"error";
        } else {
            title = customTitle;
        }
    }
    
    [[WordPressAppDelegate sharedWordPressApp] showAlertWithTitle:title message:message];
}

+ (void)showAlertWithError:(NSError *)error {
    [self showAlertWithError:error title:nil];
}

@end
