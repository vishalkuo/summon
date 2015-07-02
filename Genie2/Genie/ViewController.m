//
//  ViewController.m
//  Genie
//
//  Created by Vishal Kuo on 2015-07-02.
//  Copyright (c) 2015 VishalKuo. All rights reserved.
//

#import "ViewController.h"
#import "AFNetworking.h"

@interface ViewController ()
-(void)refillMethod;
-(void)checkMethod;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
    [_refillBtn addTarget:self action:@selector(refillMethod) forControlEvents:UIControlEventTouchUpInside];
    
    [_checkBtn addTarget:self action:@selector(checkMethod) forControlEvents:UIControlEventTouchUpInside];
    
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)refillMethod{
    NSString *tableno = _tableNO.text;
    
    AFHTTPRequestOperationManager *manager= [AFHTTPRequestOperationManager manager];
    NSDictionary *params = @{@"tableno":tableno, @"requestCode": @"100", @"request":@"Refills"};
    [manager POST:@"http://172.16.102.146:3000/api/v1/tableRequest" parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"Error: %@", error);
    }];
    
}

-(void)checkMethod{
    NSString *tableno = _tableNO.text;
    
    AFHTTPRequestOperationManager *manager= [AFHTTPRequestOperationManager manager];
    NSDictionary *params = @{@"tableno":tableno, @"requestCode": @"200", @"request":@"Check"};
    [manager POST:@"http://172.16.102.146:3000/api/v1/tableRequest" parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"Error: %@", error);
    }];
}


@end
