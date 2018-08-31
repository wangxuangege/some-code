# some-code

## dynamic-proxy
&nbsp;&nbsp;&nbsp;&nbsp;多种动态代理性能测试，结果如下所示，JDK动态代理消耗时间最小，性能最高。若有接口实现的场景需要实现动态代理，优先使用JDK动态代理。
~~~
Create Jdk Proxy : 32 ms
Create Cglib Proxy : 312 ms
Create Spring Cglib Proxy : 188 ms
Create Javassist Proxy : 109 ms
Create Javassist Bytecode Proxy : 125 ms
Run jdk proxy:62 ms,22,742,990 t/s
Run cglib proxy:406 ms,3,473,067 t/s
Run spring cglib proxy:344 ms,4,099,027 t/s
Run javassist proxy:500 ms,2,820,130 t/s
Run javassist bytecode proxy:141 ms,10,000,463 t/s
~~~

## dynamic-class
&nbsp;&nbsp;&nbsp;&nbsp;通过java代码生成一个类，然后再运行时候编译，然后调用类，执行其方法，其中使用了dubbo源码中的实现来编译类。


## mybatis
&nbsp;&nbsp;&nbsp;&nbsp;mybatis源码分析工程。

## Fluent Interface
&nbsp;&nbsp;&nbsp;&nbsp;通过动态代理实现链式调用。

## jsr269
&nbsp;&nbsp;&nbsp;&nbsp;利用JSR269规范，实现编译时候动态更改类行为。

## introspector
&nbsp;&nbsp;&nbsp;&nbsp;搞清楚java内省。