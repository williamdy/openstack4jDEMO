package com.sugon.wwb.test;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.compute.ServerService;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.HostResource;
import org.openstack4j.model.compute.Image;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.identity.v2.Tenant;
import org.openstack4j.model.identity.v2.User;
import org.openstack4j.model.identity.v3.Authentication.Scope.Domain;
import org.openstack4j.model.identity.v3.Project;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.storage.block.Volume;
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
        
        //project
        System.out.println("++++++++++++project列表++++++++++++");
        List<? extends Tenant> proList = os.identity().tenants().list();
       	for (Tenant project2 : proList) {
			System.out.println(project2.getName());
		}
        
		System.out.println( "++++++++++++++++ host长度 " + os.compute().host().list().size() + "+++++++++++") ; 
		for (HostResource host : os.compute().host().list()) {
			System.out.println(host.getHostName());
		}
		System.out.println( "++++++++++++++++ service长度 " + os.getSupportedServices().size() + "+++++++++++") ; 
		Set<ServiceType> supportedServices = os.getSupportedServices();
		for (ServiceType serviceType : supportedServices) {
			System.out.println(serviceType.name());
//			System.out.println(serviceType.getType());
		}
		
		//获取server列表
		ServerService servers = os.compute().servers();
		List<? extends Server> serverList = servers.list();
		System.out.println( "++++++++++++++++ server长度 " + serverList.size() + "+++++++++++") ; 
		
		for (Server server : serverList) {
			System.out.println(server.getName());
		}
		String serverName = "windows_wwb_v2_" + System.currentTimeMillis();
		ServerCreate newServer = Builders.server()
                .name(serverName)
                .flavor("1a2151ab-34cd-49fd-9118-e91ad7c53feb")
                .image("dd4d5f9f-3e62-4ed4-ae73-2ef1ee15e51d")
                .availabilityZone("nova:computenode")
                .build();
        newServer.addNetwork("90720a94-a6eb-4983-aa97-350118e62e47", "100.0.100.166");
        Server server = os.compute().servers().boot(newServer);
		System.out.println(server.getName());
		 /**
		  * availabilityZone是用户可见的，用户手动的来指定vm运行在哪些host上；Host aggregate是一种更智能的方式，是调度器可见的，影响调度策略的一个表达式。
		  */
//		System.out.println("获取Volume账户++++++++++++++++ Volume长度 " + serverList.size() + "+++++++++++");
		List<? extends Volume> volumes = os.blockStorage().volumes().list();
		System.out.println("获取Volume账户++++++++++++++++ Volume长度 " + volumes.size() + "+++++++++++");
		
		for (Volume volume : volumes) {
			System.out.println(volume.getId());
		}
		
		//获取images
		List<? extends Image> images = os.compute().images().list();
		System.out.println("获取images账户++++++++++++++++ images长度 " + images.size() + "+++++++++++");
		
		for (Image image : images) {
			System.out.println(image.getName() + " : " + image.getId());
		}
		//获取网络
		List<? extends Network> nets = os.networking().network().list();
		System.out.println("获取nets账户++++++++++++++++ nets长度 " + images.size() + "+++++++++++");
		
		for (Network net : nets) {
			System.out.println(net.getName() + " : " + net.getId());
		}
		
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
