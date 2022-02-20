## 配置
1. config-location 与 configuration 配置不能同时出现

## 通用 Service

1. 新建 service 包
2. 新建接口 UserService 继承 `IService<T>`，泛型写所对应的实体类
3. 新建 service 的实现包 impl
4. 创建 UserServiceImpl 类
5. UserServiceImpl 继承 `ServiceImpl<UserMapper`, User>，第一个参数是要操作的 mapper 接口，第二个参数是对应的实体类
6. UserServiceImpl 实现 UserService 接口，并加上 `@Service` 注解