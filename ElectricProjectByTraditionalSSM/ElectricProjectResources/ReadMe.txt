
1.该电商项目主要做了6个模块

后台管理系统、前台系统、用户系统、支付系统、关键字搜索系统、日志系统；

2.
①后台管理不多说，看代码和文档（使用easiui框架优化页面）
②③前台+用户系统User：
	a.首先在登录的时候远程调用用户系统进行登录
		两种方式：
			1	G:\MyCode\电商项目\WebService\电商项目中\2使用CXF工具的WebService\如何在前台系统中调用远程的用户接口\1不结合SSM框架使用webservice
			2	G:\MyCode\电商项目\WebService\电商项目中\2使用CXF工具的WebService\如何在前台系统中调用远程的用户接口\2使用SSM框架使用webservice
④支付系统：
	在前台系统的realpay.jsp中，使用restful风格访问支付系统
	注意：
		支付系统是调用支付宝的，好多都是支付宝给的，不用自己写（关注AlipayConfig中的return_url和PaymentController中的payment映射）
⑤关键字系统/solr关键字搜索系统
	前台系统的searchArea.jsp中，用户在搜索框中输入搜索内容，然后跳转到sale系统的ListController.java中keywords映射中，
	通过rest风格webservice中的get方法对keywords系统访问
	注意：访问到关键字系统后，需要开启solr服务器，具体配置都有（使用solr需要先往solr中添加数据，具体看文档）
		  日志系统中有个关于solr的参数封装，比较特殊所以使用KEYWORDS_T_MALL_SKU（com.atguigu.bean包下的bean-KEYWORDS_T_MALL_SKU.java中有解释），
		  还得在所有javabean的成员变量上加@Field注解
⑥日志系统
	在前台系统sale中的LoginController中的login映射中，当用户登录成功后，首先就是要写入日志系统，使用activeMq消息中间件；
	具体的配置即SSM框架中的使用看文档及知识点：G:\MyCode\电商项目\Mq-activemq-消息队列中间件
⑦redis缓存数据库的应用：
	在sale系统的ListController中的goto_list.do和get_list_by_attr.do中都应用了redis缓存数据库
