package com.sugon.wwb.test;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.identity.v2.Tenant;
import org.openstack4j.model.identity.v2.User;
import org.openstack4j.model.identity.v3.Authentication.Scope.Domain;
import org.openstack4j.model.identity.v3.Project;
import org.openstack4j.model.storage.object.SwiftAccount;
import org.openstack4j.model.storage.object.SwiftContainer;
import org.openstack4j.model.storage.object.options.CreateUpdateContainerOptions;
import org.openstack4j.openstack.OSFactory;

import com.google.common.io.ByteStreams;

public class MyDemoV2 {
	
	public static void main(String args[]){
		
		 /*Constant.USER_NAME = "cloud_admin";
		Constant.PASSWORD = "cloud_admin";
		Constant.DOMAIN_ID = "admin_domain";
		String ip = "10.0.38.207";
		String pro_id = "c52e9b0491054833ad9431811b2f4def";*/
		
		Constant.USER_NAME = "admin";
		Constant.PASSWORD = "svmsoft123";
		Constant.DOMAIN_ID = "default";
		String ip = "10.0.36.240";
		String pro_id = "434b1411a2c74de8b63a2d7f55c00a26";
		
		String path = String.format("http://%s:5000%s", ip,"/v2.0");
		Identifier project = Identifier.byId(pro_id);
		Identifier domain = Identifier.byName("default");
		//登陆
		OSClientV2 os = OSFactory.builderV2()
                .endpoint(path)
                .credentials(Constant.USER_NAME, Constant.PASSWORD)
               
                .tenantName("admin")
//                .scopeToProject(project)
//                .domainId(Constant.DOMAIN_ID)
                .authenticate();
		System.out.println("完成认证:"+os.getEndpoint().toString()); 
//        System.out.println(os.);
		List<? extends Server> list = os.compute().servers().list();
		for (Server server : list) {
			System.out.println(server.getName());
		}
		
        //模板列表 
        
//        List<? extends Flavor> flavors = os.compute().flavors().list();
//        for(Flavor flavor :flavors){
//            System.out.println(flavor.getName());
//        }
//        
      //domain
      /*  System.out.println("++++++++++++user列表++++++++++++");
        List<? extends User> domainList = os.identity().users().list();
       for (User domain2 : domainList) {
    	   System.out.println(domain2.getName());
       }
        
        //project
        System.out.println("++++++++++++project列表++++++++++++");
        List<? extends Tenant> proList = os.identity().tenants().list();
       	for (Tenant project2 : proList) {
			System.out.println(project2.getName());
		}*/
        
        //获取Swift账户
        System.out.println("获取Swift账户:");
        SwiftAccount swiftAccount = os.objectStorage().account().get();
        System.out.println(swiftAccount); 
        
        //获取对象存储元数据信息
        System.out.println("获取对象存储元数据信息:");
        Map metadata = new HashMap ();
        boolean result = os.objectStorage().account().updateMetadata(metadata);
        System.out.println(result); 
        
        System.out.println("获取容器信息:");
        List<? extends SwiftContainer> containers = os.objectStorage().containers().list();
        int containerCount = (int) swiftAccount.getContainerCount();
        System.out.println(containerCount);
//        for(int i=0; i
//        		System.out.println(containers.get(i).getName()+containers.get(i).getTotalSize()+"/n");
//        }

        System.out.println("新建容器:");
        os.objectStorage().containers().create("wwbContainer", CreateUpdateContainerOptions.create()
        .accessAnybodyRead()
        ); 
	}
}
