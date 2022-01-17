1. mybatis 默认执行 sql 语句是手动提交方式，它默认关闭了自动提交功能，在 insert、update、delete
操作后需要手动提交事务

2. mybatis 最核心的就是通过 namespace + id 来找到你要执行的 sql 语句

3. SqlSession 不是线程安全的，所以使用的方式是：
首先，在方法的内部，执行 sql 语句之前，先获取 SqlSession 对象
接着，调用 SqlSession 的方法，执行 sql 语句
最后，调用 close() 关闭
这样就保证了 sqlSession 是方法内使用的局部变量

4. SSM 的开发步骤
* 接口中写抽象方法
* XML或注解中写SQL
* Service中调用接口
* Controller中调用Service
