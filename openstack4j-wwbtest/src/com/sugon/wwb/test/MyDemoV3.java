package com.sugon.wwb.test;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.compute.HostService;
import org.openstack4j.api.compute.ServerService;
import org.openstack4j.api.identity.EndpointURLResolver;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.core.transport.Config;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.HostResource;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.identity.URLResolverParams;
import org.openstack4j.model.identity.v3.Authentication.Scope.Domain;
import org.openstack4j.model.identity.v3.Project;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.object.SwiftAccount;
import org.openstack4j.model.storage.object.SwiftContainer;
import org.openstack4j.model.storage.object.options.CreateUpdateContainerOptions;
import org.openstack4j.openstack.OSFactory;
import org.openstack4j.openstack.identity.internal.DefaultEndpointURLResolver;

import com.google.common.io.ByteStreams;

public class MyDemoV3 {
	
	public static void main(String args[]){
		
	/*	 Constant.USER_NAME = "cloud_admin";
		Constant.PASSWORD = "cloud_admin";
		Constant.DOMAIN_ID = "admin_domain";
		String ip = "10.0.38.207";
		String pro_id = "c52e9b0491054833ad9431811b2f4def";*/
		
		Constant.USER_NAME = "admin";
		Constant.PASSWORD = "svmsoft123";
		Constant.DOMAIN_ID = "default";
		String ip = "10.0.36.240";
		String pro_id = "434b1411a2c74de8b63a2d7f55c00a26";
		
		String path = String.format("http://%s:5000%s", ip,"/v3");
		Identifier project = Identifier.byId(pro_id);
		Identifier domain = Identifier.byName(Constant.DOMAIN_ID );
		Config config = Config.newConfig();
		EndpointURLResolver endpointURLResolver = new DefaultEndpointURLResolver();
//		endpointURLResolver
		config.withEndpointURLResolver(endpointURLResolver);
		
		//��½
		OSClientV3 os = OSFactory.builderV3()
                .endpoint(path)
                .credentials(Constant.USER_NAME, Constant.PASSWORD,domain)
//                .scopeToProject(Identifier.byName("project_test"))
                .scopeToProject(project)
               .domainName(Constant.DOMAIN_ID)
//               .withConfig(config )
                .authenticate();
        	
        System.out.println(os.getToken().getVersion());
		
        //ģ���б� 
        
        List<? extends Flavor> flavors = os.compute().flavors().list();
        for(Flavor flavor :flavors){
            System.out.println(flavor.getName());
        }
        
      //domain
        System.out.println("++++++++++++token project�б�++++++++++++");
        Project project1 = os.getToken().getProject();
        System.out.println(project1.getName());
        System.out.println("++++++++++++token domain�б�++++++++++++");
        org.openstack4j.model.identity.v3.Domain domain1 =  project1.getDomain();
        System.out.println(domain1.getName());
        List<? extends  org.openstack4j.model.identity.v3.Domain> availableDomainScopes = os.identity().tokens().getDomainScopes(os.getToken().getId());
        for ( org.openstack4j.model.identity.v3.Domain domain2 : availableDomainScopes) {
			System.out.println(domain2.getName());
		}
       
        System.out.println("++++++++++++domain�б�++++++++++++");
        System.out.println(os.identity().tokens().check(os.getToken().getId()));
        
        List<? extends org.openstack4j.model.identity.v3.Domain> domainList = os.identity().domains().list();
       for (org.openstack4j.model.identity.v3.Domain domain2 : domainList) {
    	   System.out.println(domain2.getName());
       }
       
        
        //project
        System.out.println("++++++++++++project�б�++++++++++++");
        List<? extends Project> proList = os.identity().projects().list();
       	for (Project project2 : proList) {
			System.out.println(project2.getName());
		}
        
		System.out.println( "++++++++++++++++ host���� " + os.compute().host().list().size() + "+++++++++++") ; 
		for (HostResource host : os.compute().host().list()) {
			System.out.println(host.getHostName());
		}
		System.out.println( "++++++++++++++++ service���� " + os.getSupportedServices().size() + "+++++++++++") ; 
		Set<ServiceType> supportedServices = os.getSupportedServices();
		for (ServiceType serviceType : supportedServices) {
			System.out.println(serviceType.name());
			System.out.println(serviceType.getType());
		}
		
		//��ȡserver�б�
		ServerService servers = os.compute().servers();
		List<? extends Server> serverList = servers.list();
		System.out.println( "++++++++++++++++ server���� " + serverList.size() + "+++++++++++") ; 
		
		for (Server server : serverList) {
			System.out.println(server.getName());
		}
//		System.out.println("��ȡVolume�˻�++++++++++++++++ Volume���� " + serverList.size() + "+++++++++++");
		List<? extends Volume> volumes = os.blockStorage().volumes().list();
		System.out.println("��ȡVolume�˻�++++++++++++++++ Volume���� " + volumes.size() + "+++++++++++");
		
		for (Volume volume : volumes) {
			System.out.println(volume.getId());
		}
        //��ȡSwift�˻�
        System.out.println("��ȡSwift�˻�:");
        SwiftAccount swiftAccount = os.objectStorage().account().get();
        System.out.println(swiftAccount); 
        
        //��ȡ����洢Ԫ������Ϣ
        System.out.println("��ȡ����洢Ԫ������Ϣ:");
        Map metadata = new HashMap ();
        boolean result = os.objectStorage().account().updateMetadata(metadata);
        System.out.println(result); 
        
        System.out.println("��ȡ������Ϣ:");
        List<? extends SwiftContainer> containers = os.objectStorage().containers().list();
        int containerCount = (int) swiftAccount.getContainerCount();
        System.out.println(containerCount);
//        for(int i=0; i
//        		System.out.println(containers.get(i).getName()+containers.get(i).getTotalSize()+"/n");
//        }

        System.out.println("�½�����:");
        os.objectStorage().containers().create("wwbContainer", CreateUpdateContainerOptions.create()
        .accessAnybodyRead()
        ); 
	}
}