/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import static javaapplication1.JavaApplication1.configDevice;

/**
 *
 */
public class ConfigUtil {

    public static final String ROOT_FROM_FILE_PATH = "/Users/amberh3/dev/myscript/";
    public static final String ROOT_TO_FILE_PATH = "/Users/amberh3/dev/myscript/out/";

    static {
        File fromdir = new File(ROOT_FROM_FILE_PATH);
        if (!fromdir.exists()) {
            System.out.println("make dir: " + ROOT_FROM_FILE_PATH);
            fromdir.mkdir();
        }
        
        File todir = new File(ROOT_TO_FILE_PATH);
        if (!todir.exists()) {
            System.out.println("make dir: " + ROOT_TO_FILE_PATH);
            todir.mkdir();
        }
    }

    static void configPC(Scanner in) {
        System.out.println("input hostname: ");
        String hostNameStr = in.nextLine();
        System.out.println("---hostname: " + hostNameStr);

        System.out.println("input pc ip");
        String ipStr = in.nextLine();
        System.out.println("---pc ip: " + ipStr);

        System.out.println("input subnet mask: ");
        String subnetMaskStr = in.nextLine();
        System.out.println("---subnet mask:" + subnetMaskStr);

        System.out.println("input default gateway: ");
        String defaultGatewayStr = in.nextLine();
        System.out.println("---default gateway:" + defaultGatewayStr);

        String toFileName = hostNameStr + ".txt";
        String fromFile = ROOT_FROM_FILE_PATH + "pc.conf";
        String toFile = ROOT_TO_FILE_PATH + toFileName;

        //build the replacement keywords map, then process replacement
        ConfigFilesGenerator helper = new ConfigFilesGenerator(fromFile, toFile);
        Map<String, String> keywordsReplaceMap = helper.getKeywordsReplaceMap();
        keywordsReplaceMap.put("$ip$", ipStr);
        keywordsReplaceMap.put("$subnet_mask$", subnetMaskStr);
        keywordsReplaceMap.put("$ip_default_gateway$", defaultGatewayStr);

        List<String> appendlineList = helper.getAppendLinesList();

        System.out.println("DNS server ip? ");
        String dnsIpStr = in.nextLine();
        if (!dnsIpStr.isEmpty()) {
            System.out.println("---DNS server ip: " + dnsIpStr);
            String addedLine = "DNS Server: " + dnsIpStr;
            appendlineList.add(addedLine);
        }

        helper.generateConfigFile();

    }
    
    static void configSwitch(Scanner in){
        SwitchConfigUtil.configSwitch(in);
    }

    static void configMultiLayerSwitch(Scanner in) {
        RouterConfigUtil.configRoute(in);
    }

    static void configRoute(Scanner in) {

    }

}
