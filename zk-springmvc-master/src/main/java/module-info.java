module zk.springmvc {
	requires hibernate.core;
	requires spring.beans;
	requires spring.jdbc;
	requires spring.context;
	requires hibernate.jpa;
	requires spring.security.core;
	requires slf4j.api;
	requires zkbind;
	requires zkplus;
	requires zk;
	requires zul;
	requires bcprov.jdk15on;
	requires java.logging;
	requires web.push;
	requires servlet.api;
	requires gson;
	requires jose4j;
	requires zcommon;
//	requires aspectjweaver;
	requires org.apache.servicemix.bundles.aspectj;
	requires java.naming;

}