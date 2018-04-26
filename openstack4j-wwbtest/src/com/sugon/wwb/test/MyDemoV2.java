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
		//��½
		OSClientV2 os = OSFactory.builderV2()
                .endpoint(path)
                .credentials(Constant.USER_NAME, Constant.PASSWORD)
               
                .tenantName("admin")
//                .scopeToProject(project)
//                .domainId(Constant.DOMAIN_ID)
                .authenticate();
		System.out.println("�����֤:"+os.getEndpoint().toString()); 
//        System.out.println(os.);
		List<? extends Server> list = os.compute().servers().list();
		for (Server server : list) {
			System.out.println(server.getName());
		}
        
        //project
        System.out.println("++++++++++++project�б�++++++++++++");
        List<? extends Tenant> proList = os.identity().tenants().list();
       	for (Tenant project2 : proList) {
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
//			System.out.println(serviceType.getType());
		}
		
		//��ȡserver�б�
		ServerService servers = os.compute().servers();
		List<? extends Server> serverList = servers.list();
		System.out.println( "++++++++++++++++ server���� " + serverList.size() + "+++++++++++") ; 
		
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
		  * availabilityZone���û��ɼ��ģ��û��ֶ�����ָ��vm��������Щhost�ϣ�Host aggregate��һ�ָ����ܵķ�ʽ���ǵ������ɼ��ģ�Ӱ����Ȳ��Ե�һ�����ʽ��
		  */
//		System.out.println("��ȡVolume�˻�++++++++++++++++ Volume���� " + serverList.size() + "+++++++++++");
		List<? extends Volume> volumes = os.blockStorage().volumes().list();
		System.out.println("��ȡVolume�˻�++++++++++++++++ Volume���� " + volumes.size() + "+++++++++++");
		
		for (Volume volume : volumes) {
			System.out.println(volume.getId());
		}
		
		//��ȡimages
		List<? extends Image> images = os.compute().images().list();
		System.out.println("��ȡimages�˻�++++++++++++++++ images���� " + images.size() + "+++++++++++");
		
		for (Image image : images) {
			System.out.println(image.getName() + " : " + image.getId());
		}
		//��ȡ����
		List<? extends Network> nets = os.networking().network().list();
		System.out.println("��ȡnets�˻�++++++++++++++++ nets���� " + images.size() + "+++++++++++");
		
		for (Network net : nets) {
			System.out.println(net.getName() + " : " + net.getId());
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
