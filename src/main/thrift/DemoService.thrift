namespace java com.akkafun.service.thrift

service DemoService {
  string method1();

  i32 method2(1: i32 a, 2: i32 b);

  void method3();

}